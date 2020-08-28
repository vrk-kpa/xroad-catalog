/**
 * The MIT License
 * Copyright (c) 2016, Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.*;

import java.time.LocalDateTime;
import java.util.Collection;
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
     * @param changedAfter Only interested in member after this
     * @return Iterable of Member entities
     */
    Iterable<Member> getActiveMembers(LocalDateTime changedAfter);

    /**
     * Same as {@link #getActiveMembers(LocalDateTime)} except that returns also removed items
     * @param changedAfter Only interested in member after this
     * @return Iterable of Member entities
     */
    Iterable<Member> getAllMembers(LocalDateTime changedAfter);

    /**
     * @param businessCode Only interested in organizations with this businessCode value
     * @return Iterable of Organization entities
     */
    Iterable<Organization> getOrganizations(String businessCode);

    /**
     * @param businessId Only interested in companies with this businessId value
     * @return Iterable of Company entities
     */
    Iterable<Company> getCompanies(String businessId);

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
     * @return Wsdl, if any, null if not found
     * @throws RuntimeException if multiple matches found.
     */
    Wsdl getWsdl(String externalId);

    /**
     * Returns the full OpenApi object. Only returns active ones, removed are not found.
     * @return Wsdl, if any, null if not found
     * @throws RuntimeException if multiple matches found.
     */
    OpenApi getOpenApi(String externalId);

    /**
     * Returns the full Service object. Only returns active ones, removed are not found.
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
     * Returns the full Organization object.
     * @return Organization, if any
     */
    Optional<Organization> getOrganization(String guid);

    /**
     * Returns the full ErrorLog object.
     * @return ErrorLog, if any, null if not found
     * @throws RuntimeException if multiple matches found.
     */
    Iterable<ErrorLog> getErrorLog(LocalDateTime since);

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
     * Saves given organization data. The organization can either be a new one, or an update to an existing one.
     * Updates "changed" field based on whether data is different compared to last time.
     * @return saved organization
     * @param organization the actual organization
     */
    Organization saveOrganization(Organization organization);

    /**
     * Saves given organizationName data.
     * @param organizationName the organizationName
     */
    void saveOrganizationName(OrganizationName organizationName);

    /**
     * Saves given organizationDescription data.
     * @param organizationDescription the organizationDescription
     */
    void saveOrganizationDescription(OrganizationDescription organizationDescription);

    /**
     * Saves given email data.
     * @param email the actual email
     */
    void saveEmail(Email email);

    /**
     * Saves given phoneNumber data.
     * @param phoneNumber the actual phoneNumber
     */
    void savePhoneNumber(PhoneNumber phoneNumber);

    /**
     * Saves given webPage data.
     * @param webPage the actual webPage
     */
    void saveWebPage(WebPage webPage);

    /**
     * Saves given address data.
     * @return saved Address
     * @param address the actual address
     */
    Address saveAddress(Address address);

    /**
     * Saves given StreetAddress data.
     * @return saved StreetAddress
     * @param streetAddress the actual StreetAddress
     */
    StreetAddress saveStreetAddress(StreetAddress streetAddress);

    /**
     * Saves given PostOfficeBoxAddress data.
     * @return saved PostOfficeBoxAddress
     * @param postOfficeBoxAddress the actual PostOfficeBoxAddress
     */
    PostOfficeBoxAddress savePostOfficeBoxAddress(PostOfficeBoxAddress postOfficeBoxAddress);

    /**
     * Saves given StreetAddressMunicipality data.
     * @return saved StreetAddressMunicipality
     * @param streetAddressMunicipality the actual StreetAddressMunicipality
     */
    StreetAddressMunicipality saveStreetAddressMunicipality(StreetAddressMunicipality streetAddressMunicipality);

    /**
     * Saves given PostOfficeBoxAddressMunicipality data.
     * @return saved PostOfficeBoxAddressMunicipality
     * @param postOfficeBoxAddressMunicipality the actual PostOfficeBoxAddressMunicipality
     */
    PostOfficeBoxAddressMunicipality savePostOfficeBoxAddressMunicipality(PostOfficeBoxAddressMunicipality postOfficeBoxAddressMunicipality);


    /**
     * Saves given StreetAddressMunicipalityName data.
     * @return saved StreetAddressMunicipalityName
     * @param streetAddressMunicipalityName the actual StreetAddressMunicipalityName
     */
    StreetAddressMunicipalityName saveStreetAddressMunicipalityName(StreetAddressMunicipalityName streetAddressMunicipalityName);

    /**
     * Saves given PostOfficeBoxAddressMunicipalityName data.
     * @return saved PostOfficeBoxAddressMunicipalityName
     * @param postOfficeBoxAddressMunicipalityName the actual PostOfficeBoxAddressMunicipalityName
     */
    PostOfficeBoxAddressMunicipalityName savePostOfficeBoxAddressMunicipalityName(
            PostOfficeBoxAddressMunicipalityName postOfficeBoxAddressMunicipalityName);

    /**
     * Saves given StreetAddressAdditionalInformation data.
     * @return saved StreetAddressAdditionalInformation
     * @param streetAddressAdditionalInformation the actual StreetAddressAdditionalInformation
     */
    StreetAddressAdditionalInformation saveStreetAddressAdditionalInformation(
            StreetAddressAdditionalInformation streetAddressAdditionalInformation);

    /**
     * Saves given PostOfficeBoxAddressAdditionalInformation data.
     * @return saved PostOfficeBoxAddressAdditionalInformation
     * @param postOfficeBoxAddressAdditionalInformation the actual PostOfficeBoxAddressAdditionalInformation
     */
    PostOfficeBoxAddressAdditionalInformation savePostOfficeBoxAddressAdditionalInformation(
            PostOfficeBoxAddressAdditionalInformation postOfficeBoxAddressAdditionalInformation);

    /**
     * Saves given StreetAddressPostOffice data.
     * @return saved StreetAddressPostOffice
     * @param streetAddressPostOffice the actual StreetAddressPostOffice
     */
    StreetAddressPostOffice saveStreetAddressPostOffice(StreetAddressPostOffice streetAddressPostOffice);

    /**
     * Saves given PostOffice data.
     * @return saved PostOffice
     * @param postOffice the actual PostOffice
     */
    PostOffice savePostOffice(PostOffice postOffice);

    /**
     * Saves given PostOfficeBox data.
     * @return saved PostOfficeBox
     * @param postOfficeBox the actual PostOfficeBox
     */
    PostOfficeBox savePostOfficeBox(PostOfficeBox postOfficeBox);

    /**
     * Saves given Street data.
     * @return saved Street
     * @param street the actual Street
     */
    Street saveStreet(Street street);

    /**
     * Saves given company data. The company can either be a new one, or an update to an existing one.
     * Updates "changed" field based on whether data is different compared to last time.
     * @return saved company
     * @param company the actual company
     */
    Company saveCompany(Company company);

    /**
     * Saves given BusinessName data.
     * @param businessName the BusinessName
     */
    void saveBusinessName(BusinessName businessName);

    /**
     * Saves given BusinessAuxiliaryName data.
     * @param businessAuxiliaryName the BusinessAuxiliaryName
     */
    void saveBusinessAuxiliaryName(BusinessAuxiliaryName businessAuxiliaryName);

    /**
     * Saves given BusinessAddress data.
     * @param businessAddress the BusinessAddress
     */
    void saveBusinessAddress(BusinessAddress businessAddress);

    /**
     * Saves given BusinessIdChange data.
     * @param businessIdChange the BusinessIdChange
     */
    void saveBusinessIdChange(BusinessIdChange businessIdChange);

    /**
     * Saves given BusinessLine data.
     * @param businessLine the BusinessLine
     */
    void saveBusinessLine(BusinessLine businessLine);

    /**
     * Saves given CompanyForm data.
     * @param companyForm the CompanyForm
     */
    void saveCompanyForm(CompanyForm companyForm);

    /**
     * Saves given ContactDetail data.
     * @param contactDetail the ContactDetail
     */
    void saveContactDetail(ContactDetail contactDetail);

    /**
     * Saves given Language data.
     * @param language the Language
     */
    void saveLanguage(Language language);

    /**
     * Saves given Liquidation data.
     * @param liquidation the Liquidation
     */
    void saveLiquidation(Liquidation liquidation);

    /**
     * Saves given RegisteredEntry data.
     * @param registeredEntry the RegisteredEntry
     */
    void saveRegisteredEntry(RegisteredEntry registeredEntry);

    /**
     * Saves given RegisteredOffice data.
     * @param registeredOffice the RegisteredOffice
     */
    void saveRegisteredOffice(RegisteredOffice registeredOffice);

    /**
     * Saves given errorLog data.
     * @param errorLog the actual errorLog
     */
    ErrorLog saveErrorLog(ErrorLog errorLog);

    /**
     * Deletes old log entries
     * @param daysBefore older than daysBefore
     */
    void deleteOldErrorLogEntries(Integer daysBefore);

}
