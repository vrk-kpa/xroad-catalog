package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.lister.entity.Member;
import fi.vrk.xroad.catalog.lister.entity.Wsdl;

import java.util.Date;
import java.util.List;

/**
 * CRUD methods for catalog objects. no business logic (e.g. hash calculation),
 * just persistence-related logic.
 * Catalog entities (member, subsystem, service, wsdl) have time stamps created, updated and deleted.
 * They are used so that "updated" is changed always when entity is updated in any way, including
 * creation and deletion. This is important since getMembers(Date updatedSince) only checks
 * updated-field and ignores created & deleted.
 */
public interface CatalogService {

    /**
     * Gets all members, regardless of when they were updated.
     * Fetches all data from graph member->subsystem->service->wsdl
     * @return
     */
    Iterable<Member> getMembers();

    /**
     * Gets all members that have been changed (created) after given time.
     * Change is determined by field "updated".
     * Fetches all data from graph member->subsystem->service->wsdl.
     * @param updatedAfter
     * @return
     */
    Iterable<Member> getMembers(Date updatedAfter);

    /**
     * Saves member->subsystem->service->wsdl graph.
     * Creates items that are new, updates ones that are updated.
     * Marks previously existing subsystems and services that now do not exist anymore,
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
     * Member is also marked as "updated".
     * Marks the items from full graph member->subsystem->service->wsdl as deleted (and updated).
     * @param memberId
     * @throws Exception if member does not exist or was already deleted
     */
    void deleteMember(long memberId);
}
