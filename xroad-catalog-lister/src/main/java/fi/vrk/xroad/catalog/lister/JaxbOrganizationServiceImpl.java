/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS) Copyright (c) 2016-2022 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

    private static final String ORGANIZATION = "Organization";
    private static final String ORGANIZATION_NAME = "OrganizationName";
    private static final String ORGANIZATION_DESCRIPTION = "OrganizationDescription";
    private static final String EMAIL = "Email";
    private static final String PHONE_NUMBER = "PhoneNumber";
    private static final String WEB_PAGE = "WebPage";
    private static final String ADDRESS = "Address";
    private static final String STREET_ADDRESS = "StreetAddress";
    private static final String STREET_ADDRESS_STREET = "Street";
    private static final String STREET_ADDRESS_POST_OFFICE = "StreetAddress PostOffice";
    private static final String STREET_ADDRESS_MUNICIPALITY = "StreetAddress Municipality";
    private static final String STREET_ADDRESS_MUNICIPALITY_NAME = "StreetAddress Municipality Name";
    private static final String STREET_ADDRESS_ADDITIONAL_INFORMATION = "StreetAddress AdditionalInformation";
    private static final String POST_OFFICE_BOX_ADDRESS = "PostOfficeBoxAddress";
    private static final String POST_OFFICE_BOX_ADDRESS_POST_OFFICE = "PostOfficeBoxAddress PostOffice";
    private static final String POST_OFFICE_BOX_ADDRESS_POST_OFFICE_BOX = "PostOfficeBoxAddress PostOfficeBox";
    private static final String POST_OFFICE_BOX_ADDRESS_MUNICIPALITY = "PostOfficeBoxAddress Municipality";
    private static final String POST_OFFICE_BOX_ADDRESS_MUNICIPALITY_NAME = "PostOfficeBoxAddress Municipality Name";
    private static final String POST_OFFICE_BOX_ADDRESS_ADDITIONAL_INFORMATION = "PostOfficeBoxAddress AdditionalInformation";

    @Autowired
    @Setter
    private OrganizationService organizationService;

    @Autowired
    @Setter
    private JaxbOrganizationConverter jaxbOrganizationConverter;

    @Autowired
    @Setter
    private JaxbServiceConverter jaxbServiceConverter;

    @Override
    public Iterable<Organization> getOrganizations(String businessCode) {
        Iterable<fi.vrk.xroad.catalog.persistence.entity.Organization> entities;
        entities = organizationService.getOrganizations(businessCode);

        return jaxbOrganizationConverter.convertOrganizations(entities);
    }

    @Override
    public Iterable<ChangedValue> getChangedOrganizationValues(String guid,
                                                               XMLGregorianCalendar startDateTime,
                                                               XMLGregorianCalendar endDateTime) {
        Optional<fi.vrk.xroad.catalog.persistence.entity.Organization> organization = organizationService.getOrganization(guid);
        if (organization.isPresent()) {
            return getAllChangedValuesForOrganization(organization.get(),
                    jaxbServiceConverter.toLocalDateTime(startDateTime),
                    jaxbServiceConverter.toLocalDateTime(endDateTime));
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
            changedValue.setName(ORGANIZATION);
            changedValueList.add(changedValue);
        }

        if (organizationNames.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName(ORGANIZATION_NAME);
            changedValueList.add(changedValue);
        }

        if (organizationDescriptions.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName(ORGANIZATION_DESCRIPTION);
            changedValueList.add(changedValue);
        }

        if (emails.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName(EMAIL);
            changedValueList.add(changedValue);
        }

        if (phoneNumbers.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName(PHONE_NUMBER);
            changedValueList.add(changedValue);
        }

        if (webPages.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName(WEB_PAGE);
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
            if (getChangedValueForAddress(address, startDateTime, endDateTime) != null) {
                changedValueList.add(getChangedValueForAddress(address, startDateTime, endDateTime));
            }

            Set<StreetAddress> streetAddresses = address.getAllStreetAddresses();
            streetAddresses.forEach(streetAddress -> {
                List<ChangedValue> allChangedValuesForStreetAddress = getAllChangedValuesForStreetAddress(streetAddress, startDateTime, endDateTime);
                for (ChangedValue changedValue : allChangedValuesForStreetAddress) {
                    changedValueList.add(changedValue);
                }
            });

            Set<PostOfficeBoxAddress> postOfficeBoxAddresses = address.getAllPostOfficeBoxAddresses();
            postOfficeBoxAddresses.forEach(postOfficeBoxAddress -> {
                List<ChangedValue> allChangedValuesForPostOfficeBoxAddress = getAllChangedValuesForPostOfficeBoxAddress(postOfficeBoxAddress,
                                                                                                                        startDateTime,
                                                                                                                        endDateTime);
                for (ChangedValue changedValue : allChangedValuesForPostOfficeBoxAddress) {
                    changedValueList.add(changedValue);
                }
            });
        });

        return changedValueList;
    }

    private ChangedValue getChangedValueForAddress(Address address,
                                                   LocalDateTime startDateTime,
                                                   LocalDateTime endDateTime) {
        LocalDateTime addressChange = address.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (addressChange.isAfter(startDateTime) && addressChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(ADDRESS);
        }
        return changedValue;
    }

    private List<ChangedValue> getAllChangedValuesForStreetAddress(StreetAddress streetAddress,
                                                                   LocalDateTime startDateTime,
                                                                   LocalDateTime endDateTime) {
        List<ChangedValue> changedValueList = new ArrayList<>();
        if (getChangedValueForStreetAddress(streetAddress, startDateTime, endDateTime) != null) {
            changedValueList.add(getChangedValueForStreetAddress(streetAddress, startDateTime, endDateTime));
        }

        Set<Street> streets = streetAddress.getAllStreets();
        streets.forEach(street -> {
            if (getChangedValueForStreetAddressStreet(street, startDateTime, endDateTime) != null) {
                changedValueList.add(getChangedValueForStreetAddressStreet(street, startDateTime, endDateTime));
            }
        });

        Set<StreetAddressPostOffice> postOffices = streetAddress.getAllPostOffices();
        postOffices.forEach(postOffice -> {
            if (getChangedValueForStreetAddressPostOffice(postOffice, startDateTime, endDateTime) != null) {
                changedValueList.add(getChangedValueForStreetAddressPostOffice(postOffice, startDateTime, endDateTime));
            }
        });

        Set<StreetAddressMunicipality> municipalities = streetAddress.getAllMunicipalities();
        municipalities.forEach(municipality -> {
            if (getChangedValueForStreetAddressMunicipality(municipality, startDateTime, endDateTime) != null) {
                changedValueList.add(getChangedValueForStreetAddressMunicipality(municipality, startDateTime, endDateTime));
            }

            Set<StreetAddressMunicipalityName> municipalityNames = municipality.getAllMunicipalityNames();
            municipalityNames.forEach(municipalityName -> {
                if (getChangedValueForStreetAddressMunicipalityName(municipalityName, startDateTime, endDateTime) != null) {
                    changedValueList.add(getChangedValueForStreetAddressMunicipalityName(municipalityName, startDateTime, endDateTime));
                }
            });
        });

        Set<StreetAddressAdditionalInformation> additionalInformationList = streetAddress.getAllAdditionalInformation();
        additionalInformationList.forEach(additionalInformation -> {
            if (getChangedValueForStreetAddressAdditionalInformation(additionalInformation, startDateTime, endDateTime) != null) {
                changedValueList.add(getChangedValueForStreetAddressAdditionalInformation(additionalInformation, startDateTime, endDateTime));
            }
        });

        return changedValueList;
    }

    private ChangedValue getChangedValueForStreetAddress(StreetAddress address,
                                                         LocalDateTime startDateTime,
                                                         LocalDateTime endDateTime) {
        LocalDateTime streetAddressChange = address.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (streetAddressChange.isAfter(startDateTime) && streetAddressChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(STREET_ADDRESS);
        }
        return changedValue;
    }

    private ChangedValue getChangedValueForStreetAddressStreet(Street street,
                                                               LocalDateTime startDateTime,
                                                               LocalDateTime endDateTime) {
        LocalDateTime streetChange = street.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (streetChange.isAfter(startDateTime) && streetChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(STREET_ADDRESS_STREET);
        }
        return changedValue;
    }

    private ChangedValue getChangedValueForStreetAddressPostOffice(StreetAddressPostOffice postOffice,
                                                                   LocalDateTime startDateTime,
                                                                   LocalDateTime endDateTime) {
        LocalDateTime streetAddressPostOfficeChange = postOffice.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (streetAddressPostOfficeChange.isAfter(startDateTime) && streetAddressPostOfficeChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(STREET_ADDRESS_POST_OFFICE);
        }
        return changedValue;
    }

    private ChangedValue getChangedValueForStreetAddressMunicipality(StreetAddressMunicipality municipality,
                                                                     LocalDateTime startDateTime,
                                                                     LocalDateTime endDateTime) {
        LocalDateTime streetAddressMunicipalityChange = municipality.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (streetAddressMunicipalityChange.isAfter(startDateTime) && streetAddressMunicipalityChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(STREET_ADDRESS_MUNICIPALITY);
        }
        return changedValue;
    }

    private ChangedValue getChangedValueForStreetAddressMunicipalityName(StreetAddressMunicipalityName municipalityName,
                                                                         LocalDateTime startDateTime,
                                                                         LocalDateTime endDateTime) {
        LocalDateTime streetAddressMunicipalityNameChange = municipalityName.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (streetAddressMunicipalityNameChange.isAfter(startDateTime) && streetAddressMunicipalityNameChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(STREET_ADDRESS_MUNICIPALITY_NAME);
        }
        return changedValue;
    }

    private ChangedValue getChangedValueForStreetAddressAdditionalInformation(StreetAddressAdditionalInformation additionalInformation,
                                                                              LocalDateTime startDateTime,
                                                                              LocalDateTime endDateTime) {
        LocalDateTime streetAddressAdditionalInfoChange = additionalInformation.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (streetAddressAdditionalInfoChange.isAfter(startDateTime) && streetAddressAdditionalInfoChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(STREET_ADDRESS_ADDITIONAL_INFORMATION);
        }
        return changedValue;
    }

    private List<ChangedValue> getAllChangedValuesForPostOfficeBoxAddress(PostOfficeBoxAddress postOfficeBoxAddress,
                                                                          LocalDateTime startDateTime,
                                                                          LocalDateTime endDateTime) {
        List<ChangedValue> changedValueList = new ArrayList<>();
        if (getChangedValueForPostOfficeBoxAddress(postOfficeBoxAddress, startDateTime, endDateTime) != null) {
            changedValueList.add(getChangedValueForPostOfficeBoxAddress(postOfficeBoxAddress, startDateTime, endDateTime));
        }

        Set<PostOffice> postOffices = postOfficeBoxAddress.getAllPostOffices();
        postOffices.forEach(postOffice -> {
            if (getChangedValueForPostOfficeBoxAddressPostOffice(postOffice, startDateTime, endDateTime) != null) {
                changedValueList.add(getChangedValueForPostOfficeBoxAddressPostOffice(postOffice, startDateTime, endDateTime));
            }
        });

        Set<PostOfficeBox> postOfficeBoxes = postOfficeBoxAddress.getAllPostOfficeBoxes();
        postOfficeBoxes.forEach(postOfficeBox -> {
            if (getChangedValueForPostOfficeBoxAddressPostOfficeBox(postOfficeBox, startDateTime, endDateTime) != null) {
                changedValueList.add(getChangedValueForPostOfficeBoxAddressPostOfficeBox(postOfficeBox, startDateTime, endDateTime));
            }
        });

        Set<PostOfficeBoxAddressAdditionalInformation> addressAdditionalInformationList = postOfficeBoxAddress.getAllAdditionalInformation();
        addressAdditionalInformationList.forEach(additionalInformation -> {
            if (getChangedValueForPostOfficeBoxAddressAdditionalInformation(additionalInformation, startDateTime, endDateTime) != null) {
                changedValueList.add(getChangedValueForPostOfficeBoxAddressAdditionalInformation(additionalInformation, startDateTime, endDateTime));
            }
        });

        Set<PostOfficeBoxAddressMunicipality> municipalities = postOfficeBoxAddress.getAllMunicipalities();
        municipalities.forEach(municipality -> {
            if (getChangedValueForPostOfficeBoxAddressMunicipality(municipality, startDateTime, endDateTime) != null) {
                changedValueList.add(getChangedValueForPostOfficeBoxAddressMunicipality(municipality, startDateTime, endDateTime));
            }

            Set<PostOfficeBoxAddressMunicipalityName> municipalityNames = municipality.getAllMunicipalityNames();
            municipalityNames.forEach(municipalityName -> {
                if (getChangedValueForPostOfficeBoxAddressMunicipalityName(municipalityName, startDateTime, endDateTime) != null) {
                    changedValueList.add(getChangedValueForPostOfficeBoxAddressMunicipalityName(municipalityName, startDateTime, endDateTime));
                }
            });
        });
        return changedValueList;
    }

    private ChangedValue getChangedValueForPostOfficeBoxAddress(PostOfficeBoxAddress postOfficeBoxAddress,
                                                                LocalDateTime startDateTime,
                                                                LocalDateTime endDateTime) {
        LocalDateTime postOfficeBoxAddressChange = postOfficeBoxAddress.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (postOfficeBoxAddressChange.isAfter(startDateTime) && postOfficeBoxAddressChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(POST_OFFICE_BOX_ADDRESS);
        }
        return changedValue;
    }

    private ChangedValue getChangedValueForPostOfficeBoxAddressPostOffice(PostOffice postOffice,
                                                                          LocalDateTime startDateTime,
                                                                          LocalDateTime endDateTime) {
        LocalDateTime postOfficeChange = postOffice.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (postOfficeChange.isAfter(startDateTime) && postOfficeChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(POST_OFFICE_BOX_ADDRESS_POST_OFFICE);
        }
        return changedValue;
    }

    private ChangedValue getChangedValueForPostOfficeBoxAddressPostOfficeBox(PostOfficeBox postOfficeBox,
                                                                             LocalDateTime startDateTime,
                                                                             LocalDateTime endDateTime) {
        LocalDateTime postOfficeBoxChange = postOfficeBox.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (postOfficeBoxChange.isAfter(startDateTime) && postOfficeBoxChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(POST_OFFICE_BOX_ADDRESS_POST_OFFICE_BOX);
        }
        return changedValue;
    }

    private ChangedValue getChangedValueForPostOfficeBoxAddressAdditionalInformation(PostOfficeBoxAddressAdditionalInformation additionalInformation,
                                                                                     LocalDateTime startDateTime,
                                                                                     LocalDateTime endDateTime) {
        LocalDateTime postOfficeBoxAddressAdditionalInfoChange = additionalInformation.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (postOfficeBoxAddressAdditionalInfoChange.isAfter(startDateTime) &&
                postOfficeBoxAddressAdditionalInfoChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(POST_OFFICE_BOX_ADDRESS_ADDITIONAL_INFORMATION);
        }
        return changedValue;
    }

    private ChangedValue getChangedValueForPostOfficeBoxAddressMunicipality(PostOfficeBoxAddressMunicipality municipality,
                                                                            LocalDateTime startDateTime,
                                                                            LocalDateTime endDateTime) {
        LocalDateTime postOfficeBoxAddressMunicipalityChange = municipality.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (postOfficeBoxAddressMunicipalityChange.isAfter(startDateTime) &&
                postOfficeBoxAddressMunicipalityChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(POST_OFFICE_BOX_ADDRESS_MUNICIPALITY);
        }
        return changedValue;
    }

    private ChangedValue getChangedValueForPostOfficeBoxAddressMunicipalityName(PostOfficeBoxAddressMunicipalityName municipalityName,
                                                                                LocalDateTime startDateTime,
                                                                                LocalDateTime endDateTime) {
        LocalDateTime postOfficeBoxAddressMunicipalityNameChange = municipalityName.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (postOfficeBoxAddressMunicipalityNameChange.isAfter(startDateTime) &&
                postOfficeBoxAddressMunicipalityNameChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(POST_OFFICE_BOX_ADDRESS_MUNICIPALITY_NAME);
        }
        return changedValue;
    }
}
