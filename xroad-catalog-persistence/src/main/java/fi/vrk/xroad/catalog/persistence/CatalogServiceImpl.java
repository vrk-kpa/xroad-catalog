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
    ServiceRepository serviceRepository;

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
                    } else {
                        oldSubsystem.getStatusInfo().setTimestampsForFetched(now);
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
    public void saveServices(SubsystemId subsystemId, Collection<Service> services) {
        assert subsystemId != null;
        Subsystem oldSubsystem = subsystemRepository.findByNaturalKey(subsystemId.getXRoadInstance(),
                subsystemId.getMemberClass(), subsystemId.getMemberCode(),
                subsystemId.getSubsystemCode());
        if (oldSubsystem == null) {
            throw new IllegalStateException("subsystem " + subsystemId + " not found!");
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
            } else {
                oldService.getStatusInfo().setTimestampsForFetched(now);
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
    public void saveWsdl(SubsystemId subsystemId, ServiceId serviceId, Wsdl wsdl) {
        assert subsystemId != null;
        assert serviceId != null;
        Service oldService = serviceRepository.findByNaturalKey(subsystemId.getXRoadInstance(),
                subsystemId.getMemberClass(), subsystemId.getMemberCode(),
                subsystemId.getSubsystemCode(), serviceId.getServiceCode(),
                serviceId.getServiceVersion());
        if (oldService == null) {
            throw new IllegalStateException("service " + serviceId + " not found!");
        }
        Date now = new Date();
        Wsdl oldWsdl = oldService.getWsdl();
        if (oldWsdl == null) {
            wsdl.initializeExternalId();
            wsdl.getStatusInfo().setTimestampsForNew(now);
            oldService.setWsdl(wsdl);
            wsdl.setService(oldService);
            wsdlRepository.save(wsdl);
        } else {
            if (oldWsdl.getStatusInfo().isRemoved()) {
                // resurrect
                oldWsdl.setData(wsdl.getData());
                oldWsdl.getStatusInfo().setChanged(now);
                oldWsdl.getStatusInfo().setRemoved(null);
                oldWsdl.getStatusInfo().setFetched(now);
            } else {
                // update existing
                boolean wsdlChanged = !oldWsdl.getData().equals(wsdl.getData());
                if (wsdlChanged) {
                    oldWsdl.getStatusInfo().setChanged(now);
                }
                oldWsdl.getStatusInfo().setFetched(now);
            }
        }
    }

}
