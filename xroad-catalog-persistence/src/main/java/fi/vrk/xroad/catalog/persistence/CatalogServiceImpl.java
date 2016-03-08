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

    @Override
    public Iterable<Member> getMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Iterable<Member> getMembers(Date changedAfter) {
        return memberRepository.findChangedSince(changedAfter);
    }

    @Override
    public Iterable<Member> getSubsystems() {
        return null;
    }

    @Override
    public Wsdl getWsdl(String externalId) {
        return null;
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
                for (Subsystem subsystem: member.getSubsystems()) {
                    subsystem.getStatusInfo().setTimestampsForNew(now);
                    subsystem.setMember(member);
                }
                member = memberRepository.save(member);
            } else {
                oldMember.updateWithDataFrom(member, now);
                // process subsystems for the old member
                Map<SubsystemId, Subsystem> unprocessedOldSubsystems = new HashMap<>();
                for (Subsystem subsystem: oldMember.getSubsystems()) {
                    unprocessedOldSubsystems.put(subsystem.createKey(), subsystem);
                }
                for (Subsystem subsystem: member.getSubsystems()) {
                    Member oldSubsystem = unprocessedOldMembers.get(subsystem.createKey());
                    if (oldSubsystem == null) {
                        // brand new item, add it
                        subsystem.getStatusInfo().setTimestampsForNew(now);
                        subsystem.setMember(oldMember);
                        oldMember.getSubsystems().add(subsystem);
                    }
                    // existing items (oldSystem != null) have no updatable fields, so there's
                    // nothing to do
                    unprocessedOldSubsystems.remove(subsystem.createKey());
                    // remaining old subsystems are removed (if not already)
                    for (Subsystem oldToRemove: unprocessedOldSubsystems.values()) {
                        StatusInfo status = oldToRemove.getStatusInfo();
                        if (!status.isRemoved()) {
                            status.setTimestampsForRemoved(now);
                        }
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
            for (Subsystem subsystem: oldToRemove.getSubsystems()) {
                if (!subsystem.getStatusInfo().isRemoved()) {
                    subsystem.getStatusInfo().setTimestampsForRemoved(now);
                }
            }
        }
    }

    @Override
    public void save(Subsystem subsystem, Collection<Service> service) {

    }

    @Override
    public Wsdl saveWsdl(long serviceId, Wsdl wsdl) {
        return null;
    }

}
