package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.lister.entity.Member;
import fi.vrk.xroad.catalog.lister.entity.Service;
import fi.vrk.xroad.catalog.lister.entity.Subsystem;
import fi.vrk.xroad.catalog.lister.entity.Wsdl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * CRUD methods for catalog objects. no business logic, just persistence logic.
 */
@Slf4j
@Component("catalogService")
@Transactional
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    MemberRepository memberRepository;

    /**
     * Gets all members, regardless of when they were updated.
     * @return
     */
    @Override
    public Iterable<Member> getMembers() {
        return memberRepository.findAll();
    }

    /**
     * Gets all members that have been changed (created) after given time.
     * @param changedAfter
     * @return
     */
    @Override
    public List<Member> getMembers(Date changedAfter) {
        return null;
    }

    /**
     * Saves member->subsystem->service->wsdl graph.
     * Creates items that are new, updates ones that are updated.
     * Marks pre-existign subsystems and services that do not exist anymore,
     * as deleted.
     * @param member
     * @return
     */
    @Override
    public Member saveMember(Member member) {
        return null;
    }

    /**
     * Saves one wsdl. Overwrites previous one if there was one.
     * @param serviceId
     * @param wsdl
     * @return
     */
    @Override
    public Wsdl saveWsdl(long serviceId, Wsdl wsdl) {
        return null;
    }

    /**
     * Marks one member as deleted.
     * Marks the items from full graph member->subsystem->service->wsdl as deleted.
     * @param memberId
     * @throws Exception if member does not exist or was already deleted
     */
    @Override
    public void deleteMember(long memberId) {
    }
}
