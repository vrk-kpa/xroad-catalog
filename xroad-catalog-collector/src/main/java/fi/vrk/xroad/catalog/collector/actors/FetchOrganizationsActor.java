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
import fi.vrk.xroad.catalog.persistence.service.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Actor which fetches organizations with respective data
 */
@Component
@Scope("prototype")
@Slf4j
public class FetchOrganizationsActor extends XRoadCatalogActor {

    @Autowired
    protected CatalogService catalogService;

    @Override
    public void preStart() throws Exception {

    }

    @Override
    protected boolean handleMessage(Object message) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if (message instanceof ClientList) {
            String organizationListUrl = "https://api.palvelutietovaranto.suomi.fi";

            log.info("Fetching organizations from {}", organizationListUrl);

            List<String> organizationIds = OrganizationUtil.getOrganizationIdsList(organizationListUrl);
            int numberOfOrganizations = organizationIds.size();

            log.info("Fetched {} organization GUIDs from {}", numberOfOrganizations, organizationListUrl);

            List<JSONArray> organizationData = OrganizationUtil.getOrganizationData(organizationIds, organizationListUrl);

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
            Organization organization = OrganizationUtil.createOrganization(data.getJSONObject(i));
            List<Email> emails = OrganizationUtil.createEmails(data.getJSONObject(i).getJSONArray("emails"));
            List<Webpage> webPages = OrganizationUtil.createWebPages(data.getJSONObject(i).getJSONArray("webPages"));
            List<OrganizationDescription> organizationDescriptions = OrganizationUtil.createDescriptions(data
                    .getJSONObject(i).getJSONArray("organizationDescriptions"));
            List<OrganizationName> organizationNames = OrganizationUtil.createNames(data
                    .getJSONObject(i).getJSONArray("organizationNames"));
            List<Address> addresses = OrganizationUtil.createAddresses(data
                    .getJSONObject(i).getJSONArray("addresses"));
            JSONArray addressesListJson = data.getJSONObject(i).getJSONArray("addresses");
            List<PhoneNumber> phoneNumbers = OrganizationUtil.createPhoneNumbers(data
                    .getJSONObject(i).getJSONArray("phoneNumbers"));

            Organization savedOrganization = catalogService.saveOrganization(organization);
            log.info("Saved organization successfully");

            organizationNames.forEach(organizationName -> {
                catalogService.saveOrganizationName(organizationName, savedOrganization.getId());
                log.info("Saved organizationName successfully");
            });

            organizationDescriptions.forEach(organizationDescription -> {
                catalogService.saveOrganizationDescription(organizationDescription, savedOrganization.getId());
                log.info("Saved organizationDescription successfully");
            });

            emails.forEach(email -> {
                catalogService.saveEmail(email, savedOrganization.getId());
                log.info("Saved email successfully");
            });

            phoneNumbers.forEach(phone -> {
                catalogService.savePhoneNumber(phone, savedOrganization.getId());
                log.info("Saved phoneNumber successfully");
            });

            webPages.forEach(webPage -> {
                catalogService.saveWebPage(webPage, savedOrganization.getId());
                log.info("Saved webPage successfully");
            });

            addresses.forEach(address -> {
                Address savedAddress = catalogService.saveAddress(address, savedOrganization.getId());

                for (int j = 0; j < addressesListJson.length(); j++) {

                    // save StreetAddress
                    JSONObject streetAddressJson = addressesListJson.getJSONObject(j).getJSONObject("streetAddress");
                    StreetAddress streetAddress = OrganizationUtil.createStreetAddress(streetAddressJson);
                    StreetAddress savedStreetAddress = catalogService.saveStreetAddress(streetAddress, savedAddress.getId());

                    // save StreetAddressMunicipality
                    JSONObject streetAddressMunicipalityJson = streetAddressJson.getJSONObject("municipality");
                    StreetAddressMunicipality streetAddressMunicipality = OrganizationUtil
                            .createStreetAddressMunicipality(streetAddressMunicipalityJson);
                    StreetAddressMunicipality savedStreetAddressMunicipality = catalogService
                            .saveStreetAddressMunicipality(streetAddressMunicipality, savedStreetAddress.getId());

                    // save StreetAddressMunicipalityName
                    JSONArray streetAddressMunicipalityNamesJson = streetAddressJson.getJSONObject("municipality")
                            .getJSONArray("name");
                    List<StreetAddressMunicipalityName> streetAddressMunicipalityNames = OrganizationUtil
                            .createStreetAddressMunicipalityNames(streetAddressMunicipalityNamesJson);
                    List<StreetAddressMunicipalityName> savedStreetAddressMunicipalityNames = new ArrayList<>();
                    streetAddressMunicipalityNames.forEach(municipalityName -> {
                        savedStreetAddressMunicipalityNames.add(
                                catalogService.saveStreetAddressMunicipalityName(municipalityName,
                                        savedStreetAddressMunicipality.getId()));
                    });

                    // save StreetAddressAdditionalInformation
                    JSONArray streetAddressAdditionaInformationJsonArray = streetAddressJson
                            .getJSONArray("additionalInformation");
                    List<StreetAddressAdditionalInformation> streetAddressAdditionalInformationList = OrganizationUtil
                            .createStreetAddressAdditionalInformation(streetAddressAdditionaInformationJsonArray);
                    List<StreetAddressAdditionalInformation> savedStreetAddressAdditionalInformationList =
                            new ArrayList<>();
                    streetAddressAdditionalInformationList.forEach(additionalInfo -> {
                        savedStreetAddressAdditionalInformationList.add(catalogService
                                .saveStreetAddressAdditionalInformation(additionalInfo, savedStreetAddress.getId()));
                    });

                    // save PostOffice
                    JSONArray streetAddressPostOfficeJsonArray = streetAddressJson
                            .getJSONArray("postOffice");
                    List<StreetAddressPostOffice> streetAddressPostOfficeList = OrganizationUtil
                            .createStreetAddressPostOffices(streetAddressPostOfficeJsonArray);
                    List<StreetAddressPostOffice> savedStreetAddressPostOffices = new ArrayList<>();
                    streetAddressPostOfficeList.forEach(postOffice -> {
                        savedStreetAddressPostOffices.add(catalogService.saveStreetAddressPostOffice(postOffice,
                                savedStreetAddress.getId()));
                    });

                    // save Street
                    JSONArray streetJsonArray = streetAddressJson.getJSONArray("street");
                    List<Street> streetList = OrganizationUtil.createStreets(streetJsonArray);
                    List<Street> savedStreets = new ArrayList<>();
                    streetList.forEach(street -> {
                        savedStreets.add(catalogService.saveStreet(street, savedStreetAddress.getId()));
                    });

                    // TODO: same way as saving streetaddress related stuff, save also postOfficeBoxAddress stuff

                }
                log.info("Saved address successfully");
            });

        }
    }

}
