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
package fi.vrk.xroad.catalog.collector.util;

import fi.vrk.xroad.catalog.persistence.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OrganizationUtil {

    private OrganizationUtil() {

    }

    public static JSONObject getCompany(String url, String businessCode) {
        final String fetchCompaniesUrl = new StringBuilder().append(url)
                .append("/").append(businessCode).toString();
        JSONObject jsonObject = new JSONObject();
        try {
            String ret = getResponseBody(fetchCompaniesUrl);
            jsonObject = new JSONObject(ret);
            return jsonObject;
        } catch (KeyStoreException e) {
            log.error("KeyStoreException occurred when fetching companies from url {} with businessCode {}", url, businessCode);
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException occurred when fetching companies from url {} with businessCode {}", url, businessCode);
        } catch (KeyManagementException e) {
            log.error("KeyManagementException occurred when fetching companies from url {} with businessCode {}", url, businessCode);
        }
        return jsonObject;
    }

    public static List<String> getOrganizationIdsList(String url, Integer fetchOrganizationsLimit)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String response = getResponseBody(url);
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

    public static List<JSONArray> getOrganizationData(List<String> idsList, String host, Integer maxPerRequest) {
        List<JSONArray> fullList = new ArrayList<>();
        AtomicInteger elementCount = new AtomicInteger();
        List<String> guidsList = new ArrayList<>();

        idsList.forEach(id -> {
            guidsList.add(id);
            elementCount.getAndIncrement();
            if (elementCount.get() % maxPerRequest == 0) {
                fullList.add(getDataByIds(guidsList, host));
                guidsList.clear();
            }
            if (elementCount.get() == idsList.size()) {
                fullList.add(getDataByIds(guidsList, host));
            }
        });

        return fullList;
    }

    public static Organization createOrganization(JSONObject jsonObject) {
        return Organization.builder().businessCode(jsonObject.optString("businessCode"))
                .guid(jsonObject.optString("id"))
                .organizationType(jsonObject.optString("organizationType"))
                .publishingStatus(jsonObject.optString("publishingStatus"))
                .build();
    }

    public static List<Email> createEmails(JSONArray jsonArray) {
        List<Email> emails = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            emails.add(Email.builder()
                    .description(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("description")))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("value"))).build());
        }
        return emails;
    }

    public static List<WebPage> createWebPages(JSONArray jsonArray) {
        List<WebPage> webPages = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            webPages.add(WebPage.builder()
                    .url(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("url")))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("value"))).build());
        }
        return webPages;
    }

    public static List<OrganizationDescription> createDescriptions(JSONArray jsonArray) {
        List<OrganizationDescription> organizationDescriptions = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            organizationDescriptions.add(OrganizationDescription.builder()
                    .type(jsonArray.optJSONObject(i).optString("type"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("value"))).build());
        }
        return organizationDescriptions;
    }

    private static String replaceUnicodeControlCharacters(String input) {
        return input.replaceAll("[\\x{0000}-\\x{0009}]|[\\x{000b}-\\x{000c}]|[\\x{000e}-\\x{000f}]|[\\x{0010}-\\x{001f}]", "");
    }

    public static List<OrganizationName> createNames(JSONArray jsonArray) {
        List<OrganizationName> organizationNames = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            organizationNames.add(OrganizationName.builder()
                    .type(jsonArray.optJSONObject(i).optString("type"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("value"))).build());
        }
        return organizationNames;
    }

    public static List<Address> createAddresses(JSONArray jsonArray) {
        List<Address> addresses = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            addresses.add(Address.builder()
                    .type(jsonArray.optJSONObject(i).optString("type"))
                    .subType(jsonArray.optJSONObject(i).optString("subType"))
                    .country(jsonArray.optJSONObject(i).optString("country")).build());
        }
        return addresses;
    }

    public static StreetAddress createStreetAddress(JSONObject jsonObject) {
        return StreetAddress.builder()
                .streetNumber(jsonObject.optString("streetNumber"))
                .postalCode(jsonObject.optString("postalCode"))
                .latitude(jsonObject.optString("latitude"))
                .longitude(jsonObject.optString("longitude"))
                .coordinateState(jsonObject.optString("coordinateState")).build();
    }

    public static StreetAddressMunicipality createStreetAddressMunicipality(JSONObject jsonObject) {
        return StreetAddressMunicipality.builder()
                .code(jsonObject.optString("code")).build();
    }

    public static List<StreetAddressMunicipalityName> createStreetAddressMunicipalityNames(JSONArray jsonArray) {
        List<StreetAddressMunicipalityName> streetAddressMunicipalityNames = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            streetAddressMunicipalityNames.add(StreetAddressMunicipalityName.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(jsonArray.optJSONObject(i).optString("value")).build());
        }
        return streetAddressMunicipalityNames;
    }

    public static List<StreetAddressAdditionalInformation> createStreetAddressAdditionalInformation(JSONArray jsonArray) {
        List<StreetAddressAdditionalInformation> additionalInformationList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            additionalInformationList.add(StreetAddressAdditionalInformation.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("value"))).build());
        }
        return additionalInformationList;
    }

    public static List<StreetAddressPostOffice> createStreetAddressPostOffices(JSONArray jsonArray) {
        List<StreetAddressPostOffice> postOffices = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            postOffices.add(StreetAddressPostOffice.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(jsonArray.optJSONObject(i).optString("value")).build());
        }
        return postOffices;
    }

    public static List<Street> createStreets(JSONArray jsonArray) {
        List<Street> streets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            streets.add(Street.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(jsonArray.optJSONObject(i).optString("value")).build());
        }
        return streets;
    }

    public static PostOfficeBoxAddress createPostOfficeBoxAddress(JSONObject jsonObject) {
        return PostOfficeBoxAddress.builder()
                .postalCode(jsonObject.optString("postalCode")).build();
    }

    public static List<PostOfficeBoxAddressAdditionalInformation> createPostOfficeBoxAddressAdditionalInformation(
            JSONArray jsonArray) {
        List<PostOfficeBoxAddressAdditionalInformation> additionalInformationList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            additionalInformationList.add(PostOfficeBoxAddressAdditionalInformation.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("value"))).build());
        }
        return additionalInformationList;
    }

    public static List<PostOfficeBox> createPostOfficeBoxes(JSONArray jsonArray) {
        List<PostOfficeBox> postOfficeBoxes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            postOfficeBoxes.add(PostOfficeBox.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(jsonArray.optJSONObject(i).optString("value")).build());
        }
        return postOfficeBoxes;
    }

    public static List<PostOffice> createPostOffice(JSONArray jsonArray) {
        List<PostOffice> postOffices = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            postOffices.add(PostOffice.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(jsonArray.optJSONObject(i).optString("value")).build());
        }
        return postOffices;
    }

    public static PostOfficeBoxAddressMunicipality createPostOfficeBoxAddressMunicipality(JSONObject jsonObject) {
        return PostOfficeBoxAddressMunicipality.builder()
                .code(jsonObject.optString("code")).build();
    }

    public static List<PostOfficeBoxAddressMunicipalityName> createPostOfficeBoxAddressMunicipalityNames(JSONArray jsonArray) {
        List<PostOfficeBoxAddressMunicipalityName> streetAddressMunicipalityNames = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            streetAddressMunicipalityNames.add(PostOfficeBoxAddressMunicipalityName.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(jsonArray.optJSONObject(i).optString("value")).build());
        }
        return streetAddressMunicipalityNames;
    }

    public static List<PhoneNumber> createPhoneNumbers(JSONArray jsonArray) {
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            phoneNumbers.add(PhoneNumber.builder()
                    .additionalInformation(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("additionalInformation")))
                    .number(jsonArray.optJSONObject(i).optString("number"))
                    .isFinnishServiceNumber(jsonArray.optJSONObject(i).getBoolean("isFinnishServiceNumber"))
                    .prefixNumber(jsonArray.optJSONObject(i).optString("prefixNumber"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .chargeDescription(replaceUnicodeControlCharacters(jsonArray.optJSONObject(i).optString("chargeDescription")))
                    .serviceChargeType(jsonArray.optJSONObject(i).optString("serviceChargeType")).build());
        }
        return phoneNumbers;
    }

    public static Company createCompany(JSONObject jsonObject) {
        return Company.builder().businessId(jsonObject.optString("businessId"))
                .companyForm(jsonObject.optString("companyForm"))
                .detailsUri(jsonObject.optString("detailsUri"))
                .name(jsonObject.optString("name"))
                .registrationDate(parseDateFromString(jsonObject.optString("registrationDate")))
                .build();
    }

    public static List<BusinessAddress> createBusinessAddresses(JSONArray jsonArray) {
        List<BusinessAddress> businessAddresses = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            businessAddresses.add(BusinessAddress.builder()
                    .careOf(jsonArray.optJSONObject(i).optString("careOf"))
                    .city(jsonArray.optJSONObject(i).optString("city"))
                    .country(jsonArray.optJSONObject(i).optString("country"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .postCode(jsonArray.optJSONObject(i).optString("postCode"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .type(jsonArray.optJSONObject(i).optLong("type"))
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .street(jsonArray.optJSONObject(i).optString("street"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        return businessAddresses;
    }

    public static List<BusinessAuxiliaryName> createBusinessAuxiliaryNames(JSONArray jsonArray) {
        List<BusinessAuxiliaryName> businessAuxiliaryNames = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            businessAuxiliaryNames.add(BusinessAuxiliaryName.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .ordering(jsonArray.optJSONObject(i).optLong("order"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        return businessAuxiliaryNames;
    }

    public static List<BusinessIdChange> createBusinessIdChanges(JSONArray jsonArray) {
        List<BusinessIdChange> businessIdChanges = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            businessIdChanges.add(BusinessIdChange.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .change(jsonArray.optJSONObject(i).optString("change"))
                    .changeDate(jsonArray.optJSONObject(i).optString("changeDate"))
                    .description(jsonArray.optJSONObject(i).optString("description"))
                    .reason(jsonArray.optJSONObject(i).optString("reason"))
                    .oldBusinessId(jsonArray.optJSONObject(i).optString("oldBusinessId"))
                    .newBusinessId(jsonArray.optJSONObject(i).optString("newBusinessId"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .build());
        }
        return businessIdChanges;
    }

    public static List<BusinessLine> createBusinessLines(JSONArray jsonArray) {
        List<BusinessLine> businessLines = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            businessLines.add(BusinessLine.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .ordering(jsonArray.optJSONObject(i).optLong("order"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        return businessLines;
    }

    public static List<BusinessName> createBusinessNames(JSONArray jsonArray) {
        List<BusinessName> businessNames = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            businessNames.add(BusinessName.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .ordering(jsonArray.optJSONObject(i).optLong("order"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        return businessNames;
    }

    public static List<CompanyForm> createCompanyForms(JSONArray jsonArray) {
        List<CompanyForm> companyForms = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            companyForms.add(CompanyForm.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .type(jsonArray.optJSONObject(i).optLong("type"))
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        return companyForms;
    }

    public static List<ContactDetail> createContactDetails(JSONArray jsonArray) {
        List<ContactDetail> contactDetails = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            contactDetails.add(ContactDetail.builder()
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .type(jsonArray.optJSONObject(i).optString("type"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(jsonArray.optJSONObject(i).optString("value"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        return contactDetails;
    }

    public static List<Language> createLanguages(JSONArray jsonArray) {
        List<Language> languages = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            languages.add(Language.builder()
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        return languages;
    }

    public static List<Liquidation> createLiquidations(JSONArray jsonArray) {
        List<Liquidation> liquidations = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            liquidations.add(Liquidation.builder()
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .type(jsonArray.optJSONObject(i).optLong("type"))
                    .registrationDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now())
                    .build());
        }
        return liquidations;
    }

    public static List<RegisteredEntry> createRegisteredEntries(JSONArray jsonArray) {
        List<RegisteredEntry> registeredEntries = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            registeredEntries.add(RegisteredEntry.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .description(jsonArray.optJSONObject(i).optString("description"))
                    .register(jsonArray.optJSONObject(i).optLong("register"))
                    .status(jsonArray.optJSONObject(i).optLong("status"))
                    .authority(jsonArray.optJSONObject(i).optLong("authority"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        return registeredEntries;
    }

    public static List<RegisteredOffice> createRegisteredOffices(JSONArray jsonArray) {
        List<RegisteredOffice> registeredOffices = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            registeredOffices.add(RegisteredOffice.builder()
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .ordering(jsonArray.optJSONObject(i).optLong("order"))
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        return registeredOffices;
    }

    private static LocalDateTime parseDateFromString(String dateValue) {
        if (dateValue != null && !dateValue.isEmpty()) {
            return LocalDate.parse(dateValue).atStartOfDay();
        }
        return null;
    }

    public static JSONArray getDataByIds(List<String> guids, String url) {
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
            String ret = getResponseBody(listOrganizationsUrl);
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

    public static String getResponseBody(String url) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        headers.setAccept(mediaTypes);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = createTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    private static RestTemplate createTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        };
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        return restTemplate;
    }
}
