/**
 * The MIT License
 * Copyright (c) 2022, Population Register Centre (VRK)
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
package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.OrganizationService;
import fi.vrk.xroad.catalog.persistence.entity.*;
import fi.vrk.xroad.xroad_catalog_lister.ChangedValue;
import fi.vrk.xroad.xroad_catalog_lister.Organization;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Slf4j
public class JaxbOrganizationServiceImpl implements JaxbOrganizationService {

    @Autowired
    @Setter
    private OrganizationService organizationService;

    @Autowired
    @Setter
    private JaxbConverter jaxbConverter;

    @Override
    public Iterable<Organization> getOrganizations(String businessCode) {
        Iterable<fi.vrk.xroad.catalog.persistence.entity.Organization> entities;
        entities = organizationService.getOrganizations(businessCode);

        return jaxbConverter.convertOrganizations(entities);
    }

    @Override
    public Iterable<ChangedValue> getChangedOrganizationValues(String guid,
                                                               XMLGregorianCalendar startDateTime,
                                                               XMLGregorianCalendar endDateTime) {
        Optional<fi.vrk.xroad.catalog.persistence.entity.Organization> organization = organizationService.getOrganization(guid);
        if (organization.isPresent()) {
            return getAllChangedValuesForOrganization(organization.get(),
                    jaxbConverter.toLocalDateTime(startDateTime),
                    jaxbConverter.toLocalDateTime(endDateTime));
        } else {
            throw new CatalogListerRuntimeException("Organization with guid " + guid + " not found");
        }
    }

    private Iterable<ChangedValue> getAllChangedValuesForOrganization(fi.vrk.xroad.catalog.persistence.entity.Organization organization,
                                                                      LocalDateTime startDateTime,
                                                                      LocalDateTime endDateTime) {
        List<ChangedValue> changedValueList = new ArrayList<>();
        Set<OrganizationName> organizationNames = organization.getAllOrganizationNames();
        Set<OrganizationDescription> organizationDescriptions = organization.getAllOrganizationDescriptions();
        Set<Email> emails = organization.getAllEmails();
        Set<PhoneNumber> phoneNumbers = organization.getAllPhoneNumbers();
        Set<WebPage> webPages = organization.getAllWebPages();
        Set<Address> addresses = organization.getAllAddresses();

        if (organization.getStatusInfo().getChanged().isAfter(startDateTime) &&
                organization.getStatusInfo().getChanged().isBefore(endDateTime)) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("Organization");
            changedValueList.add(changedValue);
        }

        if (organizationNames.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("OrganizationName");
            changedValueList.add(changedValue);
        }

        if (organizationDescriptions.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("OrganizationDescription");
            changedValueList.add(changedValue);
        }

        if (emails.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("Email");
            changedValueList.add(changedValue);
        }

        if (phoneNumbers.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("PhoneNumber");
            changedValueList.add(changedValue);
        }

        if (webPages.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("WebPage");
            changedValueList.add(changedValue);
        }

        changedValueList.addAll(getAllChangedValuesForAddress(addresses, startDateTime, endDateTime));

        return changedValueList;
    }

    private Collection<ChangedValue> getAllChangedValuesForAddress(Set<Address> addresses,
                                                                   LocalDateTime startDateTime,
                                                                   LocalDateTime endDateTime) {
        List<ChangedValue> changedValueList = new ArrayList<>();
        addresses.forEach(address -> {
            LocalDateTime addressChange = address.getStatusInfo().getChanged();
            if (addressChange.isAfter(startDateTime) && addressChange.isBefore(endDateTime)) {
                ChangedValue changedValue = new ChangedValue();
                changedValue.setName("Address");
                changedValueList.add(changedValue);
            }

            Set<StreetAddress> streetAddresses = address.getAllStreetAddresses();
            streetAddresses.forEach(streetAddress -> {
                LocalDateTime streetAddressChange = streetAddress.getStatusInfo().getChanged();
                if (streetAddressChange.isAfter(startDateTime) && streetAddressChange.isBefore(endDateTime)) {
                    ChangedValue changedValue = new ChangedValue();
                    changedValue.setName("StreetAddress");
                    changedValueList.add(changedValue);
                }

                Set<Street> streets = streetAddress.getAllStreets();
                streets.forEach(street -> {
                    LocalDateTime streetChange = street.getStatusInfo().getChanged();
                    if (streetChange.isAfter(startDateTime) && streetChange.isBefore(endDateTime)) {
                        ChangedValue changedValue = new ChangedValue();
                        changedValue.setName("Street");
                        changedValueList.add(changedValue);
                    }
                });

                Set<StreetAddressPostOffice> postOffices = streetAddress.getAllPostOffices();
                postOffices.forEach(postOffice -> {
                    LocalDateTime streetAddressPostOfficeChange = postOffice.getStatusInfo().getChanged();
                    if (streetAddressPostOfficeChange.isAfter(startDateTime) && streetAddressPostOfficeChange.isBefore(endDateTime)) {
                        ChangedValue changedValue = new ChangedValue();
                        changedValue.setName("StreetAddress PostOffice");
                        changedValueList.add(changedValue);
                    }
                });

                Set<StreetAddressMunicipality> municipalities = streetAddress.getAllMunicipalities();
                municipalities.forEach(municipality -> {
                    LocalDateTime streetAddressMunicipalityChange = municipality.getStatusInfo().getChanged();
                    if (streetAddressMunicipalityChange.isAfter(startDateTime) && streetAddressMunicipalityChange.isBefore(endDateTime)) {
                        ChangedValue changedValue = new ChangedValue();
                        changedValue.setName("StreetAddress Municipality");
                        changedValueList.add(changedValue);
                    }

                    Set<StreetAddressMunicipalityName> municipalityNames = municipality.getAllMunicipalityNames();
                    municipalityNames.forEach(municipalityName -> {
                        LocalDateTime streetAddressMunicipalityNameChange = municipalityName.getStatusInfo().getChanged();
                        if (streetAddressMunicipalityNameChange.isAfter(startDateTime) && streetAddressMunicipalityNameChange.isBefore(endDateTime)) {
                            ChangedValue changedValue = new ChangedValue();
                            changedValue.setName("StreetAddress Municipality Name");
                            changedValueList.add(changedValue);
                        }
                    });
                });

                Set<StreetAddressAdditionalInformation> additionalInformationList = streetAddress.getAllAdditionalInformation();
                additionalInformationList.forEach(additionalInformation -> {
                    LocalDateTime streetAddressAdditionalInfoChange = additionalInformation.getStatusInfo().getChanged();
                    if (streetAddressAdditionalInfoChange.isAfter(startDateTime) && streetAddressAdditionalInfoChange.isBefore(endDateTime)) {
                        ChangedValue changedValue = new ChangedValue();
                        changedValue.setName("StreetAddress AdditionalInformation");
                        changedValueList.add(changedValue);
                    }
                });

            });

            Set<PostOfficeBoxAddress> postOfficeBoxAddresses = address.getAllPostOfficeBoxAddresses();
            postOfficeBoxAddresses.forEach(postOfficeBoxAddress -> {
                LocalDateTime postOfficeBoxAddressChange = postOfficeBoxAddress.getStatusInfo().getChanged();
                if (postOfficeBoxAddressChange.isAfter(startDateTime) && postOfficeBoxAddressChange.isBefore(endDateTime)) {
                    ChangedValue changedValue = new ChangedValue();
                    changedValue.setName("PostOfficeBoxAddress");
                    changedValueList.add(changedValue);
                }

                Set<PostOffice> postOffices = postOfficeBoxAddress.getAllPostOffices();
                postOffices.forEach(postOffice -> {
                    LocalDateTime postOfficeChange = postOffice.getStatusInfo().getChanged();
                    if (postOfficeChange.isAfter(startDateTime) && postOfficeChange.isBefore(endDateTime)) {
                        ChangedValue changedValue = new ChangedValue();
                        changedValue.setName("PostOfficeBoxAddress PostOffice");
                        changedValueList.add(changedValue);
                    }
                });

                Set<PostOfficeBox> postOfficeBoxes = postOfficeBoxAddress.getAllPostOfficeBoxes();
                postOfficeBoxes.forEach(postOfficeBox -> {
                    LocalDateTime postOfficeBoxChange = postOfficeBox.getStatusInfo().getChanged();
                    if (postOfficeBoxChange.isAfter(startDateTime) && postOfficeBoxChange.isBefore(endDateTime)) {
                        ChangedValue changedValue = new ChangedValue();
                        changedValue.setName("PostOfficeBoxAddress PostOfficeBox");
                        changedValueList.add(changedValue);
                    }
                });

                Set<PostOfficeBoxAddressAdditionalInformation> addressAdditionalInformationList = postOfficeBoxAddress.getAllAdditionalInformation();
                addressAdditionalInformationList.forEach(additionalInformation -> {
                    LocalDateTime postOfficeBoxAddressAdditionalInfoChange = additionalInformation.getStatusInfo().getChanged();
                    if (postOfficeBoxAddressAdditionalInfoChange.isAfter(startDateTime) &&
                            postOfficeBoxAddressAdditionalInfoChange.isBefore(endDateTime)) {
                        ChangedValue changedValue = new ChangedValue();
                        changedValue.setName("PostOfficeBoxAddress AdditionalInformation");
                        changedValueList.add(changedValue);
                    }
                });

                Set<PostOfficeBoxAddressMunicipality> municipalities = postOfficeBoxAddress.getAllMunicipalities();
                municipalities.forEach(municipality -> {
                    LocalDateTime postOfficeBoxAddressMunicipalityChange = municipality.getStatusInfo().getChanged();
                    if (postOfficeBoxAddressMunicipalityChange.isAfter(startDateTime) &&
                            postOfficeBoxAddressMunicipalityChange.isBefore(endDateTime)) {
                        ChangedValue changedValue = new ChangedValue();
                        changedValue.setName("PostOfficeBoxAddress Municipality");
                        changedValueList.add(changedValue);
                    }

                    Set<PostOfficeBoxAddressMunicipalityName> municipalityNames = municipality.getAllMunicipalityNames();
                    municipalityNames.forEach(municipalityName -> {
                        LocalDateTime postOfficeBoxAddressMunicipalityNameChange = municipalityName.getStatusInfo().getChanged();
                        if (postOfficeBoxAddressMunicipalityNameChange.isAfter(startDateTime) &&
                                postOfficeBoxAddressMunicipalityNameChange.isBefore(endDateTime)) {
                            ChangedValue changedValue = new ChangedValue();
                            changedValue.setName("PostOfficeBoxAddress Municipality Name");
                            changedValueList.add(changedValue);
                        }
                    });
                });
            });
        });

        return changedValueList;
    }
}
