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
import fi.vrk.xroad.catalog.collector.wsimport.ClientType;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Value("${xroad-catalog.fetch-organizations-limit}")
    private Integer fetchOrganizationsLimit;

    @Autowired
    protected CatalogService catalogService;

    @Override
    public void preStart() throws Exception {

    }

    @Override
    protected boolean handleMessage(Object message) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if (message instanceof ClientType) {

            log.info("Fetching organizations from {}", fetchOrganizationsUrl);

            List<String> organizationIds = getOrganizationIdsList(fetchOrganizationsUrl, fetchOrganizationsLimit);
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
                    log.info("Saving {}. batch of {} organizations out of total {}",
                            batchCount.get(), guidsList.size(), numberOfOrganizations);
                    saveBatch(getDataByIds(guidsList, fetchOrganizationsUrl));
                    guidsList.clear();
                }
                if (elementCount.get() == organizationIds.size()) {
                    batchCount.getAndIncrement();
                    log.info("Saving {}. batch of {} organizations out of total {}",
                            batchCount.get(), guidsList.size(), numberOfOrganizations);
                    saveBatch(getDataByIds(guidsList, fetchOrganizationsUrl));
                }
            });
            log.info("Saved data of {} organizations successfully", numberOfOrganizations);

            return true;
        } else {
            return false;
        }
    }

    private List<String> getOrganizationIdsList(String url, Integer fetchOrganizationsLimit)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String response = OrganizationUtil.getResponseBody(url);
        JSONObject json = new JSONObject(response);
        JSONArray itemList = json.optJSONArray("itemList");
        List<String> idsList = new ArrayList<>();
        int totalFetchAmount = itemList.length() > fetchOrganizationsLimit ? fetchOrganizationsLimit : itemList.length();
        for (int i = 0; i < totalFetchAmount; i++) {
            String id = itemList.optJSONObject(i).optString("id");
            idsList.add(id);
        }

        return idsList;
    }

    private JSONArray getDataByIds(List<String> guids, String url) {
        String requestGuids = "";
        for (int i = 0; i < guids.size(); i++) {
            requestGuids += guids.get(i);
            if (i < (guids.size() - 1)) {
                requestGuids += ",";
            }
        }

        final String listOrganizationsUrl = new StringBuilder().append(url)
                .append("/list?guids=").append(requestGuids).toString();

        JSONArray itemList = new JSONArray();
        try {
            String ret = OrganizationUtil.getResponseBody(listOrganizationsUrl);
            JSONObject json = new JSONObject("{\"items\":" + ret + "}");
            itemList = json.optJSONArray("items");
            return itemList;
        } catch (KeyStoreException e) {
            log.error("KeyStoreException occurred when fetching organizations with from url {}", url);
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException occurred when fetching organizations with from url {}", url);
        } catch (KeyManagementException e) {
            log.error("KeyManagementException occurred when fetching organizations with from url {}", url);
        }
        return itemList;
    }

    private void saveBatch(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            JSONObject dataJson = data.optJSONObject(i);
            Organization organization = Organization.builder().businessCode(dataJson.optString("businessCode"))
                    .guid(dataJson.optString("id")).organizationType(dataJson.optString("organizationType"))
                    .publishingStatus(dataJson.optString("publishingStatus")).build();
            log.info("Saving {}.organization with guid {}", i+1, organization.getGuid());
            organization.setOrganizationNames(createOrganizationNames(dataJson));
            organization.setOrganizationDescriptions(createOrganizationDescriptions(dataJson));
            organization.setEmails(createEmails(dataJson));
            organization.setPhoneNumbers(createPhoneNumbers(dataJson));
            organization.setWebPages(createWebPages(dataJson));
            organization.setAddresses(createAddresses(dataJson));
            catalogService.saveOrganization(organization);
        }
    }

    private Set<OrganizationName> createOrganizationNames(JSONObject data) {
        JSONArray jsonArray = data.optJSONArray("organizationNames");
        List<OrganizationName> organizationNamesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            organizationNamesList.add(OrganizationName.builder()
                    .type(jsonArray.optJSONObject(i).optString("type"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("value"))).build());
        }
        Set<OrganizationName> organizationNames = new HashSet<>();
        organizationNamesList.forEach(organizationName -> organizationNames.add(organizationName));
        return organizationNames;
    }

    private Set<OrganizationDescription> createOrganizationDescriptions(JSONObject data) {
        JSONArray jsonArray = data.optJSONArray("organizationDescriptions");
        List<OrganizationDescription> organizationDescriptionsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            organizationDescriptionsList.add(OrganizationDescription.builder()
                    .type(jsonArray.optJSONObject(i).optString("type"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("value"))).build());
        }
        Set<OrganizationDescription> organizationDescriptions = new HashSet<>();
        organizationDescriptionsList.forEach(organizationDescription -> organizationDescriptions.add(organizationDescription));
        return organizationDescriptions;
    }

    private Set<Email> createEmails(JSONObject data) {
        JSONArray jsonArray = data.optJSONArray("emails");
        List<Email> emailsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            emailsList.add(Email.builder()
                    .description(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("description")))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("value"))).build());
        }
        Set<Email> emails = new HashSet<>();
        emailsList.forEach(email -> emails.add(email));
        return emails;
    }

    private Set<PhoneNumber> createPhoneNumbers(JSONObject data) {
        JSONArray jsonArray = data.optJSONArray("phoneNumbers");
        List<PhoneNumber> phoneNumbersList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            phoneNumbersList.add(PhoneNumber.builder()
                    .additionalInformation(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("additionalInformation")))
                    .number(jsonArray.optJSONObject(i).optString("number"))
                    .isFinnishServiceNumber(jsonArray.optJSONObject(i).getBoolean("isFinnishServiceNumber"))
                    .prefixNumber(jsonArray.optJSONObject(i).optString("prefixNumber"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .chargeDescription(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("chargeDescription")))
                    .serviceChargeType(jsonArray.optJSONObject(i).optString("serviceChargeType")).build());
        }
        Set<PhoneNumber> phoneNumbers = new HashSet<>();
        phoneNumbersList.forEach(phoneNumber -> phoneNumbers.add(phoneNumber));
        return phoneNumbers;
    }

    private Set<WebPage> createWebPages(JSONObject data) {
        JSONArray jsonArray = data.optJSONArray("webPages");
        List<WebPage> webPagesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            webPagesList.add(WebPage.builder()
                    .url(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("url")))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("value"))).build());
        }
        Set<WebPage> webPages = new HashSet<>();
        webPagesList.forEach(webPage -> webPages.add(webPage));
        return webPages;
    }

    private Set<Address> createAddresses(JSONObject data) {
        JSONArray jsonArray = data.optJSONArray("addresses");
        List<Address> addressesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            addressesList.add(Address.builder()
                    .type(jsonArray.optJSONObject(i).optString("type"))
                    .subType(jsonArray.optJSONObject(i).optString("subType"))
                    .country(jsonArray.optJSONObject(i).optString("country")).build());
        }
        Set<Address> addresses = new HashSet<>();
        addressesList.forEach(address -> {
            address.setStreetAddresses(createStreetAddresses(jsonArray));
            address.setPostOfficeBoxAddresses(createPostOfficeBoxAddresses(jsonArray));
            addresses.add(address);
        });
        return addresses;
    }

    private Set<StreetAddress> createStreetAddresses(JSONArray addressesListJson) {
        Set<StreetAddress> streetAddresses = new HashSet<>();
        for (int j = 0; j < addressesListJson.length(); j++) {
            if (addressesListJson.optJSONObject(j).optJSONObject("streetAddress") != null) {
                JSONObject streetAddressJson = addressesListJson.optJSONObject(j).optJSONObject("streetAddress");
                StreetAddress streetAddress = StreetAddress.builder()
                        .streetNumber(streetAddressJson.optString("streetNumber"))
                        .postalCode(streetAddressJson.optString("postalCode"))
                        .latitude(streetAddressJson.optString("latitude"))
                        .longitude(streetAddressJson.optString("longitude"))
                        .coordinateState(streetAddressJson.optString("coordinateState")).build();
                streetAddress.setMunicipalities(createStreetAddressMunicipalities(streetAddressJson.optJSONObject("municipality")));
                streetAddress.setAdditionalInformation(createStreetAddressAdditionalInformation(streetAddressJson.optJSONArray("additionalInformation")));
                streetAddress.setPostOffices(createStreetAddressPostOffices(streetAddressJson.optJSONArray("postOffice")));
                streetAddress.setStreets(createStreetAddressStreets(streetAddressJson.optJSONArray("street")));
                streetAddresses.add(streetAddress);
            }
        }
        return streetAddresses;
    }

    private Set<PostOfficeBoxAddress> createPostOfficeBoxAddresses(JSONArray addressesListJson) {
        Set<PostOfficeBoxAddress> postOfficeBoxAddresses = new HashSet<>();
        for (int j = 0; j < addressesListJson.length(); j++) {
            if (addressesListJson.optJSONObject(j).optJSONObject("postOfficeBoxStreetAddress") != null) {
                JSONObject postOfficeBoxAddressJson = addressesListJson.optJSONObject(j)
                        .optJSONObject("postOfficeBoxStreetAddress");
                PostOfficeBoxAddress postOfficeBoxAddress = PostOfficeBoxAddress.builder()
                        .postalCode(postOfficeBoxAddressJson.optString("postalCode")).build();
                postOfficeBoxAddress.setAdditionalInformation(createPostOfficeBoxAddressAdditionalInformation(
                        postOfficeBoxAddressJson.optJSONArray("additionalInformation")));
                postOfficeBoxAddress.setPostOffices(createPostOffices(postOfficeBoxAddressJson.optJSONArray("postOffice")));
                postOfficeBoxAddress.setPostOfficesBoxes(createPostOfficeBoxes(postOfficeBoxAddressJson.optJSONArray("postOfficeBox")));
                postOfficeBoxAddress.setPostOfficeBoxAddressMunicipalities(createPostOfficeBoxAddressMunicipalities(
                        postOfficeBoxAddressJson.optJSONObject("municipality")));
                postOfficeBoxAddresses.add(postOfficeBoxAddress);
            }
        }
        return postOfficeBoxAddresses;
    }

    private Set<StreetAddressMunicipality> createStreetAddressMunicipalities(JSONObject municipalityJson) {
        Set<StreetAddressMunicipality> streetAddressMunicipalities = new HashSet<>();
        if (municipalityJson != null) {
            StreetAddressMunicipality streetAddressMunicipality = StreetAddressMunicipality.builder()
                    .code(municipalityJson.optString("code")).build();
            Set<StreetAddressMunicipalityName> streetAddressMunicipalityNames = new HashSet<>();
            if (municipalityJson.optJSONArray("name") != null) {
                JSONArray streetAddressMunicipalityNamesJson = municipalityJson.optJSONArray("name");
                for (int i = 0; i < streetAddressMunicipalityNamesJson.length(); i++) {
                    streetAddressMunicipalityNames.add(StreetAddressMunicipalityName.builder()
                            .language(streetAddressMunicipalityNamesJson.optJSONObject(i).optString("language"))
                            .value(streetAddressMunicipalityNamesJson.optJSONObject(i).optString("value")).build());
                }
            }
            streetAddressMunicipality.setStreetAddressMunicipalityNames(streetAddressMunicipalityNames);
            streetAddressMunicipalities.add(streetAddressMunicipality);
        }
        return streetAddressMunicipalities;
    }

    private Set<StreetAddressAdditionalInformation> createStreetAddressAdditionalInformation(JSONArray additionalInformationJson) {
        Set<StreetAddressAdditionalInformation> streetAddressAdditionalInformation = new HashSet<>();
        if (additionalInformationJson != null) {
            for (int i = 0; i < additionalInformationJson.length(); i++) {
                streetAddressAdditionalInformation.add(StreetAddressAdditionalInformation.builder()
                        .language(additionalInformationJson.optJSONObject(i).optString("language"))
                        .value(replaceUnicodeControlCharacters(additionalInformationJson.optJSONObject(i).optString("value"))).build());
            }
        }
        return streetAddressAdditionalInformation;
    }

    private Set<StreetAddressPostOffice> createStreetAddressPostOffices(JSONArray postOfficeJson) {
        Set<StreetAddressPostOffice> streetAddressPostOffices = new HashSet<>();
        if (postOfficeJson!= null) {
            for (int i = 0; i < postOfficeJson.length(); i++) {
                streetAddressPostOffices.add(StreetAddressPostOffice.builder()
                        .language(postOfficeJson.optJSONObject(i).optString("language"))
                        .value(postOfficeJson.optJSONObject(i).optString("value")).build());
            }
        }
        return streetAddressPostOffices;
    }

    private Set<Street> createStreetAddressStreets(JSONArray streetJson) {
        Set<Street> streets = new HashSet<>();
        if (streetJson != null) {
            for (int i = 0; i < streetJson.length(); i++) {
                streets.add(Street.builder()
                        .language(streetJson.optJSONObject(i).optString("language"))
                        .value(streetJson.optJSONObject(i).optString("value")).build());
            }
        }
        return streets;
    }

    private Set<PostOfficeBoxAddressAdditionalInformation> createPostOfficeBoxAddressAdditionalInformation(JSONArray additionalInformationJson) {
        Set<PostOfficeBoxAddressAdditionalInformation> addressAdditionalInformation = new HashSet<>();
        if (additionalInformationJson != null) {
            for (int i = 0; i < additionalInformationJson.length(); i++) {
                addressAdditionalInformation.add(PostOfficeBoxAddressAdditionalInformation.builder()
                        .language(additionalInformationJson.optJSONObject(i).optString("language"))
                        .value(replaceUnicodeControlCharacters(additionalInformationJson.optJSONObject(i).optString("value"))).build());
            }
        }
        return addressAdditionalInformation;
    }

    private Set<PostOffice> createPostOffices(JSONArray postOfficeJson) {
        Set<PostOffice> postOffices = new HashSet<>();
        if (postOfficeJson != null) {
            for (int i = 0; i < postOfficeJson.length(); i++) {
                postOffices.add(PostOffice.builder()
                        .language(postOfficeJson.optJSONObject(i).optString("language"))
                        .value(postOfficeJson.optJSONObject(i).optString("value")).build());
            }
        }
        return postOffices;
    }

    private Set<PostOfficeBoxAddressMunicipality> createPostOfficeBoxAddressMunicipalities(JSONObject municipalityJson) {
        Set<PostOfficeBoxAddressMunicipality> postOfficeBoxAddressMunicipalities = new HashSet<>();
        if (municipalityJson!= null) {
            PostOfficeBoxAddressMunicipality postOfficeBoxAddressMunicipality = PostOfficeBoxAddressMunicipality.builder()
                    .code(municipalityJson.optString("code")).build();

            Set<PostOfficeBoxAddressMunicipalityName> postOfficeBoxAddressMunicipalityNames = new HashSet<>();
            if (municipalityJson.optJSONArray("name") != null) {
                JSONArray postOfficeBoxAddressMunicipalityNamesJson = municipalityJson.optJSONArray("name");
                for (int i = 0; i < postOfficeBoxAddressMunicipalityNamesJson.length(); i++) {
                    postOfficeBoxAddressMunicipalityNames.add(PostOfficeBoxAddressMunicipalityName.builder()
                            .language(postOfficeBoxAddressMunicipalityNamesJson.optJSONObject(i).optString("language"))
                            .value(postOfficeBoxAddressMunicipalityNamesJson.optJSONObject(i).optString("value")).build());
                }
            }
            postOfficeBoxAddressMunicipality.setPostOfficeBoxAddressMunicipalityNames(postOfficeBoxAddressMunicipalityNames);
            postOfficeBoxAddressMunicipalities.add(postOfficeBoxAddressMunicipality);
        }
        return postOfficeBoxAddressMunicipalities;
    }

    private Set<PostOfficeBox> createPostOfficeBoxes(JSONArray postOfficeBoxJson) {
        Set<PostOfficeBox> postOfficeBoxes = new HashSet<>();
        if (postOfficeBoxJson != null) {
            for (int i = 0; i < postOfficeBoxJson.length(); i++) {
                postOfficeBoxes.add(PostOfficeBox.builder()
                        .language(postOfficeBoxJson.optJSONObject(i).optString("language"))
                        .value(postOfficeBoxJson.optJSONObject(i).optString("value")).build());
            }
        }
        return postOfficeBoxes;
    }

    private String replaceUnicodeControlCharacters(String input) {
        return input.replaceAll("[\\x{0000}-\\x{0009}]|[\\x{000b}-\\x{000c}]|[\\x{000e}-\\x{000f}]|[\\x{0010}-\\x{001f}]", "");
    }

}
