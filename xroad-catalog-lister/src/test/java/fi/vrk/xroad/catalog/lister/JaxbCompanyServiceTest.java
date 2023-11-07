/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2016-2023 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.lister;

import com.google.common.collect.Iterables;
import fi.vrk.xroad.catalog.lister.util.JaxbServiceUtil;
import fi.vrk.xroad.catalog.persistence.CompanyService;
import fi.vrk.xroad.catalog.persistence.CompanyServiceImpl;
import fi.vrk.xroad.catalog.persistence.entity.BusinessAddress;
import fi.vrk.xroad.catalog.persistence.entity.BusinessAuxiliaryName;
import fi.vrk.xroad.catalog.persistence.entity.BusinessIdChange;
import fi.vrk.xroad.catalog.persistence.entity.BusinessLine;
import fi.vrk.xroad.catalog.persistence.entity.BusinessName;
import fi.vrk.xroad.catalog.persistence.entity.Company;
import fi.vrk.xroad.catalog.persistence.entity.CompanyForm;
import fi.vrk.xroad.catalog.persistence.entity.ContactDetail;
import fi.vrk.xroad.catalog.persistence.entity.Language;
import fi.vrk.xroad.catalog.persistence.entity.Liquidation;
import fi.vrk.xroad.catalog.persistence.entity.RegisteredEntry;
import fi.vrk.xroad.catalog.persistence.entity.RegisteredOffice;
import fi.vrk.xroad.xroad_catalog_lister.ChangedValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JaxbCompanyServiceTest {

    JaxbCompanyServiceImpl service = new JaxbCompanyServiceImpl();
    JaxbServiceConverter jaxbServiceConverter = new JaxbServiceConverter();
    JaxbOrganizationConverter jaxbOrganizationConverter = new JaxbOrganizationConverter();

    private CompanyService companyService;
    private static final LocalDateTime DATETIME_2016 = LocalDateTime.of(2016, 1, 1, 0, 0);
    private static final LocalDateTime DATETIME_2015 = LocalDateTime.of(2015, 1, 1, 0, 0);

    @BeforeAll
    public void setup() {
        companyService = new CompanyServiceImpl() {
            @Override
            public Iterable<Company> getCompanies(String businessId) {
                return mockGetCompanies(businessId);
            }
        };
        service = new JaxbCompanyServiceImpl();
        service.setJaxbServiceConverter(jaxbServiceConverter);
        service.setJaxbOrganizationConverter(jaxbOrganizationConverter);
        service.setCompanyService(companyService);
    }

    @Test
    public void testGetCompanies() {
        Iterable<fi.vrk.xroad.xroad_catalog_lister.Company> companies = service.getCompanies("1710128-9");
        assertEquals(1, companies.iterator().next().getBusinessAddresses().getBusinessAddress().size());
        assertEquals(1, companies.iterator().next().getBusinessAuxiliaryNames().getBusinessAuxiliaryName().size());
        assertEquals(1, companies.iterator().next().getBusinessIdChanges().getBusinessIdChange().size());
        assertEquals(1, companies.iterator().next().getBusinessLines().getBusinessLine().size());
        assertEquals(1, companies.iterator().next().getBusinessNames().getBusinessName().size());
        assertEquals(1, companies.iterator().next().getCompanyForms().getCompanyForm().size());
        assertEquals(1, companies.iterator().next().getContactDetails().getContactDetail().size());
        assertEquals(1, companies.iterator().next().getLanguages().getLanguage().size());
        assertEquals(1, companies.iterator().next().getLiquidations().getLiquidation().size());
        assertEquals(1, companies.iterator().next().getRegisteredEntries().getRegisteredEntry().size());
        assertEquals(1, companies.iterator().next().getRegisteredOffices().getRegisteredOffice().size());
        assertEquals("1710128-9", companies.iterator().next().getBusinessId());
        assertEquals("Oyj", companies.iterator().next().getCompanyForm());
        assertEquals("Gofore Oyj", companies.iterator().next().getName());
        assertEquals("Katu 1", companies.iterator().next().getBusinessAddresses().getBusinessAddress().get(0).getStreet());
        assertEquals("Auxiliary name", companies.iterator().next().getBusinessAuxiliaryNames().getBusinessAuxiliaryName().get(0).getName());
        assertEquals("12345", companies.iterator().next().getBusinessIdChanges().getBusinessIdChange().get(0).getOldBusinessId());
        assertEquals("Business line", companies.iterator().next().getBusinessLines().getBusinessLine().get(0).getName());
        assertEquals("EN", companies.iterator().next().getBusinessNames().getBusinessName().get(0).getLanguage());
        assertEquals("FORM", companies.iterator().next().getCompanyForms().getCompanyForm().get(0).getName());
        assertEquals("EN", companies.iterator().next().getContactDetails().getContactDetail().get(0).getLanguage());
        assertEquals("Osakeyhtiö", companies.iterator().next().getLanguages().getLanguage().get(0).getName());
        assertEquals("EN", companies.iterator().next().getLiquidations().getLiquidation().get(0).getLanguage());
        assertEquals("Description", companies.iterator().next().getRegisteredEntries().getRegisteredEntry().get(0).getDescription());
        assertEquals("EN", companies.iterator().next().getRegisteredOffices().getRegisteredOffice().get(0).getLanguage());
    }

    @Test
    public void testGetChangedCompanyValuesAfter2014() {
        XMLGregorianCalendar changedAfter20141231 = JaxbServiceUtil.toXmlGregorianCalendar(LocalDateTime.of(2014, 12, 31, 0, 0));
        XMLGregorianCalendar endDateTime = JaxbServiceUtil.toXmlGregorianCalendar(LocalDateTime.of(2022, 12, 31, 0, 0));
        Iterable<ChangedValue> changedValues = service.getChangedCompanyValues("1710128-9",
                changedAfter20141231, endDateTime);
        assertEquals(12, Iterables.size(changedValues));
    }

    @Test
    public void testGetChangedCompanyValuesAfter2015() {
        XMLGregorianCalendar changedAfter20151231 = JaxbServiceUtil.toXmlGregorianCalendar(LocalDateTime.of(2015, 12, 31, 0, 0));
        XMLGregorianCalendar endDateTime = JaxbServiceUtil.toXmlGregorianCalendar(LocalDateTime.of(2022, 12, 31, 0, 0));
        Iterable<ChangedValue> changedValues = service.getChangedCompanyValues("1710128-9",
                changedAfter20151231, endDateTime);
        assertEquals(1, Iterables.size(changedValues));
        assertEquals("CompanyForm", changedValues.iterator().next().getName());
    }

    @Test
    public void testGetChangedCompanyValuesAfter2016() {
        XMLGregorianCalendar changedAfter20160101 = JaxbServiceUtil.toXmlGregorianCalendar(LocalDateTime.of(2016, 1, 1, 0, 0));
        XMLGregorianCalendar endDateTime = JaxbServiceUtil.toXmlGregorianCalendar(LocalDateTime.of(2022, 12, 31, 0, 0));
        Iterable<ChangedValue> changedValues = service.getChangedCompanyValues("1710128-9",
                changedAfter20160101, endDateTime);
        assertEquals(0, Iterables.size(changedValues));
    }

    private Iterable<Company> mockGetCompanies(String businessId) {
        List<Company> companies = new ArrayList<>();
        companies.add(createTestCompanyByBusinessId(businessId));
        return companies;
    }

    private Company createTestCompanyByBusinessId(String businessId) {
        Company company = new Company();
        company.setId(1L);
        company.setBusinessId(businessId);
        company.setCompanyForm("Oyj");
        company.setDetailsUri("");
        company.setName("Gofore Oyj");
        company.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        company.setBusinessAddresses(createBusinessAddresses(company));
        company.setBusinessAuxiliaryNames(createBusinessAuxiliaryNames(company));
        company.setBusinessIdChanges(createBusinessIdChanges(company));
        company.setBusinessLines(createBusinessLines(company));
        company.setBusinessNames(createBusinessNames(company));
        company.setCompanyForms(createCompanyForms(company));
        company.setContactDetails(createContactDetails(company));
        company.setLanguages(createLanguages(company));
        company.setLiquidations(createLiquidations(company));
        company.setRegisteredEntries(createRegisteredEntries(company));
        company.setRegisteredOffices(createRegisteredOffices(company));

        return company;
    }

    private Set<BusinessAddress> createBusinessAddresses(Company c) {
        Set<BusinessAddress> businessAddresses = new HashSet<>();
        BusinessAddress businessAddress = BusinessAddress.builder().careOf("").city("Tampere").country("Finland")
                .language("FI").postCode("30123").source(0).street("Katu 1").type(0).version(0)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .statusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null))
                .endDate(null).company(c).build();
        businessAddresses.add(businessAddress);
        return businessAddresses;
    }

    private Set<BusinessAuxiliaryName> createBusinessAuxiliaryNames(Company c) {
        Set<BusinessAuxiliaryName> businessAuxiliaryNames = new HashSet<>();
        BusinessAuxiliaryName businessAuxiliaryName = BusinessAuxiliaryName.builder()
                .name("Auxiliary name").language("EN").ordering(0).source(0).version(0)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .statusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null))
                .endDate(null).company(c).build();
        businessAuxiliaryNames.add(businessAuxiliaryName);
        return businessAuxiliaryNames;
    }

    private Set<BusinessIdChange> createBusinessIdChanges(Company c) {
        Set<BusinessIdChange> businessIdChanges = new HashSet<>();
        BusinessIdChange businessIdChange = BusinessIdChange.builder()
                .language("EN").change("1").description("Change description").reason("Change reason")
                .source(0).oldBusinessId("12345").newBusinessId("67890").changeDate("2020-01-25")
                .statusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null))
                .company(c).build();
        businessIdChanges.add(businessIdChange);
        return businessIdChanges;
    }

    private Set<BusinessLine> createBusinessLines(Company c) {
        Set<BusinessLine> businessLines = new HashSet<>();
        BusinessLine businessLine = BusinessLine.builder()
                .name("Business line").language("EN").ordering(0).source(0).version(0)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .statusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null))
                .endDate(null).company(c).build();
        businessLines.add(businessLine);
        return businessLines;
    }

    private Set<BusinessName> createBusinessNames(Company c) {
        Set<BusinessName> businessNames = new HashSet<>();
        BusinessName businessName = BusinessName.builder()
                .name("Business name").language("EN").ordering(0).source(0).version(0)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .statusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null))
                .endDate(null).company(c).build();
        businessNames.add(businessName);
        return businessNames;
    }

    private Set<CompanyForm> createCompanyForms(Company c) {
        Set<CompanyForm> companyForms = new HashSet<>();
        CompanyForm companyForm = CompanyForm.builder()
                .name("FORM").language("EN").source(0).version(0).type(1)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .statusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2016, DATETIME_2015, null))
                .endDate(null).company(c).build();
        companyForms.add(companyForm);
        return companyForms;
    }

    private Set<ContactDetail> createContactDetails(Company c) {
        Set<ContactDetail> contactDetails = new HashSet<>();
        ContactDetail contactDetail = ContactDetail.builder()
                .value("VALUE").language("EN").source(0).version(0).type("1")
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .statusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null))
                .endDate(null).company(c).build();
        contactDetails.add(contactDetail);
        return contactDetails;
    }

    private Set<Language> createLanguages(Company c) {
        Set<Language> languages = new HashSet<>();
        Language language = Language.builder()
                .name("Osakeyhtiö").language("FI").source(0).version(0)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .statusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null))
                .endDate(null).company(c).build();
        languages.add(language);
        return languages;
    }

    private Set<Liquidation> createLiquidations(Company c) {
        Set<Liquidation> liquidations = new HashSet<>();
        Liquidation liquidation = Liquidation.builder()
                .name("Liquidation").language("EN").source(0).version(0).type(1)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .statusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null))
                .endDate(null).company(c).build();
        liquidations.add(liquidation);
        return liquidations;
    }

    private Set<RegisteredEntry> createRegisteredEntries(Company c) {
        Set<RegisteredEntry> registeredEntries = new HashSet<>();
        RegisteredEntry registeredEntry = RegisteredEntry.builder()
                .status(1).authority(2).register(3).description("Description")
                .language("EN").registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .statusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null))
                .endDate(null).company(c).build();
        registeredEntries.add(registeredEntry);
        return registeredEntries;
    }

    private Set<RegisteredOffice> createRegisteredOffices(Company c) {
        Set<RegisteredOffice> registeredOffices = new HashSet<>();
        RegisteredOffice registeredOffice = RegisteredOffice.builder().source(0).ordering(0)
                .name("Registered Office").version(0).language("EN")
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .statusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null))
                .endDate(null).company(c).build();
        registeredOffices.add(registeredOffice);
        return registeredOffices;
    }

}
