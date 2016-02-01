package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.lister.entity.Member;
import fi.vrk.xroad.catalog.lister.entity.Wsdl;

import java.util.Date;
import java.util.List;

public interface CatalogService {

    /**
     * Gets all members, regardless of when they were updated.
     * Fetches ALL data from graph member->subsystem->service->wsdl
     * @return
     */
    Iterable<Member> getMembers();

    /**
     * Gets all members that have been changed (created) after given time.
     * @param changedAfter
     * @return
     */
    List<Member> getMembers(Date changedAfter);

    /**
     * Saves member->subsystem->service->wsdl graph.
     * Creates items that are new, updates ones that are updated.
     * Marks pre-existign subsystems and services that do not exist anymore,
     * as deleted.
     * @param member
     * @return
     */
    Member saveMember(Member member);

    /**
     * Saves one wsdl. Overwrites previous one if there was one.
     * @param serviceId
     * @param wsdl
     * @return
     */
    Wsdl saveWsdl(long serviceId, Wsdl wsdl);

    /**
     * Marks one member as deleted.
     * Marks the items from full graph member->subsystem->service->wsdl as deleted.
     * @param memberId
     * @throws Exception if member does not exist or was already deleted
     */
    void deleteMember(long memberId);
}
