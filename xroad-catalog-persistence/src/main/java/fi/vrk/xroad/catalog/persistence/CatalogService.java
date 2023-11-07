/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2016-2023 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.dto.LastCollectionData;
import fi.vrk.xroad.catalog.persistence.dto.DistinctServiceStatistics;
import fi.vrk.xroad.catalog.persistence.dto.MemberDataList;
import fi.vrk.xroad.catalog.persistence.dto.ServiceStatistics;
import fi.vrk.xroad.catalog.persistence.dto.XRoadData;
import fi.vrk.xroad.catalog.persistence.entity.*;
import org.springframework.data.domain.Page;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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
     * @return Iterable of Member entities
     */
    Iterable<Member> getActiveMembers();

    /**
     * Same as {@link #getActiveMembers()} except that returns also
     * removed items
     * @return Iterable of Member entities
     */
    Iterable<Member> getAllMembers();

    /**
     * Gets all non-removed members that have been changed (created) after given time.
     * Change is determined by field "changed".
     * Fetches all data from graph member->subsystem->service->wsdl.
     * @param startDateTime Only interested in member after this
     * @param endDateTime Only interested in member before this
     * @return Iterable of Member entities
     */
    Iterable<Member> getActiveMembers(LocalDateTime startDateTime, LocalDateTime endDateTime);

    /**
     * Same as {@link #getActiveMembers(LocalDateTime, LocalDateTime)} except that returns also removed items
     * @param startDateTime Only interested in member after this
     * @param endDateTime Only interested in member before this
     * @return Iterable of Member entities
     */
    Iterable<Member> getAllMembers(LocalDateTime startDateTime, LocalDateTime endDateTime);

    /**
     * Returns full Member object
     * @param xRoadInstance name of the instance
     * @param memberClass member class
     * @param memberCode member  code
     * @return Member entity
     */
    Member getMember(String xRoadInstance, String memberClass, String memberCode);

    /**
     * Returns the full Wsdl object. Only returns active ones, removed are not found.
     * @param externalId id of a Wsdl
     * @return Wsdl, if any, null if not found
     * @throws RuntimeException if multiple matches found.
     */
    Wsdl getWsdl(String externalId);

    /**
     * Returns the full OpenApi object. Only returns active ones, removed are not found.
     * @param externalId id of an OpenAPI
     * @return OpenApi, if any, null if not found
     * @throws RuntimeException if multiple matches found.
     */
    OpenApi getOpenApi(String externalId);

    /**
     * Returns the full Rest object. Only returns active ones, removed are not found.
     * @param Service a service object
     * @return Rest, if any, null if not found
     * @throws RuntimeException if multiple matches found.
     */
    Rest getRest(Service service);

    /**
     * Returns the full Service object. Only returns active ones, removed are not found.
     * @param xRoadInstance X-Road instance identifier
     * @param memberClass X-Road member class
     * @param memberCode X-Road member code
     * @param serviceCode X-Road service code
     * @param subsystemCode X-Road subsystem code
     * @param serviceVersion X-Road service version
     * @return Service, if any, null if not found
     * @throws RuntimeException if multiple matches found.
     */
    Service getService(String xRoadInstance,
                       String memberClass,
                       String memberCode,
                       String serviceCode,
                       String subsystemCode,
                       String serviceVersion);

    /**
     * Returns List of full Service objects. Only returns active ones, removed are not found.
     * @param xRoadInstance X-Road instance identifier
     * @param memberClass X-Road member class
     * @param memberCode X-Road member code
     * @param serviceCode X-Road service code
     * @param subsystemCode X-Road subsystem code
     * @return List of Service objects, empty list if not found
     */
    List<Service> getServices(String xRoadInstance,
                              String memberClass,
                              String memberCode,
                              String subsystemCode,
                              String serviceCode);

    /**
     * Returns a list of error logs with pagination
     * @param xRoadData X-Road instance identifier, member class, member code and subsystem code
     * @param page page number of pageable result of error logs
     * @param limit number of results per page
     * @param startDate creation date from
     * @param endDate creation date to
     * @return Page of ErrorLog, null if not found
     */
    Page<ErrorLog> getErrors(XRoadData xRoadData,
                             int page,
                             int limit,
                             LocalDateTime startDate,
                             LocalDateTime endDate);

    /**
     * Returns a list of service statistics
     * @param startDateTime creation date from
     * @param endDateTime creation date to
     * @return List of ServiceStatistics, null if not found
     */
    List<ServiceStatistics> getServiceStatistics(LocalDateTime startDateTime, LocalDateTime endDateTime);

    /**
     * Returns a list of distinct service statistics
     * @return List of DistinctServiceStatistics, null if not found
     */
    List<DistinctServiceStatistics> getDistinctServiceStatistics(LocalDateTime startDateTime, LocalDateTime endDateTime);

    /**
     * Returns a list of memberDataLists
     * @param startDateTime creation date from
     * @param endDateTime creation date to
     * @return List of memberDataLists, null if not found
     */
    List<MemberDataList> getMemberData(LocalDateTime startDateTime, LocalDateTime endDateTime);

    /**
     * Returns the full ErrorLog object.
     * @param startDateTime creation date from
     * @param endDateTime creation date to
     * @return ErrorLog, if any, null if not found
     * @throws RuntimeException if multiple matches found.
     */
    Iterable<ErrorLog> getErrorLog(LocalDateTime startDateTime, LocalDateTime endDateTime);

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
     * Saves given wsdl data. The wsdl can either be a new one, or an update to an existing one.
     * Updates "changed" field based on whether data is different compared to last time.
     * @param subsystemId identifier of the subsystem
     * @param serviceId identifier of the service
     * @param wsdl the actual wsdl
     */
    void saveWsdl(SubsystemId subsystemId, ServiceId serviceId, String wsdl);

    /**
     * Saves given openApi data. The openApi can either be a new one, or an update to an existing one.
     * Updates "changed" field based on whether data is different compared to last time.
     * @param subsystemId identifier of the subsystem
     * @param serviceId identifier of the service
     * @param openApi the actual openApi
     */
    void saveOpenApi(SubsystemId subsystemId, ServiceId serviceId, String openApi);

    /**
     * Saves given rest data. The rest can either be a new one, or an update to an existing one.
     * Updates "changed" field based on whether data is different compared to last time.
     * @param subsystemId identifier of the subsystem
     * @param serviceId identifier of the service
     * @param rest the actual rest
     */
    void saveRest(SubsystemId subsystemId, ServiceId serviceId, String rest);

    /**
     * Saves given rest data. The rest can either be a new one, or an update to an existing one.
     * Updates "changed" field based on whether data is different compared to last time.
     * @param subsystemId identifier of the subsystem
     * @param serviceId identifier of the service
     * @param method method info
     * @param path path info
     */
    void saveEndpoint(SubsystemId subsystemId, ServiceId serviceId, String method, String path);

    /**
     * Marks all entries in the Endpoints table as removed
     * so that when new endpoints are being fetched, those will be marked as non-removed
     * and when some endpoints are missing in the future, the ones still present in the table
     * will remain as removed
     * @param subsystemId identifier of the subsystem
     * @param serviceId identifier of the service
     */
    void prepareEndpoints(SubsystemId subsystemId, ServiceId serviceId);

    /**
     * Checks if database is alive
     * @return databaseConnection
     */
    Boolean checkDatabaseConnection();

    /**
     * Retrieves latest collection data
     * @return LastCollectionData
     */
    LastCollectionData getLastCollectionData();

    /**
     * Saves given errorLog data.
     * @param errorLog the actual errorLog
     @return error log
     */
    ErrorLog saveErrorLog(ErrorLog errorLog);

    /**
     * Deletes old log entries
     * @param daysBefore older than daysBefore
     */
    void deleteOldErrorLogEntries(Integer daysBefore);

}
