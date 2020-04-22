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
package fi.vrk.xroad.catalog.collector.actors;

import fi.vrk.xroad.catalog.collector.util.OrganizationUtil;
import fi.vrk.xroad.catalog.collector.wsimport.ClientList;
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
import java.util.List;

/**
 * Actor which fetches organizations with respective data
 */
@Component
@Scope("prototype")
@Slf4j
public class FetchOrganizationsActor extends XRoadCatalogActor {

    @Value("${xroad-catalog.fetch-organizations-url}")
    private String fetchOrganizationsUrl;

    @Value("${xroad-catalog.max-organizations-per-request}")
    private Integer maxOrganizationsPerRequest;

    @Autowired
    protected CatalogService catalogService;

    @Override
    public void preStart() throws Exception {

    }

    @Override
    protected boolean handleMessage(Object message) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if (message instanceof ClientList) {

            log.info("Fetching organizations from {}", fetchOrganizationsUrl);

            List<String> organizationIds = OrganizationUtil.getOrganizationIdsList(fetchOrganizationsUrl);
            int numberOfOrganizations = organizationIds.size();

            log.info("Fetched {} organization GUIDs from {}", numberOfOrganizations, fetchOrganizationsUrl);

            List<JSONArray> organizationData = OrganizationUtil.getOrganizationData(organizationIds, fetchOrganizationsUrl, maxOrganizationsPerRequest);

            for (int i = 0; i < organizationData.size(); i++) {
                log.info("Saving {}. batch of {} organizations out of total {}",
                        (i + 1), organizationData.get(i).length(), numberOfOrganizations);
                saveBatch(organizationData.get(i));
            }

            log.info("Saved data of {} organizations successfully", numberOfOrganizations);

            return true;
        } else {
            return false;
        }
    }

    private void saveBatch(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            Organization organization = OrganizationUtil.createOrganization(data.optJSONObject(i));
            List<Email> emails = OrganizationUtil.createEmails(data.optJSONObject(i).optJSONArray("emails"));
            List<WebPage> webPages = OrganizationUtil.createWebPages(data.optJSONObject(i).optJSONArray("webPages"));
            List<OrganizationDescription> organizationDescriptions = OrganizationUtil.createDescriptions(data
                    .optJSONObject(i).optJSONArray("organizationDescriptions"));
            List<OrganizationName> organizationNames = OrganizationUtil.createNames(data
                    .optJSONObject(i).optJSONArray("organizationNames"));
            List<Address> addresses = OrganizationUtil.createAddresses(data
                    .optJSONObject(i).optJSONArray("addresses"));
            JSONArray addressesListJson = data.optJSONObject(i).optJSONArray("addresses");
            List<PhoneNumber> phoneNumbers = OrganizationUtil.createPhoneNumbers(data
                    .optJSONObject(i).optJSONArray("phoneNumbers"));

            Organization savedOrganization = catalogService.saveOrganization(organization);

            organizationNames.forEach(organizationName -> {
                organizationName.setOrganization(savedOrganization);
                catalogService.saveOrganizationName(organizationName);
            });

            organizationDescriptions.forEach(organizationDescription -> {
                organizationDescription.setOrganization(savedOrganization);
                catalogService.saveOrganizationDescription(organizationDescription);
            });

            emails.forEach(email -> {
                email.setOrganization(savedOrganization);
                catalogService.saveEmail(email);
            });

            phoneNumbers.forEach(phone -> {
                phone.setOrganization(savedOrganization);
                catalogService.savePhoneNumber(phone);
            });

            webPages.forEach(webPage -> {
                webPage.setOrganization(savedOrganization);
                catalogService.saveWebPage(webPage);
            });

            addresses.forEach(address -> {
                address.setOrganization(savedOrganization);
                Address savedAddress = catalogService.saveAddress(address);
                saveAddressDetails(addressesListJson, savedAddress);
            });
        }
    }

    private void saveAddressDetails(JSONArray addressesListJson, Address savedAddress) {
        for (int j = 0; j < addressesListJson.length(); j++) {

            // save StreetAddress
            if (addressesListJson.optJSONObject(j).optJSONObject("streetAddress") != null) {
                JSONObject streetAddressJson = addressesListJson.optJSONObject(j)
                        .optJSONObject("streetAddress");
                saveStreetAddress(streetAddressJson, savedAddress);
            }

            // save PostOfficeBoxAddress
            if (addressesListJson.optJSONObject(j).optJSONObject("postOfficeBoxStreetAddress") != null) {
                JSONObject postOfficeBoxAddressJson = addressesListJson.optJSONObject(j)
                        .optJSONObject("postOfficeBoxStreetAddress");
                savePostOfficeBoxAddress(postOfficeBoxAddressJson, savedAddress);
            }
        }
    }

    private void saveStreetAddress(JSONObject streetAddressJson, Address savedAddress) {
        // save StreetAddress
        StreetAddress streetAddress = OrganizationUtil.createStreetAddress(streetAddressJson);
        streetAddress.setAddress(savedAddress);
        StreetAddress savedStreetAddress = catalogService.saveStreetAddress(streetAddress);

        // save StreetAddressMunicipality
        if (streetAddressJson.optJSONObject("municipality") != null) {
            JSONObject streetAddressMunicipalityJson = streetAddressJson.optJSONObject("municipality");
            StreetAddressMunicipality streetAddressMunicipality = OrganizationUtil
                    .createStreetAddressMunicipality(streetAddressMunicipalityJson);
            streetAddressMunicipality.setStreetAddress(savedStreetAddress);
            StreetAddressMunicipality savedStreetAddressMunicipality = catalogService
                    .saveStreetAddressMunicipality(streetAddressMunicipality);

            // save StreetAddressMunicipalityName
            if (streetAddressJson.optJSONObject("municipality")
                    .optJSONArray("name") != null) {
                JSONArray streetAddressMunicipalityNamesJson = streetAddressJson.optJSONObject("municipality")
                        .optJSONArray("name");
                List<StreetAddressMunicipalityName> streetAddressMunicipalityNames = OrganizationUtil
                        .createStreetAddressMunicipalityNames(streetAddressMunicipalityNamesJson);
                streetAddressMunicipalityNames.forEach(municipalityName -> {
                    municipalityName.setStreetAddressMunicipality(savedStreetAddressMunicipality);
                    catalogService.saveStreetAddressMunicipalityName(municipalityName);
                });
            }
        }

        // save StreetAddressAdditionalInformation
        if (streetAddressJson.optJSONArray("additionalInformation") != null) {
            JSONArray streetAddressAdditionaInformationJsonArray = streetAddressJson
                    .optJSONArray("additionalInformation");
            List<StreetAddressAdditionalInformation> streetAddressAdditionalInformationList = OrganizationUtil
                    .createStreetAddressAdditionalInformation(streetAddressAdditionaInformationJsonArray);
            streetAddressAdditionalInformationList.forEach(additionalInfo -> {
                additionalInfo.setStreetAddress(savedStreetAddress);
                catalogService.saveStreetAddressAdditionalInformation(additionalInfo);
            });
        }

        // save PostOffice
        if (streetAddressJson.optJSONArray("postOffice") != null) {
            JSONArray streetAddressPostOfficeJsonArray = streetAddressJson
                    .optJSONArray("postOffice");
            List<StreetAddressPostOffice> streetAddressPostOfficeList = OrganizationUtil
                    .createStreetAddressPostOffices(streetAddressPostOfficeJsonArray);
            streetAddressPostOfficeList.forEach(postOffice -> {
                postOffice.setStreetAddress(savedStreetAddress);
                catalogService.saveStreetAddressPostOffice(postOffice);
            });
        }

        // save Street
        if (streetAddressJson.optJSONArray("street") != null) {
            JSONArray streetJsonArray = streetAddressJson.optJSONArray("street");
            List<Street> streetList = OrganizationUtil.createStreets(streetJsonArray);
            streetList.forEach(street -> {
                street.setStreetAddress(savedStreetAddress);
                catalogService.saveStreet(street);
            });
        }
    }

    private void savePostOfficeBoxAddress(JSONObject postOfficeBoxAddressJson, Address savedAddress) {
        // save PostOfficeBoxAddress
        PostOfficeBoxAddress postOfficeBoxAddress = OrganizationUtil.createPostOfficeBoxAddress(postOfficeBoxAddressJson);
        postOfficeBoxAddress.setAddress(savedAddress);
        PostOfficeBoxAddress savedPostOfficeBoxAddress = catalogService.savePostOfficeBoxAddress(postOfficeBoxAddress);

        // save PostOfficeBoxAddressAdditionalInformation
        if (postOfficeBoxAddressJson.optJSONArray("additionalInformation") != null) {
            JSONArray postOfficeBoxAddressAdditionalInformationJsonArray = postOfficeBoxAddressJson
                    .optJSONArray("additionalInformation");
            List<PostOfficeBoxAddressAdditionalInformation> postOfficeBoxAddressAdditionalInformationList
                    = OrganizationUtil.createPostOfficeBoxAddressAdditionalInformation(
                    postOfficeBoxAddressAdditionalInformationJsonArray);
            postOfficeBoxAddressAdditionalInformationList.forEach(additionalInfo -> {
                additionalInfo.setPostOfficeBoxAddress(savedPostOfficeBoxAddress);
                catalogService.savePostOfficeBoxAddressAdditionalInformation(additionalInfo);
            });
        }

        // save PostOffice
        if (postOfficeBoxAddressJson.optJSONArray("postOffice") != null) {
            JSONArray postOfficeJsonArray = postOfficeBoxAddressJson.optJSONArray("postOffice");
            List<PostOffice> postOfficeList = OrganizationUtil.createPostOffice(postOfficeJsonArray);
            postOfficeList.forEach(postOffice -> {
                postOffice.setPostOfficeBoxAddress(savedPostOfficeBoxAddress);
                catalogService.savePostOffice(postOffice);
            });
        }

        // save PostOfficeBoxAddressMunicipality
        if (postOfficeBoxAddressJson.optJSONObject("municipality") != null) {
            JSONObject postOfficeBoxAddressMunicipalityJson = postOfficeBoxAddressJson.optJSONObject("municipality");
            PostOfficeBoxAddressMunicipality postOfficeBoxAddressMunicipality = OrganizationUtil
                    .createPostOfficeBoxAddressMunicipality(postOfficeBoxAddressMunicipalityJson);
            postOfficeBoxAddressMunicipality.setPostOfficeBoxAddress(savedPostOfficeBoxAddress);
            PostOfficeBoxAddressMunicipality savedPostOfficeBoxAddressMunicipality = catalogService
                    .savePostOfficeBoxAddressMunicipality(postOfficeBoxAddressMunicipality);

            // save PostOfficeBoxAddressMunicipalityName
            if (postOfficeBoxAddressJson.optJSONObject("municipality").optJSONArray("name") != null) {
                JSONArray postOfficeBoxAddressMunicipalityNamesJson = postOfficeBoxAddressJson.optJSONObject("municipality")
                        .optJSONArray("name");
                List<PostOfficeBoxAddressMunicipalityName> postOfficeBoxAddressMunicipalityNames = OrganizationUtil
                        .createPostOfficeBoxAddressMunicipalityNames(postOfficeBoxAddressMunicipalityNamesJson);
                postOfficeBoxAddressMunicipalityNames.forEach(municipalityName -> {
                    municipalityName.setPostOfficeBoxAddressMunicipality(savedPostOfficeBoxAddressMunicipality);
                    catalogService.savePostOfficeBoxAddressMunicipalityName(municipalityName);
                });
            }
        }

        // save PostOfficeBox
        if (postOfficeBoxAddressJson.optJSONArray("postOfficeBox") != null) {
            JSONArray postOfficeBoxJsonArray = postOfficeBoxAddressJson.optJSONArray("postOfficeBox");
            List<PostOfficeBox> postOfficeBoxList = OrganizationUtil
                    .createPostOfficeBoxes(postOfficeBoxJsonArray);
            postOfficeBoxList.forEach(postOfficeBox -> {
                postOfficeBox.setPostOfficeBoxAddress(savedPostOfficeBoxAddress);
                catalogService.savePostOfficeBox(postOfficeBox);
            });
        }

    }

}
