/**
 * The MIT License
 * Copyright (c) 2020, Population Register Centre (VRK)
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

import java.util.ArrayList;
import java.util.List;


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
            if (client.getMemberClass().equalsIgnoreCase(COMMERCIAL_MEMBER_CLASS)) {
                log.info("Fetching data for company with businessCode {}", client.getMemberCode());
                String businessCode = clientType.getId().getMemberCode();
                JSONObject companyJson = OrganizationUtil.getCompany(fetchCompaniesUrl, businessCode, catalogService);
                saveData(companyJson.optJSONArray("results"));
                log.info("Successfully saved data for company with businessCode {}", businessCode);
            }

            return true;
        } else {
            return false;
        }
    }

    private void saveData(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            Company company = OrganizationUtil.createCompany(data.optJSONObject(i));
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

            List<BusinessAddress> businessAddresses = businessAddressesJson != null
                    ? OrganizationUtil.createBusinessAddresses(businessAddressesJson)
                    : new ArrayList<>();
            List<BusinessAuxiliaryName> businessAuxiliaryNames = businessAuxiliaryNamesJson != null
                    ? OrganizationUtil.createBusinessAuxiliaryNames(businessAuxiliaryNamesJson)
                    : new ArrayList<>();
            List<BusinessIdChange> businessIdChanges = businessIdChangesJson != null
                    ? OrganizationUtil.createBusinessIdChanges(businessIdChangesJson)
                    : new ArrayList<>();
            List<BusinessLine> businessLines = businessLinesJson != null
                    ? OrganizationUtil.createBusinessLines(businessLinesJson)
                    : new ArrayList<>();
            List<BusinessName> businessNames = businessNamesJson != null
                    ? OrganizationUtil.createBusinessNames(businessNamesJson)
                    : new ArrayList<>();
            List<CompanyForm> companyForms = companyFormsJson != null
                    ? OrganizationUtil.createCompanyForms(companyFormsJson)
                    : new ArrayList<>();
            List<ContactDetail> contactDetails = contactDetailsJson != null
                    ? OrganizationUtil.createContactDetails(contactDetailsJson)
                    : new ArrayList<>();
            List<Language> languages = languagesJson != null
                    ? OrganizationUtil.createLanguages(languagesJson)
                    : new ArrayList<>();
            List<Liquidation> liquidations = liquidationsJson != null
                    ? OrganizationUtil.createLiquidations(liquidationsJson)
                    : new ArrayList<>();
            List<RegisteredEntry> registeredEntries = registeredEntriesJson != null
                    ? OrganizationUtil.createRegisteredEntries(registeredEntriesJson)
                    : new ArrayList<>();
            List<RegisteredOffice> registeredOffices = registeredOfficesJson != null
                    ? OrganizationUtil.createRegisteredOffices(registeredOfficesJson)
                    : new ArrayList<>();

            Company savedCompany = catalogService.saveCompany(company);

            businessAddresses.forEach(businessAddress -> {
                businessAddress.setCompany(savedCompany);
                catalogService.saveBusinessAddress(businessAddress);
            });

            businessAuxiliaryNames.forEach(businessAuxiliaryName -> {
                businessAuxiliaryName.setCompany(savedCompany);
                catalogService.saveBusinessAuxiliaryName(businessAuxiliaryName);
            });

            businessIdChanges.forEach(businessIdChange -> {
                businessIdChange.setCompany(savedCompany);
                catalogService.saveBusinessIdChange(businessIdChange);
            });

            businessLines.forEach(businessLine -> {
                businessLine.setCompany(savedCompany);
                catalogService.saveBusinessLine(businessLine);
            });

            businessNames.forEach(businessName -> {
                businessName.setCompany(savedCompany);
                catalogService.saveBusinessName(businessName);
            });

            companyForms.forEach(companyForm -> {
                companyForm.setCompany(savedCompany);
                catalogService.saveCompanyForm(companyForm);
            });

            contactDetails.forEach(contactDetail -> {
                contactDetail.setCompany(savedCompany);
                catalogService.saveContactDetail(contactDetail);
            });

            languages.forEach(language -> {
                language.setCompany(savedCompany);
                catalogService.saveLanguage(language);
            });

            liquidations.forEach(liquidation -> {
                liquidation.setCompany(savedCompany);
                catalogService.saveLiquidation(liquidation);
            });

            registeredEntries.forEach(registeredEntry -> {
                registeredEntry.setCompany(savedCompany);
                catalogService.saveRegisteredEntry(registeredEntry);
            });

            registeredOffices.forEach(registeredOffice -> {
                registeredOffice.setCompany(savedCompany);
                catalogService.saveRegisteredOffice(registeredOffice);
            });
        }
    }
}
