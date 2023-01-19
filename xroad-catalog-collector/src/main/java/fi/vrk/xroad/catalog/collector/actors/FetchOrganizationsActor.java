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
package fi.vrk.xroad.catalog.collector.actors;

import fi.vrk.xroad.catalog.collector.util.OrganizationUtil;
import fi.vrk.xroad.catalog.collector.wsimport.ClientType;
import fi.vrk.xroad.catalog.persistence.OrganizationService;
import fi.vrk.xroad.catalog.persistence.entity.*;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Scope("prototype")
@Slf4j
public class FetchOrganizationsActor extends XRoadCatalogActor {

    @Value("${xroad-catalog.fetch-organizations-url}")
    private String fetchOrganizationsUrl;

    @Value("${xroad-catalog.max-organizations-per-request}")
    private Integer maxOrganizationsPerRequest;

    @Value("${xroad-catalog.fetch-organizations-limit}")
    private Integer fetchOrganizationsLimit;

    @Autowired
    protected CatalogService catalogService;

    @Autowired
    protected OrganizationService organizationService;

    @Override
    public void preStart() throws Exception {
        // This method is here just to override the method from the superclass
    }

    @Override
    protected boolean handleMessage(Object message) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if (message instanceof ClientType) {
            List<String> organizationIds = OrganizationUtil.getOrganizationIdsList((ClientType)message, fetchOrganizationsUrl,
                    fetchOrganizationsLimit, catalogService);
            int numberOfOrganizations = organizationIds.size();
            log.info("Fetched {} organization GUIDs from {}", numberOfOrganizations, fetchOrganizationsUrl);

            AtomicInteger elementCount = new AtomicInteger();
            AtomicInteger batchCount = new AtomicInteger();
            List<String> guidsList = new ArrayList<>();
            organizationIds.forEach(id -> {
                guidsList.add(id);
                elementCount.getAndIncrement();
                if (elementCount.get() % maxOrganizationsPerRequest == 0) {
                    batchCount.getAndIncrement();
                    saveBatch(OrganizationUtil.getDataByIds((ClientType) message, guidsList, fetchOrganizationsUrl, catalogService));
                    guidsList.clear();
                }
                if (elementCount.get() == organizationIds.size()) {
                    batchCount.getAndIncrement();
                    saveBatch(OrganizationUtil.getDataByIds((ClientType) message, guidsList, fetchOrganizationsUrl, catalogService));
                }
            });
            log.info("Saved data of {} organizations successfully", numberOfOrganizations);

            return true;
        } else {
            return false;
        }
    }

    private void saveBatch(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            Organization organization = OrganizationUtil.createOrganization(data.optJSONObject(i));
            JSONObject dataJson = data.optJSONObject(i);
            Organization savedOrganization = organizationService.saveOrganization(organization);
            saveOrganizationNames(dataJson, savedOrganization);
            saveOrganizationDescriptions(dataJson, savedOrganization);
            saveEmails(dataJson, savedOrganization);
            savePhoneNumbers(dataJson, savedOrganization);
            saveWebPages(dataJson, savedOrganization);
            saveAddresses(dataJson, savedOrganization);
        }
    }

    private void saveOrganizationNames(JSONObject data, Organization savedOrganization) {
        List<OrganizationName> organizationNames = OrganizationUtil.createNames(data
                .optJSONArray("organizationNames"));
        organizationNames.forEach(organizationName -> {
            organizationName.setOrganization(savedOrganization);
            organizationService.saveOrganizationName(organizationName);
        });
    }

    private void saveOrganizationDescriptions(JSONObject data, Organization savedOrganization) {
        List<OrganizationDescription> organizationDescriptions = OrganizationUtil.createDescriptions(data
                .optJSONArray("organizationDescriptions"));
        organizationDescriptions.forEach(organizationDescription -> {
            organizationDescription.setOrganization(savedOrganization);
            organizationService.saveOrganizationDescription(organizationDescription);
        });
    }

    private void saveEmails(JSONObject data, Organization savedOrganization) {
        List<Email> emails = OrganizationUtil.createEmails(data.optJSONArray("emails"));
        emails.forEach(email -> {
            email.setOrganization(savedOrganization);
            organizationService.saveEmail(email);
        });
    }

    private void savePhoneNumbers(JSONObject data, Organization savedOrganization) {
        List<PhoneNumber> phoneNumbers = OrganizationUtil.createPhoneNumbers(data.optJSONArray("phoneNumbers"));
        phoneNumbers.forEach(phone -> {
            phone.setOrganization(savedOrganization);
            organizationService.savePhoneNumber(phone);
        });
    }

    private void saveWebPages(JSONObject data, Organization savedOrganization) {
        List<WebPage> webPages = OrganizationUtil.createWebPages(data.optJSONArray("webPages"));
        webPages.forEach(webPage -> {
            webPage.setOrganization(savedOrganization);
            organizationService.saveWebPage(webPage);
        });
    }

    private void saveAddresses(JSONObject data, Organization savedOrganization) {
        List<Address> addresses = OrganizationUtil.createAddresses(data.optJSONArray("addresses"));
        JSONArray addressesListJson = data.optJSONArray("addresses");
        addresses.forEach(address -> {
            address.setOrganization(savedOrganization);
            Address savedAddress = organizationService.saveAddress(address);
            saveAddressDetails(addressesListJson, savedAddress);
        });
    }

    private void saveAddressDetails(JSONArray addressesListJson, Address savedAddress) {
        for (int j = 0; j < addressesListJson.length(); j++) {
            if (addressesListJson.optJSONObject(j).optJSONObject("streetAddress") != null) {
                JSONObject streetAddressJson = addressesListJson.optJSONObject(j).optJSONObject("streetAddress");
                saveStreetAddress(streetAddressJson, savedAddress);
            }

            if (addressesListJson.optJSONObject(j).optJSONObject("postOfficeBoxStreetAddress") != null) {
                JSONObject postOfficeBoxAddressJson = addressesListJson.optJSONObject(j)
                        .optJSONObject("postOfficeBoxStreetAddress");
                savePostOfficeBoxAddress(postOfficeBoxAddressJson, savedAddress);
            }
        }
    }

    private void saveStreetAddress(JSONObject streetAddressJson, Address savedAddress) {
        StreetAddress streetAddress = OrganizationUtil.createStreetAddress(streetAddressJson);
        streetAddress.setAddress(savedAddress);
        StreetAddress savedStreetAddress = organizationService.saveStreetAddress(streetAddress);
        saveStreetAddressMunicipality(streetAddressJson.optJSONObject("municipality"), savedStreetAddress);
        saveStreetAddressAdditionalInformation(streetAddressJson.optJSONArray("additionalInformation"), savedStreetAddress);
        saveStreetAddressPostOffice(streetAddressJson.optJSONArray("postOffice"), savedStreetAddress);
        saveStreetAddressStreet(streetAddressJson.optJSONArray("street"), savedStreetAddress);
    }

    private void saveStreetAddressMunicipality(JSONObject municipalityJson, StreetAddress savedStreetAddress) {
        if (municipalityJson != null) {
            StreetAddressMunicipality streetAddressMunicipality = OrganizationUtil
                    .createStreetAddressMunicipality(municipalityJson);
            streetAddressMunicipality.setStreetAddress(savedStreetAddress);
            StreetAddressMunicipality savedStreetAddressMunicipality = organizationService
                    .saveStreetAddressMunicipality(streetAddressMunicipality);

            if (municipalityJson.optJSONArray("name") != null) {
                JSONArray streetAddressMunicipalityNamesJson = municipalityJson.optJSONArray("name");
                List<StreetAddressMunicipalityName> streetAddressMunicipalityNames = OrganizationUtil
                        .createStreetAddressMunicipalityNames(streetAddressMunicipalityNamesJson);
                streetAddressMunicipalityNames.forEach(municipalityName -> {
                    municipalityName.setStreetAddressMunicipality(savedStreetAddressMunicipality);
                    organizationService.saveStreetAddressMunicipalityName(municipalityName);
                });
            }
        }
    }

    private void saveStreetAddressAdditionalInformation(JSONArray additionalInformationJson, StreetAddress savedStreetAddress) {
        if (additionalInformationJson != null) {
            List<StreetAddressAdditionalInformation> streetAddressAdditionalInformationList = OrganizationUtil
                    .createStreetAddressAdditionalInformation(additionalInformationJson);
            streetAddressAdditionalInformationList.forEach(additionalInfo -> {
                additionalInfo.setStreetAddress(savedStreetAddress);
                organizationService.saveStreetAddressAdditionalInformation(additionalInfo);
            });
        }
    }

    private void saveStreetAddressPostOffice(JSONArray postOfficeJson, StreetAddress savedStreetAddress) {
        if (postOfficeJson!= null) {
            List<StreetAddressPostOffice> streetAddressPostOfficeList = OrganizationUtil
                    .createStreetAddressPostOffices(postOfficeJson);
            streetAddressPostOfficeList.forEach(postOffice -> {
                postOffice.setStreetAddress(savedStreetAddress);
                organizationService.saveStreetAddressPostOffice(postOffice);
            });
        }
    }

    private void saveStreetAddressStreet(JSONArray streetJson, StreetAddress savedStreetAddress) {
        if (streetJson != null) {
            List<Street> streetList = OrganizationUtil.createStreets(streetJson);
            streetList.forEach(street -> {
                street.setStreetAddress(savedStreetAddress);
                organizationService.saveStreet(street);
            });
        }
    }

    private void savePostOfficeBoxAddress(JSONObject postOfficeBoxAddressJson, Address savedAddress) {
        PostOfficeBoxAddress postOfficeBoxAddress = OrganizationUtil.createPostOfficeBoxAddress(postOfficeBoxAddressJson);
        postOfficeBoxAddress.setAddress(savedAddress);
        PostOfficeBoxAddress savedPostOfficeBoxAddress = organizationService.savePostOfficeBoxAddress(postOfficeBoxAddress);

        savePostOfficeBoxAddressAdditionalInformation(postOfficeBoxAddressJson.optJSONArray("additionalInformation"),
                savedPostOfficeBoxAddress);
        savePostOffice(postOfficeBoxAddressJson.optJSONArray("postOffice"), savedPostOfficeBoxAddress);
        savePostOfficeBoxAddressMunicipality(postOfficeBoxAddressJson.optJSONObject("municipality"), savedPostOfficeBoxAddress);
        savePostOfficeBox(postOfficeBoxAddressJson.optJSONArray("postOfficeBox"), savedPostOfficeBoxAddress);
    }

    private void savePostOfficeBoxAddressAdditionalInformation(JSONArray additionalInformationJson,
                                                               PostOfficeBoxAddress savedPostOfficeBoxAddress) {
        if (additionalInformationJson != null) {
            List<PostOfficeBoxAddressAdditionalInformation> postOfficeBoxAddressAdditionalInformationList
                    = OrganizationUtil.createPostOfficeBoxAddressAdditionalInformation(additionalInformationJson);
            postOfficeBoxAddressAdditionalInformationList.forEach(additionalInfo -> {
                additionalInfo.setPostOfficeBoxAddress(savedPostOfficeBoxAddress);
                organizationService.savePostOfficeBoxAddressAdditionalInformation(additionalInfo);
            });
        }
    }

    private void savePostOffice(JSONArray postOfficeJson, PostOfficeBoxAddress savedPostOfficeBoxAddress) {
        if (postOfficeJson != null) {
            List<PostOffice> postOfficeList = OrganizationUtil.createPostOffice(postOfficeJson);
            postOfficeList.forEach(postOffice -> {
                postOffice.setPostOfficeBoxAddress(savedPostOfficeBoxAddress);
                organizationService.savePostOffice(postOffice);
            });
        }
    }

    private void savePostOfficeBoxAddressMunicipality(JSONObject municipalityJson,
                                                      PostOfficeBoxAddress savedPostOfficeBoxAddress) {
        if (municipalityJson!= null) {
            PostOfficeBoxAddressMunicipality postOfficeBoxAddressMunicipality = OrganizationUtil
                    .createPostOfficeBoxAddressMunicipality(municipalityJson);
            postOfficeBoxAddressMunicipality.setPostOfficeBoxAddress(savedPostOfficeBoxAddress);
            PostOfficeBoxAddressMunicipality savedPostOfficeBoxAddressMunicipality = organizationService
                    .savePostOfficeBoxAddressMunicipality(postOfficeBoxAddressMunicipality);

            if (municipalityJson.optJSONArray("name") != null) {
                JSONArray postOfficeBoxAddressMunicipalityNamesJson = municipalityJson.optJSONArray("name");
                List<PostOfficeBoxAddressMunicipalityName> postOfficeBoxAddressMunicipalityNames = OrganizationUtil
                        .createPostOfficeBoxAddressMunicipalityNames(postOfficeBoxAddressMunicipalityNamesJson);
                postOfficeBoxAddressMunicipalityNames.forEach(municipalityName -> {
                    municipalityName.setPostOfficeBoxAddressMunicipality(savedPostOfficeBoxAddressMunicipality);
                    organizationService.savePostOfficeBoxAddressMunicipalityName(municipalityName);
                });
            }
        }
    }

    private void savePostOfficeBox(JSONArray postOfficeBoxJson, PostOfficeBoxAddress savedPostOfficeBoxAddress) {
        if (postOfficeBoxJson != null) {
            List<PostOfficeBox> postOfficeBoxList = OrganizationUtil
                    .createPostOfficeBoxes(postOfficeBoxJson);
            postOfficeBoxList.forEach(postOfficeBox -> {
                postOfficeBox.setPostOfficeBoxAddress(savedPostOfficeBoxAddress);
                organizationService.savePostOfficeBox(postOfficeBox);
            });
        }
    }

}
