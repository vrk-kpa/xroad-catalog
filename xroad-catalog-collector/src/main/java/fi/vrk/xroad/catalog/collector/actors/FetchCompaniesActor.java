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
import fi.vrk.xroad.catalog.collector.wsimport.XRoadClientIdentifierType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.*;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Actor which fetches companies with respective data
 */
@Component
@Scope("prototype")
@Slf4j
public class FetchCompaniesActor extends XRoadCatalogActor {

    private static final String COMMERCIAL_MEMBER_CLASS = "com";

    @Value("${xroad-catalog.fetch-companies-url}")
    private String fetchCompaniesUrl;

    @Autowired
    protected CatalogService catalogService;

    @Override
    public void preStart() throws Exception {

    }

    @Override
    protected boolean handleMessage(Object message) {
        if (message instanceof ClientType) {
            ClientType clientType = (ClientType) message;
            XRoadClientIdentifierType client = clientType.getId();
            //if (client.getMemberClass().equalsIgnoreCase(COMMERCIAL_MEMBER_CLASS)) {
                log.info("Fetching data for company with businessCode {}", client.getMemberCode());
                String businessCode = "1710128-9";//clientType.getId().getMemberCode();
                JSONObject companyJson = getCompany(fetchCompaniesUrl, businessCode);
                saveData(companyJson.optJSONArray("results"));
                log.info("Successfully saved data for company with businessCode {}", businessCode);
            //}

            return true;
        } else {
            return false;
        }
    }

