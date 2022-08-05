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

import fi.vrk.xroad.catalog.persistence.CompanyService;
import fi.vrk.xroad.catalog.persistence.entity.*;
import fi.vrk.xroad.xroad_catalog_lister.ChangedValue;
import fi.vrk.xroad.xroad_catalog_lister.Company;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Slf4j
public class JaxbCompanyServiceImpl implements JaxbCompanyService {

    @Autowired
    @Setter
    private CompanyService companyService;

    @Autowired
    @Setter
    private JaxbConverter jaxbConverter;

    @Override
    public Iterable<ChangedValue> getChangedCompanyValues(String businessId,
                                                          XMLGregorianCalendar startDateTime,
                                                          XMLGregorianCalendar endDateTime) {
        Iterable<fi.vrk.xroad.catalog.persistence.entity.Company> companies = companyService.getCompanies(businessId);
        List<ChangedValue> changedValueList = new ArrayList<>();
        if (companies.iterator().hasNext()) {
            companies.forEach(company -> changedValueList.addAll(getAllChangedValuesForCompany(company,
                    jaxbConverter.toLocalDateTime(startDateTime),
                    jaxbConverter.toLocalDateTime(endDateTime))));
            return changedValueList;
        } else {
            throw new CatalogListerRuntimeException("company with businessId " + businessId + " not found");
        }
    }

    @Override
    public Iterable<Company> getCompanies(String businessId) {
        Iterable<fi.vrk.xroad.catalog.persistence.entity.Company> entities;
        entities = companyService.getCompanies(businessId);

        return jaxbConverter.convertCompanies(entities);
    }

    private Collection<ChangedValue> getAllChangedValuesForCompany(fi.vrk.xroad.catalog.persistence.entity.Company company,
                                                                   LocalDateTime startDateTime,
                                                                   LocalDateTime endDateTime) {
        List<ChangedValue> changedValueList = new ArrayList<>();
        Set<BusinessAddress> businessAddresses = company.getAllBusinessAddresses();
        Set<BusinessAuxiliaryName> businessAuxiliaryNames = company.getAllBusinessAuxiliaryNames();
        Set<BusinessIdChange> businessIdChanges = company.getAllBusinessIdChanges();
        Set<BusinessLine> businessLines = company.getAllBusinessLines();
        Set<BusinessName> businessNames = company.getAllBusinessNames();
        Set<CompanyForm> companyForms = company.getAllCompanyForms();
        Set<ContactDetail> contactDetails = company.getAllContactDetails();
        Set<Language> languages = company.getAllLanguages();
        Set<Liquidation> liquidations = company.getAllLiquidations();
        Set<RegisteredEntry> registeredEntries = company.getAllRegisteredEntries();
        Set<RegisteredOffice> registeredOffices = company.getAllRegisteredOffices();

        LocalDateTime companyChange = company.getStatusInfo().getChanged();
        if (companyChange.isAfter(startDateTime) && companyChange.isBefore(endDateTime)) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("Company");
            changedValueList.add(changedValue);
        }

        if (businessAddresses.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("BusinessAddress");
            changedValueList.add(changedValue);
        }

        if (businessAuxiliaryNames.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("BusinessAuxiliaryName");
            changedValueList.add(changedValue);
        }

        if (businessIdChanges.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("BusinessIdChange");
            changedValueList.add(changedValue);
        }

        if (businessLines.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("BusinessLine");
            changedValueList.add(changedValue);
        }

        if (businessNames.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("BusinessName");
            changedValueList.add(changedValue);
        }

        if (companyForms.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("CompanyForm");
            changedValueList.add(changedValue);
        }

        if (contactDetails.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("ContactDetail");
            changedValueList.add(changedValue);
        }

        if (languages.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("Language");
            changedValueList.add(changedValue);
        }

        if (liquidations.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("Liquidation");
            changedValueList.add(changedValue);
        }

        if (registeredEntries.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("RegisteredEntry");
            changedValueList.add(changedValue);
        }

        if (registeredOffices.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            ChangedValue changedValue = new ChangedValue();
            changedValue.setName("RegisteredOffice");
            changedValueList.add(changedValue);
        }

        return changedValueList;
    }
}
