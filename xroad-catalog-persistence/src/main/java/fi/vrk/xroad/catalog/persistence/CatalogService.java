package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

/**
 * CRUD methods for catalog objects. no business logic (e.g. hash calculation),
 * just persistence-related logic.
 * Catalog entities (member, subsystem, service, wsdl) have time stamps created, updated and deleted.
 * They are used so that "updated" is changed always when entity is updated in any way, including
 * creation and deletion. This is important since getActiveMembers(Date updatedSince) only checks
 * updated-field and ignores created & deleted.
 */
public interface CatalogService {

    // TODO: make it NOT fetch wsdl yet
    /**
     * Gets all non-removed members, regardless of when they were changed.
     * Fetches all data from graph member->subsystem->service->wsdl
     */
    Iterable<Member> getActiveMembers();

    /**
     * Same as {@link #getActiveMembers()} except that returns also
     * removed items
     */
    Iterable<Member> getAllMembers();

    // TODO: make it NOT fetch wsdl yet

    /**
     * Gets all non-removed members that have been changed (created) after given time.
     * Change is determined by field "changed".
     * Fetches all data from graph member->subsystem->service->wsdl.
     * @param changedAfter
     * @return
     */
    Iterable<Member> getActiveMembers(Date changedAfter);

    /**
     * Same as {@link #getActiveMembers(Date)} except that returns also removed items
     * @param changedAfter
     * @return
     */
    Iterable<Member> getAllMembers(Date changedAfter);

    /**
     * Mainly for testing use
     * @return
     */
    Iterable<Member> getSubsystems();

    /**
     * Returns the full Wsdl object. Only returns active ones, removed are not found.
     * @return Wsdl, if any, null if not found
     * @throws RuntimeException if multiple matches found.
     */
    Wsdl getWsdl(String externalId);

    /**
     * Stores given members and subsystems. This should be the full dataset of both items
     * - items not included in the parameters are marked as removed, if the existed previously.
     *
     * "Full service": updates all status timestamps (created/changed/fetched/removed) automatically,
     * and knows whether to update existing items or create new ones.
     *
     * Does not touch the child items (service, wsdl). If creating new subsystems, the
     * service collection will be empty.
     *
     * @param members all Members that currently exist. If some members are missing from
     *                the collection, they are (marked) removed. Member should have
     *                member.subsystems collection populated, and each subsystem should
     *                have subsystem.member populated as well.
     */
    void saveAllMembersAndSubsystems(Collection<Member> members);

    /**
     * Stores services for given subsystem.
     * @param subsystem identifier info for subsystem
     * @param service services
     */
    void save(Subsystem subsystem, Collection<Service> service);

    Wsdl saveWsdl(long serviceId, Wsdl wsdl);

}
