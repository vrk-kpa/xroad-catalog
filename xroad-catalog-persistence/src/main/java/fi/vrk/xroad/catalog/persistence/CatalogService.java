package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;

import java.util.Collection;
import java.util.Date;

/**
 * CRUD methods for catalog objects. no business logic (e.g. hash calculation),
 * just persistence-related logic.
 * Catalog entities (member, subsystem, service, wsdl) have time stamps created, updated and deleted.
 * They are used so that "updated" is changed always when entity is updated in any way, including
 * creation and deletion. This is important since getMembers(Date updatedSince) only checks
 * updated-field and ignores created & deleted.
 */
public interface CatalogService {

    // TODO: make it NOT fetch wsdl yet

    /**
     * Gets all members, regardless of when they were updated.
     * Fetches all data from graph member->subsystem->service->wsdl
     * @return
     */
    Iterable<Member> getMembers();

    // TODO: make it NOT fetch wsdl yet

    /**
     * Gets all members that have been changed (created) after given time.
     * Change is determined by field "updated".
     * Fetches all data from graph member->subsystem->service->wsdl.
     * @param updatedAfter
     * @return
     */
    Iterable<Member> getMembers(Date updatedAfter);

    /**
     * Mainly for testing use
     * @return
     */
    Iterable<Member> getSubsystems();

    /**
     * Returns the full Wsdl object.
     * @return
     */
    Wsdl getWsdl(String externalId);

    // TODO: search for member based on unique fields (instance, class, code)
    // TODO: indexes for those

    /**
     * Stores given members and subsystems. These represent the full dataset of both items
     * - items not included in the parameters are marked as removed, if the existed previously.
     * "Full service": updates all status timestamps (created/updated/removed) automatically,
     * and knows whether to update existing item or create a new one.
     *
     * Does not touch the child items (service, wsdl). If creating new subsystems, the
     * service collection will be empty
     */
    // TODO: document, needs subsystem collections in place
    void save(Collection<Member> members);

    /**
     * Stores services for given subsystem.
     * @param subsystem identifier info for subsystem
     * @param service services
     */
    void save(Subsystem subsystem, Collection<Service> service);

    Wsdl saveWsdl(long serviceId, Wsdl wsdl);

}
