package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.StreamSupport;

@Slf4j
@Component("catalogService")
@Transactional
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SubsystemRepository subsystemRepository;

    @Autowired
    WsdlRepository wsdlRepository;

    @Override
    public Iterable<Member> getActiveMembers() {
        return memberRepository.findAllActive();
    }

    @Override
    public Iterable<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Iterable<Member> getActiveMembers(Date changedAfter) {
        return memberRepository.findActiveChangedSince(changedAfter);
    }

    @Override
    public Iterable<Member> getAllMembers(Date changedAfter) {
        return memberRepository.findAllChangedSince(changedAfter);
    }

    @Override
    public Iterable<Member> getSubsystems() {
        return null;
    }

    @Override
    public Wsdl getWsdl(String externalId) {
        List<Wsdl> matches = wsdlRepository.findByExternalId(externalId);
        if (matches.size() > 1) {
            throw new IllegalStateException("multiple matches found to " + externalId + ": " + matches);
        } else if (matches.size() == 1) {
            return matches.iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public void saveAllMembersAndSubsystems(Collection<Member> members) {
        Date now = new Date();
        // process members
        Map<MemberId, Member> unprocessedOldMembers = new HashMap<>();
        StreamSupport.stream(memberRepository.findAll().spliterator(), false)
                .forEach(member -> unprocessedOldMembers.put(member.createKey(), member));

        for (Member member: members) {
            Member oldMember = unprocessedOldMembers.get(member.createKey());
            if (oldMember == null) {
                // brand new item
                member.getStatusInfo().setTimestampsForNew(now);
                for (Subsystem subsystem: member.getAllSubsystems()) {
                    subsystem.getStatusInfo().setTimestampsForNew(now);
                    subsystem.setMember(member);
                }
                member = memberRepository.save(member);
            } else {
                oldMember.updateWithDataFrom(member, now);
                // process subsystems for the old member
                Map<SubsystemId, Subsystem> unprocessedOldSubsystems = new HashMap<>();
                for (Subsystem subsystem: oldMember.getAllSubsystems()) {
                    unprocessedOldSubsystems.put(subsystem.createKey(), subsystem);
                }
                for (Subsystem subsystem: member.getAllSubsystems()) {
                    Subsystem oldSubsystem = unprocessedOldSubsystems.get(subsystem.createKey());
                    if (oldSubsystem == null) {
                        // brand new item, add it
                        subsystem.getStatusInfo().setTimestampsForNew(now);
                        subsystem.setMember(oldMember);
                        oldMember.getAllSubsystems().add(subsystem);
                    } else if (oldSubsystem.getStatusInfo().isRemoved()) {
                        // TODO: maybe refactor shared code...if reasonable
                        // resurrect this subsystem
                        oldSubsystem.getStatusInfo().setRemoved(null);
                        oldSubsystem.getStatusInfo().setChanged(now);
                        oldSubsystem.getStatusInfo().setFetched(now);
                    } else {
                        // already existing subsystem, no fields to update except timestamp
                        oldSubsystem.getStatusInfo().setFetched(now);
                    }
                    unprocessedOldSubsystems.remove(subsystem.createKey());
                }
                // remaining old subsystems - that were not included in member.subsystems -
                // are removed (if not already)
                for (Subsystem oldToRemove: unprocessedOldSubsystems.values()) {
                    StatusInfo status = oldToRemove.getStatusInfo();
                    if (!status.isRemoved()) {
                        status.setTimestampsForRemoved(now);
                    }
                }

                member = memberRepository.save(oldMember);
            }
            unprocessedOldMembers.remove(member.createKey());
        }
        // now unprocessedOldMembers should all be removed (either already removed, or will be now)
        for (Member oldToRemove: unprocessedOldMembers.values()) {
            StatusInfo status = oldToRemove.getStatusInfo();
            if (!status.isRemoved()) {
                status.setTimestampsForRemoved(now);
            }
            for (Subsystem subsystem: oldToRemove.getAllSubsystems()) {
                if (!subsystem.getStatusInfo().isRemoved()) {
                    subsystem.getStatusInfo().setTimestampsForRemoved(now);
                }
            }
        }
    }

    @Override
    public void saveServices(Subsystem subsystem, Collection<Service> services) {
        assert subsystem != null;
        assert subsystem.getMember() != null;
        Subsystem oldSubsystem = subsystemRepository.findByNaturalKey(subsystem.getMember().getXRoadInstance(),
                subsystem.getMember().getMemberClass(), subsystem.getMember().getMemberCode(),
                subsystem.getSubsystemCode());
        if (oldSubsystem == null) {
            throw new IllegalStateException("subsystem " + subsystem + " not found!");
        }

        Date now = new Date();

        Map<ServiceId, Service> unprocessedOldServices = new HashMap<>();
        oldSubsystem.getAllServices().stream().forEach(s -> unprocessedOldServices.put(s.createKey(), s));

        for (Service service: services) {
            Service oldService = unprocessedOldServices.get(service.createKey());
            if (oldService == null) {
                // brand new item, add it
                service.getStatusInfo().setTimestampsForNew(now);
                service.setSubsystem(oldSubsystem);
                oldSubsystem.getAllServices().add(service);
            } else if (oldService.getStatusInfo().isRemoved()) {
                // resurrect this service
                oldService.getStatusInfo().setRemoved(null);
                oldService.getStatusInfo().setChanged(now);
                oldService.getStatusInfo().setFetched(now);
            } else {
                // already existing subsystem, no fields to update except timestamp
                oldService.getStatusInfo().setFetched(now);
            }
            unprocessedOldServices.remove(service.createKey());
        }

        // now unprocessedOldServices should all be removed (either already removed, or will be now)
        for (Service oldToRemove: unprocessedOldServices.values()) {
            StatusInfo status = oldToRemove.getStatusInfo();
            if (!status.isRemoved()) {
                status.setTimestampsForRemoved(now);
            }
        }

    }

    @Override
    public Wsdl saveWsdl(long serviceId, Wsdl wsdl) {
        return null;
    }

}
