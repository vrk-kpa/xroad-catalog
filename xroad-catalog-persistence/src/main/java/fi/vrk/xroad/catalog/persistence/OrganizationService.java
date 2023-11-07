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
import fi.vrk.xroad.catalog.persistence.dto.LastOrganizationCollectionData;
import fi.vrk.xroad.catalog.persistence.entity.*;
import java.util.Optional;

/**
 * CRUD methods for organization related objects. no business logic (e.g. hash calculation),
 * just persistence-related logic.
 * Organization entities have time stamps created, updated and deleted.
 */
public interface OrganizationService {

    /**
     * Retrieves latest collection data
     * @return LastCollectionData
     */
    LastOrganizationCollectionData getLastOrganizationCollectionData();

    /**
     * @param businessCode Only interested in organizations with this businessCode value
     * @return Iterable of Organization entities
     */
    Iterable<Organization> getOrganizations(String businessCode);

    /**
     * Returns the full Organization object.
     * @param guid guid of an organization
     * @return Organization, if any
     */
    Optional<Organization> getOrganization(String guid);

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
}
