package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.xroad_catalog_lister.Member;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Service which talks in JAXB objects.
 * Responsible for correctly mapping the trees of JAXB objects / JPA entities.
 */
public interface JaxbCatalogService {

    /**
     * Returns all members that have had some part of member->substem->service->wsdl graph
     * changed after <code>changedAfter</code>. If changedAfter = null, returns all members
     * altogether.
     *
     * All substem->service->wsdl items are always returned, whether they are removed items
     * or not, and whether they have been updated since changedAfter or not.
     *
     * @return
     */
    Iterable<Member> getAllMembers(XMLGregorianCalendar changedAfter) throws DatatypeConfigurationException;
}