    public static JSONObject getCompany(String url, String businessCode) {
        final String fetchCompaniesUrl = new StringBuilder().append(url)
                .append("/").append(businessCode).toString();
        JSONObject jsonObject = new JSONObject();
        try {
            String ret = OrganizationUtil.getResponseBody(fetchCompaniesUrl);
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

    private void saveData(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            Company company = createCompany(data.optJSONObject(i));
            JSONArray businessAddressesJson = data.optJSONObject(i).optJSONArray("addresses");
            JSONArray businessAuxiliaryNamesJson = data.optJSONObject(i).optJSONArray("auxiliaryNames");
            JSONArray businessIdChangesJson = data.optJSONObject(i).optJSONArray("businessIdChanges");
            JSONArray businessLinesJson = data.optJSONObject(i).optJSONArray("businessLines");
            JSONArray businessNamesJson = data.optJSONObject(i).optJSONArray("businessNames");
            JSONArray companyFormsJson = data.optJSONObject(i).optJSONArray("companyForms");
            JSONArray contactDetailsJson = data.optJSONObject(i).optJSONArray("contactDetails");
            JSONArray languagesJson = data.optJSONObject(i).optJSONArray("languages");
            JSONArray liquidationsJson = data.optJSONObject(i).optJSONArray("liquidations");
            JSONArray registeredEntriesJson = data.optJSONObject(i).optJSONArray("registeredEntries");
            JSONArray registeredOfficesJson = data.optJSONObject(i).optJSONArray("registeredOffices");

            Set<BusinessAddress> businessAddresses = businessAddressesJson != null
                    ? createBusinessAddresses(businessAddressesJson) : new HashSet<>();
            Set<BusinessAuxiliaryName> businessAuxiliaryNames = businessAuxiliaryNamesJson != null
                    ? createBusinessAuxiliaryNames(businessAuxiliaryNamesJson) : new HashSet<>();
            Set<BusinessIdChange> businessIdChanges = businessIdChangesJson != null
                    ? createBusinessIdChanges(businessIdChangesJson) : new HashSet<>();
            Set<BusinessLine> businessLines = businessLinesJson != null
                    ? createBusinessLines(businessLinesJson) : new HashSet<>();
            Set<BusinessName> businessNames = businessNamesJson != null
                    ? createBusinessNames(businessNamesJson) : new HashSet<>();
            Set<CompanyForm> companyForms = companyFormsJson != null
                    ? createCompanyForms(companyFormsJson) : new HashSet<>();
            Set<ContactDetail> contactDetails = contactDetailsJson != null
                    ? createContactDetails(contactDetailsJson) : new HashSet<>();
            Set<Language> languages = languagesJson != null
                    ? createLanguages(languagesJson) : new HashSet<>();
            Set<Liquidation> liquidations = liquidationsJson != null
                    ? createLiquidations(liquidationsJson) : new HashSet<>();
            Set<RegisteredEntry> registeredEntries = registeredEntriesJson != null
                    ? createRegisteredEntries(registeredEntriesJson) : new HashSet<>();
            Set<RegisteredOffice> registeredOffices = registeredOfficesJson != null
                    ? createRegisteredOffices(registeredOfficesJson) : new HashSet<>();

            company.setBusinessAddresses(businessAddresses);
            company.setBusinessAuxiliaryNames(businessAuxiliaryNames);
            company.setBusinessIdChanges(businessIdChanges);
            company.setBusinessLines(businessLines);
            company.setBusinessNames(businessNames);
            company.setCompanyForms(companyForms);
            company.setContactDetails(contactDetails);
            company.setLanguages(languages);
            company.setLiquidations(liquidations);
            company.setRegisteredEntries(registeredEntries);
            company.setRegisteredOffices(registeredOffices);
            catalogService.saveCompany(company);
        }
    }

    private Company createCompany(JSONObject jsonObject) {
        return Company.builder().businessId(jsonObject.optString("businessId"))
                .companyForm(jsonObject.optString("companyForm"))
                .detailsUri(jsonObject.optString("detailsUri"))
                .name(jsonObject.optString("name"))
                .registrationDate(parseDateFromString(jsonObject.optString("registrationDate")))
                .build();
    }

    private Set<BusinessAddress> createBusinessAddresses(JSONArray jsonArray) {
        List<BusinessAddress> businessAddressesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            businessAddressesList.add(BusinessAddress.builder()
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
        Set<BusinessAddress> businessAddresses = new HashSet<>();
        businessAddressesList.forEach(businessAddress -> businessAddresses.add(businessAddress));
        return businessAddresses;
    }

    private Set<BusinessAuxiliaryName> createBusinessAuxiliaryNames(JSONArray jsonArray) {
        List<BusinessAuxiliaryName> businessAuxiliaryNamesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            businessAuxiliaryNamesList.add(BusinessAuxiliaryName.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .ordering(jsonArray.optJSONObject(i).optLong("order"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        Set<BusinessAuxiliaryName> businessAuxiliaryNames = new HashSet<>();
        businessAuxiliaryNamesList.forEach(businessAuxiliaryName -> businessAuxiliaryNames.add(businessAuxiliaryName));
        return businessAuxiliaryNames;
    }

    private Set<BusinessIdChange> createBusinessIdChanges(JSONArray jsonArray) {
        List<BusinessIdChange> businessIdChangesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            businessIdChangesList.add(BusinessIdChange.builder()
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
        Set<BusinessIdChange> businessIdChanges = new HashSet<>();
        businessIdChangesList.forEach(businessIdChange -> businessIdChanges.add(businessIdChange));
        return businessIdChanges;
    }

    private Set<BusinessLine> createBusinessLines(JSONArray jsonArray) {
        List<BusinessLine> businessLinesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            businessLinesList.add(BusinessLine.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .ordering(jsonArray.optJSONObject(i).optLong("order"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        Set<BusinessLine> businessLines = new HashSet<>();
        businessLinesList.forEach(businessLine -> businessLines.add(businessLine));
        return businessLines;
    }

    private Set<BusinessName> createBusinessNames(JSONArray jsonArray) {
        List<BusinessName> businessNamesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            businessNamesList.add(BusinessName.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .ordering(jsonArray.optJSONObject(i).optLong("order"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        Set<BusinessName> businessNames = new HashSet<>();
        businessNamesList.forEach(businessName -> businessNames.add(businessName));
        return businessNames;
    }

    private Set<CompanyForm> createCompanyForms(JSONArray jsonArray) {
        List<CompanyForm> companyFormsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            companyFormsList.add(CompanyForm.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .type(jsonArray.optJSONObject(i).optLong("type"))
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        Set<CompanyForm> companyForms = new HashSet<>();
        companyFormsList.forEach(companyForm -> companyForms.add(companyForm));
        return companyForms;
    }

    private Set<ContactDetail> createContactDetails(JSONArray jsonArray) {
        List<ContactDetail> contactDetailsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            contactDetailsList.add(ContactDetail.builder()
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .type(jsonArray.optJSONObject(i).optString("type"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .value(jsonArray.optJSONObject(i).optString("value"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        Set<ContactDetail> contactDetails = new HashSet<>();
        contactDetailsList.forEach(contactDetail -> contactDetails.add(contactDetail));
        return contactDetails;
    }

    private Set<Language> createLanguages(JSONArray jsonArray) {
        List<Language> languagesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            languagesList.add(Language.builder()
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        Set<Language> languages = new HashSet<>();
        languagesList.forEach(language -> languages.add(language));
        return languages;
    }

    private Set<Liquidation> createLiquidations(JSONArray jsonArray) {
        List<Liquidation> liquidationsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            liquidationsList.add(Liquidation.builder()
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .type(jsonArray.optJSONObject(i).optLong("type"))
                    .registrationDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now())
                    .build());
        }
        Set<Liquidation> liquidations = new HashSet<>();
        liquidationsList.forEach(liquidation -> liquidations.add(liquidation));
        return liquidations;
    }

    private Set<RegisteredEntry> createRegisteredEntries(JSONArray jsonArray) {
        List<RegisteredEntry> registeredEntriesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            registeredEntriesList.add(RegisteredEntry.builder()
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .description(jsonArray.optJSONObject(i).optString("description"))
                    .register(jsonArray.optJSONObject(i).optLong("register"))
                    .status(jsonArray.optJSONObject(i).optLong("status"))
                    .authority(jsonArray.optJSONObject(i).optLong("authority"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        Set<RegisteredEntry> registeredEntries = new HashSet<>();
        registeredEntriesList.forEach(registeredEntry -> registeredEntries.add(registeredEntry));
        return registeredEntries;
    }

    private Set<RegisteredOffice> createRegisteredOffices(JSONArray jsonArray) {
        List<RegisteredOffice> registeredOfficesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            registeredOfficesList.add(RegisteredOffice.builder()
                    .source(jsonArray.optJSONObject(i).optLong("source"))
                    .language(jsonArray.optJSONObject(i).optString("language"))
                    .name(jsonArray.optJSONObject(i).optString("name"))
                    .ordering(jsonArray.optJSONObject(i).optLong("order"))
                    .version(jsonArray.optJSONObject(i).optLong("version"))
                    .registrationDate(parseDateFromString(jsonArray.optJSONObject(i).optString("registrationDate")))
                    .endDate(parseDateFromString(jsonArray.optJSONObject(i).optString("endDate")))
                    .build());
        }
        Set<RegisteredOffice> registeredOffices = new HashSet<>();
        registeredOfficesList.forEach(registeredOffice -> registeredOffices.add(registeredOffice));
        return registeredOffices;
    }

    private LocalDateTime parseDateFromString(String dateValue) {
        if (dateValue != null && !dateValue.isEmpty()) {
            return LocalDate.parse(dateValue).atStartOfDay();
        }
        return null;
    }
}
