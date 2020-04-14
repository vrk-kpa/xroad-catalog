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
import org.springframework.beans.factory.annotation.Autowired;
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
                // saveBatch(organizationData.get(i));
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
            List<PhoneNumber> phoneNumbers = OrganizationUtil.createPhoneNumbers(data
                    .getJSONObject(i).getJSONArray("phoneNumbers"));

            Organization savedOrganization = catalogService.saveOrganization(organization);
            log.info("Saved organization successfully");

            organizationNames.forEach(organizationName -> {
                organizationName.setOrganization(savedOrganization);
                catalogService.saveOrganizationName(organizationName, savedOrganization.getId());
                log.info("Saved organizationName successfully");
            });

            organizationDescriptions.forEach(organizationDescription -> {
                organizationDescription.setOrganization(savedOrganization);
                catalogService.saveOrganizationDescription(organizationDescription, savedOrganization.getId());
                log.info("Saved organizationDescription successfully");
            });

            emails.forEach(email -> {
                email.setOrganization(savedOrganization);
                catalogService.saveEmail(email, savedOrganization.getId());
                log.info("Saved email successfully");
            });

            phoneNumbers.forEach(phone -> {
                phone.setOrganization(savedOrganization);
                catalogService.savePhoneNumber(phone, savedOrganization.getId());
                log.info("Saved phoneNumber successfully");
            });

            webPages.forEach(webPage -> {
                webPage.setOrganization(savedOrganization);
                catalogService.saveWebPage(webPage, savedOrganization.getId());
                log.info("Saved webPage successfully");
            });

            addresses.forEach(address -> {
                address.setOrganization(savedOrganization);
                catalogService.saveAddress(address, savedOrganization.getId());
                log.info("Saved address successfully");
            });

        }
    }

}
