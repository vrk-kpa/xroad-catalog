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

    private static final String COMPANY = "Company";
    private static final String BUSINESS_ADDRESS = "BusinessAddress";
    private static final String BUSINESS_AUXILIARY_NAME = "BusinessAuxiliaryName";
    private static final String BUSINESS_ID_CHANGE = "BusinessIdChange";
    private static final String BUSINESS_LINE = "BusinessLine";
    private static final String BUSINESS_NAME = "BusinessName";
    private static final String COMPANY_FORM = "CompanyForm";
    private static final String CONTACT_DETAIL = "ContactDetail";
    private static final String LANGUAGE = "Language";
    private static final String LIQUIDATION = "Liquidation";
    private static final String REGISTERED_ENTRY = "RegisteredEntry";
    private static final String REGISTERED_OFFICE = "RegisteredOffice";

    @Autowired
    @Setter
    private CompanyService companyService;

    @Autowired
    @Setter
    private JaxbServiceConverter jaxbServiceConverter;

    @Autowired
    @Setter
    private JaxbOrganizationConverter jaxbOrganizationConverter;

    @Override
    public Iterable<ChangedValue> getChangedCompanyValues(String businessId,
                                                          XMLGregorianCalendar startDateTime,
                                                          XMLGregorianCalendar endDateTime) {
        Iterable<fi.vrk.xroad.catalog.persistence.entity.Company> companies = companyService.getCompanies(businessId);
        List<ChangedValue> changedValueList = new ArrayList<>();
        if (companies.iterator().hasNext()) {
            companies.forEach(company -> changedValueList.addAll(getAllChangedValuesForCompany(company,
                    jaxbServiceConverter.toLocalDateTime(startDateTime),
                    jaxbServiceConverter.toLocalDateTime(endDateTime))));
            return changedValueList;
        } else {
            throw new CatalogListerRuntimeException("company with businessId " + businessId + " not found");
        }
    }

    @Override
    public Iterable<Company> getCompanies(String businessId) {
        Iterable<fi.vrk.xroad.catalog.persistence.entity.Company> entities;
        entities = companyService.getCompanies(businessId);

        return jaxbOrganizationConverter.convertCompanies(entities);
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

        if (getCompanyChangedValue(company, startDateTime, endDateTime) != null) {
            changedValueList.add(getCompanyChangedValue(company, startDateTime, endDateTime));
        }

        if (getBusinessAddressChangedValue(businessAddresses, startDateTime, endDateTime) != null) {
            changedValueList.add(getBusinessAddressChangedValue(businessAddresses, startDateTime, endDateTime));
        }

        if (getBusinessAuxiliaryNamesChangedValue(businessAuxiliaryNames, startDateTime, endDateTime) != null) {
            changedValueList.add(getBusinessAuxiliaryNamesChangedValue(businessAuxiliaryNames, startDateTime, endDateTime));
        }

        if (getBusinessIdChangeChangedValue(businessIdChanges, startDateTime, endDateTime) != null) {
            changedValueList.add(getBusinessIdChangeChangedValue(businessIdChanges, startDateTime, endDateTime));
        }

        if (getBusinessLineChangedValue(businessLines, startDateTime, endDateTime) != null) {
            changedValueList.add(getBusinessLineChangedValue(businessLines, startDateTime, endDateTime));
        }

        if (getBusinessNameChangedValue(businessNames, startDateTime, endDateTime) != null) {
            changedValueList.add(getBusinessNameChangedValue(businessNames, startDateTime, endDateTime));
        }

        if (getCompanyFormChangedValue(companyForms, startDateTime, endDateTime) != null) {
            changedValueList.add(getCompanyFormChangedValue(companyForms, startDateTime, endDateTime));
        }

        if (getContactDetailChangedValue(contactDetails, startDateTime, endDateTime) != null) {
            changedValueList.add(getContactDetailChangedValue(contactDetails, startDateTime, endDateTime));
        }

        if (getLanguageChangedValue(languages, startDateTime, endDateTime) != null) {
            changedValueList.add(getLanguageChangedValue(languages, startDateTime, endDateTime));
        }

        if (getLiquidationChangedValue(liquidations, startDateTime, endDateTime) != null) {
            changedValueList.add(getLiquidationChangedValue(liquidations, startDateTime, endDateTime));
        }

        if (getRegisteredEntryChangedValue(registeredEntries, startDateTime, endDateTime) != null) {
            changedValueList.add(getRegisteredEntryChangedValue(registeredEntries, startDateTime, endDateTime));
        }

        if (getRegisteredOfficeChangedValue(registeredOffices, startDateTime, endDateTime) != null) {
            changedValueList.add(getRegisteredOfficeChangedValue(registeredOffices, startDateTime, endDateTime));
        }

        return changedValueList;
    }

    private ChangedValue getCompanyChangedValue(fi.vrk.xroad.catalog.persistence.entity.Company company,
                                                LocalDateTime startDateTime,
                                                LocalDateTime endDateTime) {
        LocalDateTime companyChange = company.getStatusInfo().getChanged();
        ChangedValue changedValue = null;
        if (companyChange.isAfter(startDateTime) && companyChange.isBefore(endDateTime)) {
            changedValue = new ChangedValue();
            changedValue.setName(COMPANY);
        }
        return changedValue;
    }

    private ChangedValue getBusinessAddressChangedValue(Set<BusinessAddress> businessAddresses,
                                                        LocalDateTime startDateTime,
                                                        LocalDateTime endDateTime) {
        ChangedValue changedValue = null;
        if (businessAddresses.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            changedValue = new ChangedValue();
            changedValue.setName(BUSINESS_ADDRESS);
        }
        return changedValue;
    }

    private ChangedValue getBusinessAuxiliaryNamesChangedValue(Set<BusinessAuxiliaryName> businessAuxiliaryNames,
                                                               LocalDateTime startDateTime,
                                                               LocalDateTime endDateTime) {
        ChangedValue changedValue = null;
        if (businessAuxiliaryNames.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            changedValue = new ChangedValue();
            changedValue.setName(BUSINESS_AUXILIARY_NAME);
        }
        return changedValue;
    }

    private ChangedValue getBusinessIdChangeChangedValue(Set<BusinessIdChange> businessIdChanges,
                                                         LocalDateTime startDateTime,
                                                         LocalDateTime endDateTime) {
        ChangedValue changedValue = null;
        if (businessIdChanges.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            changedValue = new ChangedValue();
            changedValue.setName(BUSINESS_ID_CHANGE);
        }
        return changedValue;
    }

    private ChangedValue getBusinessLineChangedValue(Set<BusinessLine> businessLines,
                                                     LocalDateTime startDateTime,
                                                     LocalDateTime endDateTime) {
        ChangedValue changedValue = null;
        if (businessLines.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            changedValue = new ChangedValue();
            changedValue.setName(BUSINESS_LINE);
        }
        return changedValue;
    }

    private ChangedValue getBusinessNameChangedValue(Set<BusinessName> businessNames,
                                                     LocalDateTime startDateTime,
                                                     LocalDateTime endDateTime) {
        ChangedValue changedValue = null;
        if (businessNames.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            changedValue = new ChangedValue();
            changedValue.setName(BUSINESS_NAME);
        }
        return changedValue;
    }

    private ChangedValue getCompanyFormChangedValue(Set<CompanyForm> companyForms,
                                                    LocalDateTime startDateTime,
                                                    LocalDateTime endDateTime) {
        ChangedValue changedValue = null;
        if (companyForms.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            changedValue = new ChangedValue();
            changedValue.setName(COMPANY_FORM);
        }
        return changedValue;
    }

    private ChangedValue getContactDetailChangedValue(Set<ContactDetail> contactDetails,
                                                      LocalDateTime startDateTime,
                                                      LocalDateTime endDateTime) {
        ChangedValue changedValue = null;
        if (contactDetails.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            changedValue = new ChangedValue();
            changedValue.setName(CONTACT_DETAIL);
        }
        return changedValue;
    }

    private ChangedValue getLanguageChangedValue(Set<Language> languages,
                                                 LocalDateTime startDateTime,
                                                 LocalDateTime endDateTime) {
        ChangedValue changedValue = null;
        if (languages.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            changedValue = new ChangedValue();
            changedValue.setName(LANGUAGE);
        }
        return changedValue;
    }

    private ChangedValue getLiquidationChangedValue(Set<Liquidation> liquidations,
                                                    LocalDateTime startDateTime,
                                                    LocalDateTime endDateTime) {
        ChangedValue changedValue = null;
        if (liquidations.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            changedValue = new ChangedValue();
            changedValue.setName(LIQUIDATION);
        }
        return changedValue;
    }

    private ChangedValue getRegisteredEntryChangedValue(Set<RegisteredEntry> registeredEntries,
                                                        LocalDateTime startDateTime,
                                                        LocalDateTime endDateTime) {
        ChangedValue changedValue = null;
        if (registeredEntries.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            changedValue = new ChangedValue();
            changedValue.setName(REGISTERED_ENTRY);
        }
        return changedValue;
    }

    private ChangedValue getRegisteredOfficeChangedValue(Set<RegisteredOffice> registeredOffices,
                                                         LocalDateTime startDateTime,
                                                         LocalDateTime endDateTime) {
        ChangedValue changedValue = null;
        if (registeredOffices.stream().anyMatch(obj -> obj.getStatusInfo().getChanged().isAfter(startDateTime) &&
                obj.getStatusInfo().getChanged().isBefore(endDateTime))) {
            changedValue = new ChangedValue();
            changedValue.setName(REGISTERED_OFFICE);
        }
        return changedValue;
    }

}
