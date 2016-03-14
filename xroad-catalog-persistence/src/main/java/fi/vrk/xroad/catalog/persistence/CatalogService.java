package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.*;

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

    /**
     * Gets all non-removed members, regardless of when they were changed.
     * Fetches all data from graph member->subsystem->service.
     * Does NOT fetch wsdl.
     */
    Iterable<Member> getActiveMembers();

    /**
     * Same as {@link #getActiveMembers()} except that returns also
     * removed items
     */
    Iterable<Member> getAllMembers();

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
     * Stores services for given subsystem. Does not modify the associated Subsystem or
     * the wsdl.
     * @param subsystem identifier info for subsystem. Also needs to have subsystem.member
     *                  populated properly.
     * @param service services
     */
    void saveServices(SubsystemId subsystem, Collection<Service> service);

    /**
     * Saves given wsdl. The wsdl can either be a new one, or an update to an existing one.
     * Updates "changed" field based on whether data is different compared to last time.
     * @param subsystemId identifier of the subsystem
     * @param serviceId identifier of the service
     * @param wsdl the actual wsdl
     * @return
     */
    void saveWsdl(SubsystemId subsystemId, ServiceId serviceId, Wsdl wsdl);

}
