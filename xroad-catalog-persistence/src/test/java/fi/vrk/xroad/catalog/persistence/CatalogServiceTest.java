/**
 * The MIT License
 * Copyright (c) 2021, Population Register Centre (VRK)
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
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.*;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fi.vrk.xroad.catalog.persistence.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Persistence tests
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class CatalogServiceTest {

    /**
     * item counts for test data
     */
    public static final int TEST_DATA_MEMBERS = 8;
    public static final int TEST_DATA_ACTIVE_MEMBERS = 7;
    public static final int TEST_DATA_SUBSYSTEMS = 12;
    public static final int TEST_DATA_ACTIVE_SUBSYSTEMS = 10;
    @Autowired
    CatalogService catalogService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SubsystemRepository subsystemRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    WsdlRepository wsdlRepository;

    @Autowired
    OpenApiRepository openApiRepository;

    @Autowired
    TestUtil testUtil;

    @Test
    public void testGetWsdl() {
        Wsdl wsdl = catalogService.getWsdl("1000");
        assertNotNull(wsdl);
        assertEquals("<?xml version=\"1.0\" standalone=\"no\"?><wsdl-6-1-1-1-changed/>", wsdl.getData());
        assertEquals(7, wsdl.getService().getSubsystem().getId());
    }

    @Test
    public void testGetOpenApi() {
        OpenApi openApi = catalogService.getOpenApi("3003");
        assertNotNull(openApi);
        assertEquals("<openapi>", openApi.getData());
        assertEquals(8, openApi.getService().getSubsystem().getId());
    }

    @Test
    public void testGetOrganizations() {
        Iterable<Organization> organizations = catalogService.getOrganizations("0123456-9");
        assertEquals(1, Iterables.size(organizations));
        assertEquals(1, organizations.iterator().next().getAllOrganizationNames().size());
        assertEquals(1, organizations.iterator().next().getAllOrganizationDescriptions().size());
        assertEquals(1, organizations.iterator().next().getAllEmails().size());
        assertEquals(1, organizations.iterator().next().getAllPhoneNumbers().size());
        assertEquals(1, organizations.iterator().next().getAllWebPages().size());
        assertEquals(1, organizations.iterator().next().getAllAddresses().size());
        assertEquals(1, organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllStreetAddresses().size());
        assertEquals(1, organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().size());
        assertEquals("0123456-9", organizations.iterator().next().getBusinessCode());
        assertEquals("abcdef123456", organizations.iterator().next().getGuid());
        assertEquals("Published", organizations.iterator().next().getPublishingStatus());
        assertEquals("Municipality", organizations.iterator().next().getOrganizationType());
        assertEquals("Vaasan kaupunki", organizations.iterator().next().getAllOrganizationNames().iterator().next().getValue());
        assertEquals("Vaasa on yli 67 000 asukkaan voimakkaasti kasvava kaupunki",
                organizations.iterator().next().getAllOrganizationDescriptions().iterator().next().getValue());
        assertEquals("vaasa@vaasa.fi", organizations.iterator().next().getAllEmails().iterator().next().getValue());
        assertEquals("62249111", organizations.iterator().next().getAllPhoneNumbers().iterator().next().getNumber());
        assertEquals("https://www.vaasa.fi/", organizations.iterator().next().getAllWebPages().iterator().next().getUrl());
        assertEquals("Street", organizations.iterator().next().getAllAddresses().iterator().next().getSubType());
        assertEquals("64200", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllStreetAddresses().iterator().next().getPostalCode());
        assertEquals("Motellikuja", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllStreetAddresses().iterator().next().getAllStreets().iterator().next().getValue());
        assertEquals("64200", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getPostalCode());
        assertEquals("NIVALA", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllPostOffices().iterator().next().getValue());
        assertEquals("NIVALA", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllPostOfficeBoxes().iterator().next().getValue());
        assertEquals("Kaupungintalo/kaupunginjohtaja", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllAdditionalInformation().iterator().next().getValue());
        assertEquals("545", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllMunicipalities().iterator().next().getCode());
        assertEquals("Nivala", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllMunicipalities().iterator().next()
                .getAllMunicipalityNames().iterator().next().getValue());
    }

    @Test
    public void testGetOrganization() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        assertEquals(1, organization.get().getAllOrganizationDescriptions().size());
        assertEquals(1, organization.get().getAllEmails().size());
        assertEquals(1, organization.get().getAllPhoneNumbers().size());
        assertEquals(1, organization.get().getAllWebPages().size());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next()
                .getAllStreetAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().size());
        assertEquals("0123456-9", organization.get().getBusinessCode());
        assertEquals("abcdef123456", organization.get().getGuid());
        assertEquals("Published", organization.get().getPublishingStatus());
        assertEquals("Municipality", organization.get().getOrganizationType());
        assertEquals("Vaasan kaupunki", organization.get().getAllOrganizationNames().iterator().next().getValue());
        assertEquals("Vaasa on yli 67 000 asukkaan voimakkaasti kasvava kaupunki",
                organization.get().getAllOrganizationDescriptions().iterator().next().getValue());
        assertEquals("vaasa@vaasa.fi", organization.get().getAllEmails().iterator().next().getValue());
        assertEquals("62249111", organization.get().getAllPhoneNumbers().iterator().next().getNumber());
        assertEquals("https://www.vaasa.fi/", organization.get().getAllWebPages().iterator().next().getUrl());
        assertEquals("Street", organization.get().getAllAddresses().iterator().next().getSubType());
        assertEquals("64200", organization.get().getAllAddresses().iterator().next()
                .getAllStreetAddresses().iterator().next().getPostalCode());
        assertEquals("Motellikuja", organization.get().getAllAddresses().iterator().next()
                .getAllStreetAddresses().iterator().next().getAllStreets().iterator().next().getValue());
        assertEquals("64200", organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getPostalCode());
        assertEquals("NIVALA", organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllPostOffices().iterator().next().getValue());
        assertEquals("NIVALA", organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllPostOfficeBoxes().iterator().next().getValue());
        assertEquals("Kaupungintalo/kaupunginjohtaja", organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllAdditionalInformation().iterator().next().getValue());
        assertEquals("545", organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllMunicipalities().iterator().next().getCode());
        assertEquals("Nivala", organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllMunicipalities().iterator().next()
                .getAllMunicipalityNames().iterator().next().getValue());
    }

    @Test
    public void testGetCompanies() {
        Iterable<Company> companies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        assertEquals(1, companies.iterator().next().getAllBusinessAddresses().size());
        assertEquals(1, companies.iterator().next().getAllBusinessAuxiliaryNames().size());
        assertEquals(1, companies.iterator().next().getAllBusinessIdChanges().size());
        assertEquals(1, companies.iterator().next().getAllBusinessLines().size());
        assertEquals(1, companies.iterator().next().getAllBusinessNames().size());
        assertEquals(1, companies.iterator().next().getAllCompanyForms().size());
        assertEquals(1, companies.iterator().next().getAllContactDetails().size());
        assertEquals(1, companies.iterator().next().getAllLanguages().size());
        assertEquals(1, companies.iterator().next().getAllLiquidations().size());
        assertEquals(1, companies.iterator().next().getAllRegisteredEntries().size());
        assertEquals(1, companies.iterator().next().getAllRegisteredOffices().size());
        assertEquals("1710128-9", companies.iterator().next().getBusinessId());
        assertEquals("", companies.iterator().next().getDetailsUri());
        assertEquals("OYJ", companies.iterator().next().getCompanyForm());
        assertEquals("Gofore Oyj", companies.iterator().next().getName());
        assertEquals("Kalevantie 2", companies.iterator().next().getAllBusinessAddresses().iterator().next().getStreet());
        assertEquals("Solinor", companies.iterator().next().getAllBusinessAuxiliaryNames().iterator().next().getName());
        assertEquals("1796717-0", companies.iterator().next().getAllBusinessIdChanges().iterator().next().getOldBusinessId());
        assertEquals("Dataprogrammering", companies.iterator().next().getAllBusinessLines().iterator().next().getName());
        assertEquals("FI", companies.iterator().next().getAllBusinessNames().iterator().next().getLanguage());
        assertEquals("Public limited company", companies.iterator().next().getAllCompanyForms().iterator().next().getName());
        assertEquals("EN", companies.iterator().next().getAllContactDetails().iterator().next().getLanguage());
        assertEquals("Finska", companies.iterator().next().getAllLanguages().iterator().next().getName());
        assertEquals("FI", companies.iterator().next().getAllLiquidations().iterator().next().getLanguage());
        assertEquals("Unregistered", companies.iterator().next().getAllRegisteredEntries().iterator().next().getDescription());
        assertEquals("FI", companies.iterator().next().getAllRegisteredOffices().iterator().next().getLanguage());
    }

    @Test
    public void testGetErrorLog() {
        LocalDateTime changedAfter = LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0);
        Iterable<ErrorLog> errorLogEntries = catalogService.getErrorLog(changedAfter);
        assertNotNull(errorLogEntries);
        assertEquals(true, errorLogEntries.iterator().hasNext());
    }

    @Test
    public void testGetErrors() {
        List<ErrorLog> errorLogEntries = catalogService.getErrors("DEV",
                "GOV",
                "1234",
                "TestSubsystem",
                1000L);
        assertNotNull(errorLogEntries);
        assertEquals(1, errorLogEntries.size());
    }

    @Test
    public void testSaveErrorLog() {
        ErrorLog errorLog = ErrorLog.builder().message("Error").code("500")
                .created(LocalDateTime.now()).build();
        ErrorLog savedErrorLog = catalogService.saveErrorLog(errorLog);
        assertNotNull(savedErrorLog);
    }

    @Test
    public void testSaveCompany() {
        Company company = Company.builder()
                .companyForm("OYJ")
                .businessId("123456789-1")
                .detailsUri("123")
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .name("A company").build();
        Company savedCompany = catalogService.saveCompany(company);
        assertNotNull(savedCompany);
        assertNotNull(savedCompany.getId());
        assertEquals("123456789-1", savedCompany.getBusinessId());
        assertEquals("123", savedCompany.getDetailsUri());
        assertEquals("OYJ", savedCompany.getCompanyForm());
        assertEquals("A company", savedCompany.getName());
        assertEquals(LocalDate.of(2020, 4, 30), savedCompany.getRegistrationDate().toLocalDate());
        verifySavedStatusInfo(savedCompany.getStatusInfo());
    }

    @Test
    public void testSaveBusinessName() {
        Iterable<Company> companies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        BusinessName businessName = BusinessName.builder()
                .name("").language("FI").ordering(0).source(0).version(0)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        catalogService.saveBusinessName(businessName);
        Iterable<Company> foundCompanies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(foundCompanies));
        assertEquals(1, companies.iterator().next().getAllBusinessNames().size());
        assertEquals("FI", companies.iterator().next().getAllBusinessNames().iterator().next().getLanguage());
        assertEquals("", companies.iterator().next().getAllBusinessNames().iterator().next().getName());
        assertEquals(0, companies.iterator().next().getAllBusinessNames().iterator().next().getOrdering());
        assertEquals(0, companies.iterator().next().getAllBusinessNames().iterator().next().getSource());
        assertEquals(0, companies.iterator().next().getAllBusinessNames().iterator().next().getVersion());
        assertEquals(LocalDate.of(2020, 4, 30),
                companies.iterator().next().getAllBusinessNames().iterator().next().getRegistrationDate().toLocalDate());
        assertNull(companies.iterator().next().getAllBusinessNames().iterator().next().getEndDate());
        verifySavedStatusInfo(companies.iterator().next().getAllBusinessNames().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveBusinessAuxiliaryName() {
        Iterable<Company> companies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        BusinessAuxiliaryName businessAuxiliaryName = BusinessAuxiliaryName.builder()
                .name("Solinor").language("").ordering(5).source(1).version(1)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        catalogService.saveBusinessAuxiliaryName(businessAuxiliaryName);
        Iterable<Company> foundCompanies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(foundCompanies));
        assertEquals(1, companies.iterator().next().getAllBusinessAuxiliaryNames().size());
        assertEquals("", companies.iterator().next().getAllBusinessAuxiliaryNames().iterator().next().getLanguage());
        assertEquals("Solinor", companies.iterator().next().getAllBusinessAuxiliaryNames().iterator().next().getName());
        assertEquals(5, companies.iterator().next().getAllBusinessAuxiliaryNames().iterator().next().getOrdering());
        assertEquals(1, companies.iterator().next().getAllBusinessAuxiliaryNames().iterator().next().getSource());
        assertEquals(1, companies.iterator().next().getAllBusinessAuxiliaryNames().iterator().next().getVersion());
        assertEquals(LocalDate.of(2020, 4, 30),
                companies.iterator().next().getAllBusinessAuxiliaryNames().iterator().next().getRegistrationDate().toLocalDate());
        assertNull(companies.iterator().next().getAllBusinessAuxiliaryNames().iterator().next().getEndDate());
        verifySavedStatusInfo(companies.iterator().next().getAllBusinessAuxiliaryNames().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveBusinessAddress() {
        Iterable<Company> companies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        BusinessAddress businessAddress = BusinessAddress.builder().careOf("").city("Tampere").country("Finland")
                .language("FI").postCode("30123").source(0).street("Katu 1").type(2).version(1)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        catalogService.saveBusinessAddress(businessAddress);
        Iterable<Company> foundCompanies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(foundCompanies));
        assertEquals(1, companies.iterator().next().getAllBusinessAddresses().size());
        assertEquals("FI", companies.iterator().next().getAllBusinessAddresses().iterator().next().getLanguage());
        assertEquals("", companies.iterator().next().getAllBusinessAddresses().iterator().next().getCareOf());
        assertEquals("Tampere", companies.iterator().next().getAllBusinessAddresses().iterator().next().getCity());
        assertEquals("Finland", companies.iterator().next().getAllBusinessAddresses().iterator().next().getCountry());
        assertEquals("30123", companies.iterator().next().getAllBusinessAddresses().iterator().next().getPostCode());
        assertEquals("Katu 1", companies.iterator().next().getAllBusinessAddresses().iterator().next().getStreet());
        assertEquals(0, companies.iterator().next().getAllBusinessAddresses().iterator().next().getSource());
        assertEquals(2, companies.iterator().next().getAllBusinessAddresses().iterator().next().getType());
        assertEquals(1, companies.iterator().next().getAllBusinessAddresses().iterator().next().getVersion());
        assertEquals(LocalDate.of(2020, 4, 30),
                companies.iterator().next().getAllBusinessAddresses().iterator().next().getRegistrationDate().toLocalDate());
        assertNull(companies.iterator().next().getAllBusinessAddresses().iterator().next().getEndDate());
    }

    @Test
    public void testSaveBusinessIdChange() {
        Iterable<Company> companies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        BusinessIdChange businessIdChange = BusinessIdChange.builder()
                .language("").change("44").description("Change description").reason("Change reason")
                .source(2).oldBusinessId("1796717-0").newBusinessId("1710128-9").changeDate("2020-01-25")
                .company(companies.iterator().next()).build();
        catalogService.saveBusinessIdChange(businessIdChange);
        Iterable<Company> foundCompanies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(foundCompanies));
        assertEquals(1, companies.iterator().next().getAllBusinessIdChanges().size());
        assertEquals("", companies.iterator().next().getAllBusinessIdChanges().iterator().next().getLanguage());
        assertEquals("44", companies.iterator().next().getAllBusinessIdChanges().iterator().next().getChange());
        assertEquals("Change description", companies.iterator().next().getAllBusinessIdChanges().iterator().next().getDescription());
        assertEquals("Change reason", companies.iterator().next().getAllBusinessIdChanges().iterator().next().getReason());
        assertEquals(2, companies.iterator().next().getAllBusinessIdChanges().iterator().next().getSource());
        assertEquals("1796717-0", companies.iterator().next().getAllBusinessIdChanges().iterator().next().getOldBusinessId());
        assertEquals("1710128-9", companies.iterator().next().getAllBusinessIdChanges().iterator().next().getNewBusinessId());
        assertEquals("2020-01-25", companies.iterator().next().getAllBusinessIdChanges().iterator().next().getChangeDate());
        verifySavedStatusInfo(companies.iterator().next().getAllBusinessAuxiliaryNames().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveBusinessLine() {
        Iterable<Company> companies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        BusinessLine businessLine = BusinessLine.builder()
                .name("Dataprogrammering").language("SE").ordering(0).source(2).version(1)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        catalogService.saveBusinessLine(businessLine);
        Iterable<Company> foundCompanies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(foundCompanies));
        assertEquals(1, companies.iterator().next().getAllBusinessLines().size());
        assertEquals("SE", companies.iterator().next().getAllBusinessLines().iterator().next().getLanguage());
        assertEquals("Dataprogrammering", companies.iterator().next().getAllBusinessLines().iterator().next().getName());
        assertEquals(0, companies.iterator().next().getAllBusinessLines().iterator().next().getOrdering());
        assertEquals(2, companies.iterator().next().getAllBusinessLines().iterator().next().getSource());
        assertEquals(1, companies.iterator().next().getAllBusinessLines().iterator().next().getVersion());
        assertEquals(LocalDate.of(2020, 4, 30),
                companies.iterator().next().getAllBusinessLines().iterator().next().getRegistrationDate().toLocalDate());
        assertNull(companies.iterator().next().getAllBusinessLines().iterator().next().getEndDate());
        verifySavedStatusInfo(companies.iterator().next().getAllBusinessLines().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveCompanyForm() {
        Iterable<Company> companies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        CompanyForm companyForm = CompanyForm.builder()
                .name("Public limited company").language("EN").source(1).version(1).type(0)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        catalogService.saveCompanyForm(companyForm);
        Iterable<Company> foundCompanies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(foundCompanies));
        assertEquals(1, companies.iterator().next().getAllCompanyForms().size());
        assertEquals("EN", companies.iterator().next().getAllCompanyForms().iterator().next().getLanguage());
        assertEquals("Public limited company", companies.iterator().next().getAllCompanyForms().iterator().next().getName());
        assertEquals(1, companies.iterator().next().getAllCompanyForms().iterator().next().getSource());
        assertEquals(1, companies.iterator().next().getAllCompanyForms().iterator().next().getVersion());
        assertEquals(0, companies.iterator().next().getAllCompanyForms().iterator().next().getType());
        assertEquals(LocalDate.of(2020, 4, 30),
                companies.iterator().next().getAllCompanyForms().iterator().next().getRegistrationDate().toLocalDate());
        assertNull(companies.iterator().next().getAllCompanyForms().iterator().next().getEndDate());
        verifySavedStatusInfo(companies.iterator().next().getAllCompanyForms().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveContactDetail() {
        Iterable<Company> companies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        ContactDetail contactDetail = ContactDetail.builder()
                .value("VALUE").language("EN").source(0).version(1).type("0")
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        catalogService.saveContactDetail(contactDetail);
        Iterable<Company> foundCompanies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(foundCompanies));
        assertEquals(1, companies.iterator().next().getAllContactDetails().size());
        assertEquals("EN", companies.iterator().next().getAllContactDetails().iterator().next().getLanguage());
        assertEquals("VALUE", companies.iterator().next().getAllContactDetails().iterator().next().getValue());
        assertEquals(0, companies.iterator().next().getAllContactDetails().iterator().next().getSource());
        assertEquals(1, companies.iterator().next().getAllContactDetails().iterator().next().getVersion());
        assertEquals("0", companies.iterator().next().getAllContactDetails().iterator().next().getType());
        assertEquals(LocalDate.of(2020, 4, 30),
                companies.iterator().next().getAllContactDetails().iterator().next().getRegistrationDate().toLocalDate());
        assertNull(companies.iterator().next().getAllContactDetails().iterator().next().getEndDate());
        verifySavedStatusInfo(companies.iterator().next().getAllContactDetails().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveLanguage() {
        Iterable<Company> companies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        Language language = Language.builder()
                .name("Finska").language("SE").source(0).version(1)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        catalogService.saveLanguage(language);
        Iterable<Company> foundCompanies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(foundCompanies));
        assertEquals(1, companies.iterator().next().getAllLanguages().size());
        assertEquals("SE", companies.iterator().next().getAllLanguages().iterator().next().getLanguage());
        assertEquals("Finska", companies.iterator().next().getAllLanguages().iterator().next().getName());
        assertEquals(0, companies.iterator().next().getAllLanguages().iterator().next().getSource());
        assertEquals(1, companies.iterator().next().getAllLanguages().iterator().next().getVersion());
        assertEquals(LocalDate.of(2020, 4, 30),
                companies.iterator().next().getAllLanguages().iterator().next().getRegistrationDate().toLocalDate());
        assertNull(companies.iterator().next().getAllLanguages().iterator().next().getEndDate());
        verifySavedStatusInfo(companies.iterator().next().getAllLanguages().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveLiquidation() {
        Iterable<Company> companies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        Liquidation liquidation = Liquidation.builder()
                .name("Liquidation").language("FI").source(0).version(0).type(1)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        catalogService.saveLiquidation(liquidation);
        Iterable<Company> foundCompanies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(foundCompanies));
        assertEquals(1, companies.iterator().next().getAllLiquidations().size());
        assertEquals("FI", companies.iterator().next().getAllLiquidations().iterator().next().getLanguage());
        assertEquals("Liquidation", companies.iterator().next().getAllLiquidations().iterator().next().getName());
        assertEquals(0, companies.iterator().next().getAllLiquidations().iterator().next().getSource());
        assertEquals(1, companies.iterator().next().getAllLiquidations().iterator().next().getType());
        assertEquals(0, companies.iterator().next().getAllLiquidations().iterator().next().getVersion());
        assertEquals(LocalDate.of(2020, 4, 30),
                companies.iterator().next().getAllLiquidations().iterator().next().getRegistrationDate().toLocalDate());
        assertNull(companies.iterator().next().getAllLiquidations().iterator().next().getEndDate());
        verifySavedStatusInfo(companies.iterator().next().getAllLiquidations().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveRegisteredEntry() {
        Iterable<Company> companies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        RegisteredEntry registeredEntry = RegisteredEntry.builder()
                .status(2).authority(2).register(1).description("Unregistered")
                .language("EN").registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        catalogService.saveRegisteredEntry(registeredEntry);
        Iterable<Company> foundCompanies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(foundCompanies));
        assertEquals(1, companies.iterator().next().getAllRegisteredEntries().size());
        assertEquals("EN", companies.iterator().next().getAllRegisteredEntries().iterator().next().getLanguage());
        assertEquals("Unregistered", companies.iterator().next().getAllRegisteredEntries().iterator().next().getDescription());
        assertEquals(2, companies.iterator().next().getAllRegisteredEntries().iterator().next().getStatus());
        assertEquals(2, companies.iterator().next().getAllRegisteredEntries().iterator().next().getAuthority());
        assertEquals(1, companies.iterator().next().getAllRegisteredEntries().iterator().next().getRegister());
        assertEquals(LocalDate.of(2020, 4, 30),
                companies.iterator().next().getAllRegisteredEntries().iterator().next().getRegistrationDate().toLocalDate());
        assertNull(companies.iterator().next().getAllRegisteredEntries().iterator().next().getEndDate());
        verifySavedStatusInfo(companies.iterator().next().getAllRegisteredEntries().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveRegisteredOffice() {
        Iterable<Company> companies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        RegisteredOffice registeredOffice = RegisteredOffice.builder().source(0).ordering(0)
                .name("Registered Office").version(0).language("FI")
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        catalogService.saveRegisteredOffice(registeredOffice);
        Iterable<Company> foundCompanies = catalogService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(foundCompanies));
        assertEquals(1, companies.iterator().next().getAllRegisteredOffices().size());
        assertEquals("FI", companies.iterator().next().getAllRegisteredOffices().iterator().next().getLanguage());
        assertEquals("Registered Office", companies.iterator().next().getAllRegisteredOffices().iterator().next().getName());
        assertEquals(0, companies.iterator().next().getAllRegisteredOffices().iterator().next().getSource());
        assertEquals(0, companies.iterator().next().getAllRegisteredOffices().iterator().next().getOrdering());
        assertEquals(0, companies.iterator().next().getAllRegisteredOffices().iterator().next().getVersion());
        assertEquals(LocalDate.of(2020, 4, 30),
                companies.iterator().next().getAllRegisteredOffices().iterator().next().getRegistrationDate().toLocalDate());
        assertNull(companies.iterator().next().getAllRegisteredOffices().iterator().next().getEndDate());
        verifySavedStatusInfo(companies.iterator().next().getAllRegisteredOffices().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveOrganization() {
        Organization organization = Organization.builder()
                .organizationType("Municipality")
                .businessCode("123456789-0")
                .guid("abcdef123456789")
                .publishingStatus("Published").build();
        Organization savedOrganization = catalogService.saveOrganization(organization);
        assertNotNull(savedOrganization);
        assertNotNull(savedOrganization.getId());
        assertEquals("abcdef123456789", savedOrganization.getGuid());
        assertEquals("123456789-0", savedOrganization.getBusinessCode());
        verifySavedStatusInfo(savedOrganization.getStatusInfo());
    }

    @Test
    public void testSaveOrganizationName() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        OrganizationName organizationName = OrganizationName.builder()
                .language("fi").type("Name").value("Vaasa").organization(organization.get()).build();
        catalogService.saveOrganizationName(organizationName);
        Optional<Organization> foundOrganization = catalogService.getOrganization("abcdef123456");
        assertEquals(1, foundOrganization.get().getAllOrganizationNames().size());
        assertEquals("fi", foundOrganization.get().getAllOrganizationNames().iterator().next().getLanguage());
        assertEquals("Vaasa", foundOrganization.get().getAllOrganizationNames().iterator().next().getValue());
        verifySavedStatusInfo(foundOrganization.get().getAllOrganizationNames().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveOrganizationDescription() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        OrganizationDescription organizationDescription = OrganizationDescription.builder()
                .language("fi").type("Description").value("Vaasa").organization(organization.get()).build();
        catalogService.saveOrganizationDescription(organizationDescription);
        Optional<Organization> foundOrganization = catalogService.getOrganization("abcdef123456");
        assertEquals(1, foundOrganization.get().getAllOrganizationDescriptions().size());
        assertEquals("fi", foundOrganization.get().getAllOrganizationDescriptions().iterator().next().getLanguage());
        assertEquals("Vaasa", foundOrganization.get().getAllOrganizationDescriptions().iterator().next().getValue());
        verifySavedStatusInfo(foundOrganization.get().getAllOrganizationDescriptions().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveEmail() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        Email email = Email.builder()
                .language("fi").description("Asiakaspalvelu").value("vaasa@vaasa.fi").organization(organization.get()).build();
        catalogService.saveEmail(email);
        Optional<Organization> foundOrganization = catalogService.getOrganization("abcdef123456");
        assertEquals(1, foundOrganization.get().getAllEmails().size());
        assertEquals("fi", foundOrganization.get().getAllEmails().iterator().next().getLanguage());
        assertEquals("vaasa@vaasa.fi", foundOrganization.get().getAllEmails().iterator().next().getValue());
        verifySavedStatusInfo(foundOrganization.get().getAllEmails().iterator().next().getStatusInfo());
    }

    @Test
    public void testSavePhoneNumber() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        PhoneNumber phoneNumber = PhoneNumber.builder()
                .additionalInformation("Puhelinvaihde")
                .organization(organization.get())
                .number("62249111")
                .isFinnishServiceNumber(false)
                .prefixNumber("+358")
                .language("FI")
                .chargeDescription("Chargeable")
                .serviceChargeType("charge").build();
        catalogService.savePhoneNumber(phoneNumber);
        Optional<Organization> foundOrganization = catalogService.getOrganization("abcdef123456");
        assertEquals(1, foundOrganization.get().getAllPhoneNumbers().size());
        assertEquals("62249111", foundOrganization.get().getAllPhoneNumbers().iterator().next().getNumber());
        assertEquals(false, foundOrganization.get().getAllPhoneNumbers().iterator().next().getIsFinnishServiceNumber());
        verifySavedStatusInfo(foundOrganization.get().getAllPhoneNumbers().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveWebPage() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        WebPage webPage = WebPage.builder()
                .language("fi").url("https://www.vaasa.fi/").value("Vaasa").organization(organization.get()).build();
        catalogService.saveWebPage(webPage);
        Optional<Organization> foundOrganization = catalogService.getOrganization("abcdef123456");
        assertEquals(1, foundOrganization.get().getAllWebPages().size());
        assertEquals("fi", foundOrganization.get().getAllWebPages().iterator().next().getLanguage());
        assertEquals("https://www.vaasa.fi/", foundOrganization.get().getAllWebPages().iterator().next().getUrl());
        assertEquals("Vaasa", foundOrganization.get().getAllWebPages().iterator().next().getValue());
        verifySavedStatusInfo(foundOrganization.get().getAllWebPages().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveAddress() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        Address address = Address.builder()
                .country("UK").organization(organization.get()).type("Postal").subType("Street").build();
        Address savedAddress = catalogService.saveAddress(address);
        assertNotNull(savedAddress);
        assertEquals("UK", savedAddress.getCountry());
        assertEquals("Postal", savedAddress.getType());
        assertEquals("Street", savedAddress.getSubType());
        verifySavedStatusInfo(savedAddress.getStatusInfo());
    }

    @Test
    public void testSaveStreetAddress() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        StreetAddress streetAddress = StreetAddress.builder()
                .postalCode("12345").longitude("23").latitude("59").coordinateState("Ok")
                .streetNumber("100").address(organization.get().getAllAddresses().iterator().next()).build();
        StreetAddress savedStreetAddress = catalogService.saveStreetAddress(streetAddress);
        assertNotNull(savedStreetAddress);
        assertEquals("12345", savedStreetAddress.getPostalCode());
        assertEquals("23", savedStreetAddress.getLongitude());
        assertEquals("59", savedStreetAddress.getLatitude());
        assertEquals("100", savedStreetAddress.getStreetNumber());
        verifySavedStatusInfo(savedStreetAddress.getStatusInfo());
    }

    @Test
    public void testSaveStreet() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        Street street = Street.builder()
                .streetAddress(organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().iterator().next())
                .language("fi").value("Motellikuja").build();
        Street savedStreet = catalogService.saveStreet(street);
        assertNotNull(savedStreet);
        assertEquals("fi", savedStreet.getLanguage());
        assertEquals("Motellikuja", savedStreet.getValue());
        verifySavedStatusInfo(savedStreet.getStatusInfo());
    }

    @Test
    public void testSaveStreetAddressPostOffice() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        StreetAddressPostOffice streetAddressPostOffice = StreetAddressPostOffice.builder()
                .streetAddress(organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().iterator().next())
                .language("fi").value("NIVALA").build();
        StreetAddressPostOffice savedStreetAddressPostOffice = catalogService.saveStreetAddressPostOffice(streetAddressPostOffice);
        assertNotNull(savedStreetAddressPostOffice);
        assertEquals("fi", savedStreetAddressPostOffice.getLanguage());
        assertEquals("NIVALA", savedStreetAddressPostOffice.getValue());
        verifySavedStatusInfo(savedStreetAddressPostOffice.getStatusInfo());
    }

    @Test
    public void testSaveStreetAddressAdditionalInformation() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        StreetAddressAdditionalInformation addressAdditionalInformation = StreetAddressAdditionalInformation.builder()
                .streetAddress(organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().iterator().next())
                .language("fi").value("Kaupungintalo/kaupunginjohtaja").build();
        StreetAddressAdditionalInformation savedStreetAddressAdditionalInformation =
                catalogService.saveStreetAddressAdditionalInformation(addressAdditionalInformation);
        assertNotNull(savedStreetAddressAdditionalInformation);
        assertEquals("fi", savedStreetAddressAdditionalInformation.getLanguage());
        assertEquals("Kaupungintalo/kaupunginjohtaja", savedStreetAddressAdditionalInformation.getValue());
        verifySavedStatusInfo(savedStreetAddressAdditionalInformation.getStatusInfo());
    }

    @Test
    public void testSaveStreetAddressMunicipality() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        StreetAddressMunicipality streetAddressMunicipality = StreetAddressMunicipality.builder()
                .streetAddress(organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().iterator().next())
                .code("999").build();
        StreetAddressMunicipality savedStreetAddressMunicipality = catalogService.saveStreetAddressMunicipality(streetAddressMunicipality);
        assertNotNull(savedStreetAddressMunicipality);
        assertEquals("999", savedStreetAddressMunicipality.getCode());
        verifySavedStatusInfo(savedStreetAddressMunicipality.getStatusInfo());
    }

    @Test
    public void testSaveStreetAddressMunicipalityName() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        StreetAddressMunicipalityName streetAddressMunicipalityName = StreetAddressMunicipalityName.builder()
                .streetAddressMunicipality(organization.get().getAllAddresses().iterator().next().getAllStreetAddresses()
                        .iterator().next().getAllMunicipalities().iterator().next())
                .language("fi").value("Nivala").build();
        StreetAddressMunicipalityName savedStreetAddressMunicipalityName =
                catalogService.saveStreetAddressMunicipalityName(streetAddressMunicipalityName);
        assertNotNull(savedStreetAddressMunicipalityName);
        assertEquals("fi", savedStreetAddressMunicipalityName.getLanguage());
        assertEquals("Nivala", savedStreetAddressMunicipalityName.getValue());
        verifySavedStatusInfo(savedStreetAddressMunicipalityName.getStatusInfo());
    }

    @Test
    public void testSavePostOfficeBoxAddress() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        PostOfficeBoxAddress postOfficeBoxAddress = PostOfficeBoxAddress.builder()
                .postalCode("9876").address(organization.get().getAllAddresses().iterator().next()).build();
        PostOfficeBoxAddress savedPostOfficeBoxAddress = catalogService.savePostOfficeBoxAddress(postOfficeBoxAddress);
        assertNotNull(savedPostOfficeBoxAddress);
        assertEquals("9876", savedPostOfficeBoxAddress.getPostalCode());
        verifySavedStatusInfo(savedPostOfficeBoxAddress.getStatusInfo());
    }

    @Test
    public void testSavePostOffice() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().size());
        PostOffice postOffice = PostOffice.builder()
                .postOfficeBoxAddress(organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().iterator().next())
                .language("FI").value("Posti").build();
        PostOffice savedPostOffice = catalogService.savePostOffice(postOffice);
        assertNotNull(savedPostOffice);
        assertEquals("FI", savedPostOffice.getLanguage());
        assertEquals("Posti", savedPostOffice.getValue());
        verifySavedStatusInfo(savedPostOffice.getStatusInfo());
    }

    @Test
    public void testSavePostOfficeBox() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().size());
        PostOfficeBox postOfficeBox = PostOfficeBox.builder()
                .postOfficeBoxAddress(organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().iterator().next())
                .language("FI").value("NIVALA").build();
        PostOfficeBox savedPostOfficeBox = catalogService.savePostOfficeBox(postOfficeBox);
        assertNotNull(savedPostOfficeBox);
        assertEquals("FI", savedPostOfficeBox.getLanguage());
        assertEquals("NIVALA", savedPostOfficeBox.getValue());
        verifySavedStatusInfo(savedPostOfficeBox.getStatusInfo());
    }

    @Test
    public void testSavePostOfficeBoxAddressAdditionalInformation() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().size());
        PostOfficeBoxAddressAdditionalInformation addressAdditionalInformation = PostOfficeBoxAddressAdditionalInformation.builder()
                .postOfficeBoxAddress(organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().iterator().next())
                .language("fi").value("Kaupungintalo/kaupunginjohtaja").build();
        PostOfficeBoxAddressAdditionalInformation savedAdditionalInformation =
                catalogService.savePostOfficeBoxAddressAdditionalInformation(addressAdditionalInformation);
        assertNotNull(savedAdditionalInformation);
        assertEquals("fi", savedAdditionalInformation.getLanguage());
        assertEquals("Kaupungintalo/kaupunginjohtaja", savedAdditionalInformation.getValue());
        verifySavedStatusInfo(savedAdditionalInformation.getStatusInfo());
    }

    @Test
    public void testSavePostOfficeBoxAddressMunicipality() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        PostOfficeBoxAddressMunicipality postOfficeBoxAddressMunicipality = PostOfficeBoxAddressMunicipality.builder()
                .postOfficeBoxAddress(organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().iterator().next())
                .code("222").build();
        PostOfficeBoxAddressMunicipality savedPostOfficeBoxAddressMunicipality =
                catalogService.savePostOfficeBoxAddressMunicipality(postOfficeBoxAddressMunicipality);
        assertNotNull(savedPostOfficeBoxAddressMunicipality);
        assertEquals("222", savedPostOfficeBoxAddressMunicipality.getCode());
        verifySavedStatusInfo(savedPostOfficeBoxAddressMunicipality.getStatusInfo());
    }

    @Test
    public void testSavePostOfficeBoxMunicipalityName() {
        Optional<Organization> organization = catalogService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().size());
        PostOfficeBoxAddressMunicipalityName postOfficeBoxAddressMunicipalityName = PostOfficeBoxAddressMunicipalityName.builder()
                .postOfficeBoxAddressMunicipality(organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses()
                        .iterator().next().getAllMunicipalities().iterator().next())
                .language("fi").value("Nivala").build();
        PostOfficeBoxAddressMunicipalityName savedPostOfficeBoxMunicipalityName =
                catalogService.savePostOfficeBoxAddressMunicipalityName(postOfficeBoxAddressMunicipalityName);
        assertNotNull(savedPostOfficeBoxMunicipalityName);
        assertEquals("fi", savedPostOfficeBoxMunicipalityName.getLanguage());
        assertEquals("Nivala", savedPostOfficeBoxMunicipalityName.getValue());
        verifySavedStatusInfo(savedPostOfficeBoxMunicipalityName.getStatusInfo());
    }

    @Test
    public void testEntityTreesFetchedCorrectly() throws InterruptedException {
        assertEntityTreeFetchedCorrectly(catalogService.getAllMembers());
        assertEntityTreeFetchedCorrectly(catalogService.getActiveMembers());
        LocalDateTime modifiedSince1800 = LocalDateTime.of(1800, 1, 1, 0, 0);
        assertEntityTreeFetchedCorrectly(catalogService.getAllMembers(modifiedSince1800));
        assertEntityTreeFetchedCorrectly(catalogService.getActiveMembers(modifiedSince1800));
    }

    private void assertEntityTreeFetchedCorrectly(Iterable<Member> members) {
        log.info("members loaded, detaching");
        for (Member m: members) {
            testUtil.entityManagerDetach(m);
        }
        log.info("all members detached");
        testUtil.entityManagerClear();
        // member - subsystem - service should be fetched
        // service - wdsl should also be fetched
        // wsdl.data should not be fetched but this would require hibernate
        // bytecode enhancement, and is too much of a pain
        Member m = (Member) testUtil.getEntity(members, 1L).get();
        assertNotNull(m);
        Subsystem ss = (Subsystem) testUtil.getEntity(m.getAllSubsystems(), 1L).get();
        assertNotNull(ss);
        Service s = (Service) testUtil.getEntity(ss.getAllServices(), 2L).get();
        assertNotNull(s);
        Wsdl wsdl = s.getWsdl();
        assertNotNull(wsdl);
        assertNotNull(wsdl.getData());
    }

    @Test
    public void testInsertNewMemberAndSubsystems() {

        assertMemberAndSubsystemCounts(TEST_DATA_MEMBERS,
                TEST_DATA_ACTIVE_MEMBERS,
                TEST_DATA_SUBSYSTEMS,
                TEST_DATA_ACTIVE_SUBSYSTEMS);

        Member fooMember = new Member("dev-cs", "PUB", "333111", "UnitTestMember");
        Subsystem subsystem1 = new Subsystem(null, "subsystem1");
        Subsystem subsystem2 = new Subsystem(null, "subsystem2");
        fooMember.setSubsystems(Sets.newHashSet(subsystem1, subsystem2));
        subsystem1.setMember(fooMember);
        subsystem2.setMember(fooMember);
        List<Member> members = Lists.newArrayList(fooMember);
        catalogService.saveAllMembersAndSubsystems(members);

        assertMemberAndSubsystemCounts(TEST_DATA_MEMBERS + 1,
                1, TEST_DATA_SUBSYSTEMS + 2, 2);
    }


    @Test
    public void testInsertMultipleMembersAndSubsystems() {
        assertMemberAndSubsystemCounts(TEST_DATA_MEMBERS,
                TEST_DATA_ACTIVE_MEMBERS,
                TEST_DATA_SUBSYSTEMS,
                TEST_DATA_ACTIVE_SUBSYSTEMS);

        int subsystemsPerMember = 3;
        List<Member> members = Lists.newArrayList();
        members.add(testUtil.createTestMember("200", subsystemsPerMember));
        members.add(testUtil.createTestMember("201", subsystemsPerMember));
        members.add(testUtil.createTestMember("202", subsystemsPerMember));
        catalogService.saveAllMembersAndSubsystems(members);
        int createdMembers = 3;
        int createdSubsystems = createdMembers * subsystemsPerMember;

        assertMemberAndSubsystemCounts(TEST_DATA_MEMBERS + createdMembers,
                createdMembers, TEST_DATA_SUBSYSTEMS + createdSubsystems, createdSubsystems);
    }



    private void assertMemberAndSubsystemCounts(int members, int activeMembers, int subsystems, int activeSubsystems) {
        assertEquals(members, Iterables.size(catalogService.getAllMembers()));
        assertEquals(activeMembers, Iterables.size(catalogService.getActiveMembers()));
        assertEquals(subsystems, Iterables.size(subsystemRepository.findAll()));
        assertEquals(activeSubsystems, StreamSupport.stream(subsystemRepository.findAll().spliterator(), false)
                .filter(s -> !s.getStatusInfo().isRemoved())
                .count());
    }

    @Test
    public void testMemberIsChangedOnlyWhenNameIsChanged() {
        Member member1 = memberRepository.findOne(1L);
        LocalDateTime changed = member1.getStatusInfo().getChanged();

        String oldName = "Nahka-Albert";
        String modifiedName = "Viskoosi-Jooseppi";
        Member updateToSameName = new Member();
        updateToSameName.setXRoadInstance(member1.getXRoadInstance());
        updateToSameName.setMemberClass(member1.getMemberClass());
        updateToSameName.setMemberCode(member1.getMemberCode());
        updateToSameName.setName(oldName);
        updateToSameName.setSubsystems(new HashSet<>());
        catalogService.saveAllMembersAndSubsystems(Arrays.asList(updateToSameName));

        Member member2 = memberRepository.findOne(1L);
        assertEquals(changed, member2.getStatusInfo().getChanged());

        Member updateToDifferentName = new Member();
        updateToDifferentName.setXRoadInstance(member1.getXRoadInstance());
        updateToDifferentName.setMemberClass(member1.getMemberClass());
        updateToDifferentName.setMemberCode(member1.getMemberCode());
        updateToDifferentName.setName(modifiedName);
        updateToDifferentName.setSubsystems(new HashSet<>());
        catalogService.saveAllMembersAndSubsystems(Arrays.asList(updateToDifferentName));

        Member member3 = memberRepository.findOne(1L);
        assertNotEquals(changed, member3.getStatusInfo().getChanged());
    }

    @Test
    public void testGetMember() {
        Member member = memberRepository.findOne(1L);
        Member foundMember = catalogService.getMember(member.getXRoadInstance(),
                member.getMemberClass(), member.getMemberCode());
        assertNotNull(foundMember);
    }

    @Test
    public void testGetActiveMembersSince() {
        // all non-deleted members that contain parts that were modified since 1.1.2007 (3-7)
        Iterable<Member> members = catalogService.getActiveMembers(testUtil.createDate(1, 1, 2017));
        log.info("found members: " + testUtil.getIds(members));
        assertEquals(Arrays.asList(3L, 4L, 5L, 6L, 7L),
                new ArrayList<>(testUtil.getIds(members)));
    }

    @Test
    public void testGetAllMembersSince() {
        // all members that contain parts that were modified since 1.1.2007 (3-8)
        Iterable<Member> members = catalogService.getAllMembers(testUtil.createDate(1, 1, 2017));
        log.info("found members: " + testUtil.getIds(members));
        assertEquals(Arrays.asList(3L, 4L, 5L, 6L, 7L, 8L),
                new ArrayList<Long>(testUtil.getIds(members)));
    }

    @Test
    public void testGetAllMembers() {
        Iterable<Member> members = catalogService.getAllMembers();
        assertEquals(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L),
                new ArrayList<Long>(testUtil.getIds(members)));
    }

    @Test
    public void testGetActiveMembers() {
        Iterable<Member> members = catalogService.getActiveMembers();
        assertEquals(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L),
                new ArrayList<Long>(testUtil.getIds(members)));
    }

    @Test
    public void testSaveUnmodifiedServices() {
        // test data:
        // member (7) -> subsystem (8) -> service (6) -> wsdl (4)
        // member (7) -> subsystem (8) -> service (8, removed) -> wsdl (6)
        // member (7) -> subsystem (8) -> service (9, removed) -> wsdl (7, removed)
        Subsystem originalSub = subsystemRepository.findOne(8L);
        Member originalMember = originalSub.getMember();
        Service originalService5 = serviceRepository.findOne(5L);
        Service originalService6 = serviceRepository.findOne(6L);
        Service originalRemovedService8 = serviceRepository.findOne(8L);
        Service originalRemovedService9 = serviceRepository.findOne(9L);
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        Subsystem savedSub = new Subsystem();
        testUtil.shallowCopyFields(originalSub, savedSub);
        savedSub.setMember(originalMember);
        Service savedService5 = new Service();
        Service savedService6 = new Service();
        testUtil.shallowCopyFields(originalService5, savedService5);
        testUtil.shallowCopyFields(originalService6, savedService6);

        catalogService.saveServices(savedSub.createKey(), Lists.newArrayList(savedService5, savedService6));
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        // read back and verify
        Subsystem checkedSub = subsystemRepository.findOne(8L);
        testUtil.assertAllSame(originalSub.getStatusInfo(), checkedSub.getStatusInfo());
        Service checkedService5 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 5L).get();
        Service checkedService6 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 6L).get();
        Service checkedService8 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 8L).get();
        Service checkedService9 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 9L).get();

        assertFalse(checkedService5.getStatusInfo().isRemoved());
        assertFalse(checkedService6.getStatusInfo().isRemoved());
        assertTrue(checkedService8.getStatusInfo().isRemoved());
        assertTrue(checkedService9.getStatusInfo().isRemoved());
        testUtil.assertFetchedIsOnlyDifferent(originalService5.getStatusInfo(), checkedService5.getStatusInfo());
        testUtil.assertFetchedIsOnlyDifferent(originalService6.getStatusInfo(), checkedService6.getStatusInfo());
        testUtil.assertAllSame(originalRemovedService8.getStatusInfo(), checkedService8.getStatusInfo());
        testUtil.assertAllSame(originalRemovedService9.getStatusInfo(), checkedService9.getStatusInfo());
    }

    @Test
    public void testSaveAddedNewServiceVersions() {
        // test data:
        // member (7) -> subsystem (8) -> service (6) -> wsdl (4)
        Subsystem originalSub = subsystemRepository.findOne(8L);
        Member originalMember = originalSub.getMember();
        Service originalService6 = serviceRepository.findOne(6L);
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        Subsystem savedSub = new Subsystem();
        testUtil.shallowCopyFields(originalSub, savedSub);
        savedSub.setMember(originalMember);
        Service savedService6 = new Service();
        testUtil.shallowCopyFields(originalService6, savedService6);
        Service savedService6newVersion = new Service();
        Service savedService6nullVersion = new Service();
        testUtil.shallowCopyFields(originalService6, savedService6newVersion);
        testUtil.shallowCopyFields(originalService6, savedService6nullVersion);
        savedService6newVersion.setServiceVersion(savedService6.getServiceVersion() + "-new");
        savedService6nullVersion.setServiceVersion(null);

        catalogService.saveServices(savedSub.createKey(), Lists.newArrayList(savedService6, savedService6newVersion,
                savedService6nullVersion));
        testUtil.entityManagerFlush();
        long newVersionId = savedService6newVersion.getId();
        long nullVersionId = savedService6nullVersion.getId();
        testUtil.entityManagerClear();

        // read back and verify
        Subsystem checkedSub = subsystemRepository.findOne(8L);
        assertEquals(3, checkedSub.getActiveServices().size());
        testUtil.assertAllSame(originalSub.getStatusInfo(), checkedSub.getStatusInfo());
        Service checkedService6 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 6L).get();
        Service checkedService6newVersion = (Service) testUtil.getEntity(checkedSub.getAllServices(), newVersionId)
                .get();
        Service checkedService6nullVersion = (Service) testUtil.getEntity(checkedSub.getAllServices(), nullVersionId)
                .get();

        assertFalse(checkedService6.getStatusInfo().isRemoved());
        testUtil.assertFetchedIsOnlyDifferent(originalService6.getStatusInfo(), checkedService6.getStatusInfo());
        assertNewService(checkedService6newVersion);
        assertNewService(checkedService6nullVersion);
    }


    @Test
    public void testSaveAddedServices() {
        // test data:
        // member (7) -> subsystem (8) -> service (6) -> wsdl (4)
        // member (7) -> subsystem (8) -> service (8, removed) -> wsdl (6)
        // member (7) -> subsystem (8) -> service (9, removed) -> wsdl (7, removed)
        Subsystem originalSub = subsystemRepository.findOne(8L);
        Member originalMember = originalSub.getMember();
        Service originalService5 = serviceRepository.findOne(5L);
        Service originalService6 = serviceRepository.findOne(6L);
        Service originalRemovedService8 = serviceRepository.findOne(8L);
        Service originalRemovedService9 = serviceRepository.findOne(9L);
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        Subsystem savedSub = new Subsystem();
        testUtil.shallowCopyFields(originalSub, savedSub);
        savedSub.setMember(originalMember);
        Service savedService5 = new Service();
        Service savedService6 = new Service();
        testUtil.shallowCopyFields(originalService5, savedService5);
        testUtil.shallowCopyFields(originalService6, savedService6);
        Service newService = new Service();
        newService.setServiceCode("foocode-asddsa-ads");
        newService.setServiceVersion("v6");
        Service newServiceNullVersion = new Service();
        newServiceNullVersion.setServiceCode("foocode-asddsa-ads-null");
        newServiceNullVersion.setServiceVersion(null);
        Service newServiceEmptyVersion = new Service();
        newServiceEmptyVersion.setServiceCode("foocode-asddsa-ads-empty");
        newServiceEmptyVersion.setServiceVersion("");

        catalogService.saveServices(savedSub.createKey(),
                Lists.newArrayList(savedService5, savedService6, newService,
                        newServiceNullVersion, newServiceEmptyVersion));
        testUtil.entityManagerFlush();
        long newId = newService.getId();
        long newIdNull = newServiceNullVersion.getId();
        long newIdEmpty = newServiceEmptyVersion.getId();
        testUtil.entityManagerClear();

        // read back and verify
        Subsystem checkedSub = subsystemRepository.findOne(8L);
        testUtil.assertAllSame(originalSub.getStatusInfo(), checkedSub.getStatusInfo());
        Service checkedService5 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 5L).get();
        Service checkedService6 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 6L).get();
        Service checkedService8 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 8L).get();
        Service checkedService9 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 9L).get();
        Service checkedNewService = (Service) testUtil.getEntity(checkedSub.getAllServices(), newId).get();
        Service checkedNewServiceNull = (Service) testUtil.getEntity(checkedSub.getAllServices(), newIdNull).get();
        Service checkedNewServiceEmpty = (Service) testUtil.getEntity(checkedSub.getAllServices(), newIdEmpty).get();

        assertNewService(checkedNewService);
        assertNewService(checkedNewServiceNull);
        assertNewService(checkedNewServiceEmpty);

        assertFalse(checkedService5.getStatusInfo().isRemoved());
        assertFalse(checkedService6.getStatusInfo().isRemoved());
        assertTrue(checkedService8.getStatusInfo().isRemoved());
        assertTrue(checkedService9.getStatusInfo().isRemoved());
        testUtil.assertFetchedIsOnlyDifferent(originalService5.getStatusInfo(), checkedService5.getStatusInfo());
        testUtil.assertFetchedIsOnlyDifferent(originalService6.getStatusInfo(), checkedService6.getStatusInfo());
        testUtil.assertAllSame(originalRemovedService8.getStatusInfo(), checkedService8.getStatusInfo());
        testUtil.assertAllSame(originalRemovedService9.getStatusInfo(), checkedService9.getStatusInfo());
    }

    private void assertNewService(Service checkedNewService) {
        assertFalse(checkedNewService.getStatusInfo().isRemoved());
        assertNotNull(checkedNewService.getStatusInfo().getFetched());
        assertNotNull(checkedNewService.getStatusInfo().getCreated());
        assertNotNull(checkedNewService.getStatusInfo().getChanged());
        assertNull(checkedNewService.getStatusInfo().getRemoved());
    }

    @Test
    public void testGetService() {
        Service service = serviceRepository.findOne(1L);
        Service foundService = catalogService.getService(service.getSubsystem().getMember().getXRoadInstance(),
                service.getSubsystem().getMember().getMemberClass(),
                service.getSubsystem().getMember().getMemberCode(),
                service.getServiceCode(),
                service.getSubsystem().getSubsystemCode(),
                service.getServiceVersion());
        assertNotNull(foundService);
    }

    @Test
    public void testSaveRemovedServices() {
        // test data:
        // member (7) -> subsystem (8) -> service (6) -> wsdl (4)
        // member (7) -> subsystem (8) -> service (8, removed) -> wsdl (6)
        // member (7) -> subsystem (8) -> service (9, removed) -> wsdl (7, removed)
        // member (7) -> subsystem (8) -> service (10) -> (-)
        Subsystem originalSub = subsystemRepository.findOne(8L);
        Member originalMember = originalSub.getMember();
        Service originalService5 = serviceRepository.findOne(5L);
        Service originalService6 = serviceRepository.findOne(6L);
        Service originalRemovedService8 = serviceRepository.findOne(8L);
        Service originalRemovedService9 = serviceRepository.findOne(9L);
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        // remove all services = save subsystem with empty services-collection
        Subsystem savedSub = new Subsystem();
        testUtil.shallowCopyFields(originalSub, savedSub);
        savedSub.setMember(originalMember);

        catalogService.saveServices(savedSub.createKey(),
                Lists.newArrayList());
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        // read back and verify
        Subsystem checkedSub = subsystemRepository.findOne(8L);
        testUtil.assertAllSame(originalSub.getStatusInfo(), checkedSub.getStatusInfo());

        assertEquals(Arrays.asList(5L, 6L, 8L, 9L, 10L, 11L, 12L),
                new ArrayList<>(testUtil.getIds(checkedSub.getAllServices())));
        assertTrue(checkedSub.getActiveServices().isEmpty());
        Service checkedService5 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 5L).get();
        Service checkedService6 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 6L).get();
        Service checkedService8 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 8L).get();
        Service checkedService9 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 9L).get();

        assertTrue(checkedService5.getStatusInfo().isRemoved());
        assertTrue(checkedService6.getStatusInfo().isRemoved());
        assertTrue(checkedService8.getStatusInfo().isRemoved());
        assertTrue(checkedService9.getStatusInfo().isRemoved());
        testUtil.assertEqualities(originalService5.getStatusInfo(), checkedService5.getStatusInfo(),
                true, false, false, false);
        testUtil.assertEqualities(originalService6.getStatusInfo(), checkedService6.getStatusInfo(),
                true, false, false, false);
        testUtil.assertAllSame(originalRemovedService8.getStatusInfo(), checkedService8.getStatusInfo());
        testUtil.assertAllSame(originalRemovedService9.getStatusInfo(), checkedService9.getStatusInfo());
    }

    @Test
    public void testOverwriteIdenticalWsdl() {
        // "changed" is not updated
        // fetched is updated
        // member (7) -> subsystem (8) -> service (6) -> wsdl (4)
        Wsdl originalWsdl = wsdlRepository.findOne(4L);
        Service originalService = originalWsdl.getService();
        ServiceId originalServiceId = originalWsdl.getService().createKey();
        SubsystemId originalSubsystemId = originalWsdl.getService().getSubsystem().createKey();
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        catalogService.saveWsdl(originalSubsystemId, originalServiceId, originalWsdl.getData());
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        Wsdl checkedWsdl = wsdlRepository.findOne(4L);
        assertEquals(originalWsdl.getExternalId(), checkedWsdl.getExternalId());
        assertEquals(originalWsdl.getData(), checkedWsdl.getData());
        assertEquals(originalWsdl.getExternalId(), checkedWsdl.getExternalId());
        assertEquals(originalWsdl.getService().createKey(), originalServiceId);
        testUtil.assertFetchedIsOnlyDifferent(originalWsdl.getStatusInfo(), checkedWsdl.getStatusInfo());
        testUtil.assertAllSame(originalService.getStatusInfo(), checkedWsdl.getService().getStatusInfo());
    }

    @Test
    public void testOverwriteIdenticalOpenApi() {
        OpenApi originalOpenApi = openApiRepository.findOne(2L);
        Service originalService = originalOpenApi.getService();
        ServiceId originalServiceId = originalOpenApi.getService().createKey();
        SubsystemId originalSubsystemId = originalOpenApi.getService().getSubsystem().createKey();
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        catalogService.saveOpenApi(originalSubsystemId, originalServiceId, originalOpenApi.getData());
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        OpenApi checkedOpenApi = openApiRepository.findOne(2L);
        assertEquals(originalOpenApi.getExternalId(), checkedOpenApi.getExternalId());
        assertEquals(originalOpenApi.getData(), checkedOpenApi.getData());
        assertEquals(originalOpenApi.getExternalId(), checkedOpenApi.getExternalId());
        assertEquals(originalOpenApi.getService().createKey(), originalServiceId);
        testUtil.assertFetchedIsOnlyDifferent(originalOpenApi.getStatusInfo(), checkedOpenApi.getStatusInfo());
        testUtil.assertAllSame(originalService.getStatusInfo(), checkedOpenApi.getService().getStatusInfo());
    }

    @Test
    public void testOverwriteModifiedWsdl() {
        // "changed" is updated
        // fetched is also updated
        // member (7) -> subsystem (8) -> service (6) -> wsdl (4)
        Wsdl originalWsdl = wsdlRepository.findOne(4L);
        Service originalService = originalWsdl.getService();
        ServiceId originalServiceId = originalWsdl.getService().createKey();
        SubsystemId originalSubsystemId = originalWsdl.getService().getSubsystem().createKey();
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        catalogService.saveWsdl(originalSubsystemId, originalServiceId, originalWsdl.getData() + "-modification");
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        Wsdl checkedWsdl = wsdlRepository.findOne(4L);
        assertEquals(originalWsdl.getExternalId(), checkedWsdl.getExternalId());
        assertNotEquals(originalWsdl.getData(), checkedWsdl.getData());
        assertEquals(originalWsdl.getService().createKey(), originalServiceId);
        testUtil.assertEqualities(originalWsdl.getStatusInfo(), checkedWsdl.getStatusInfo(),
                true, false, true, false);
        testUtil.assertAllSame(originalService.getStatusInfo(), checkedWsdl.getService().getStatusInfo());
    }

    @Test
    public void testOverwriteModifiedOpenApi() {
        OpenApi originalOpenApi = openApiRepository.findOne(2L);
        Service originalService = originalOpenApi.getService();
        ServiceId originalServiceId = originalOpenApi.getService().createKey();
        SubsystemId originalSubsystemId = originalOpenApi.getService().getSubsystem().createKey();
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        catalogService.saveOpenApi(originalSubsystemId, originalServiceId, originalOpenApi.getData() + "-modification");
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        OpenApi checkedOpenApi = openApiRepository.findOne(2L);
        assertEquals(originalOpenApi.getExternalId(), checkedOpenApi.getExternalId());
        assertNotEquals(originalOpenApi.getData(), checkedOpenApi.getData());
        assertEquals(originalOpenApi.getService().createKey(), originalServiceId);
        testUtil.assertEqualities(originalOpenApi.getStatusInfo(), checkedOpenApi.getStatusInfo(),
                true, false, true, false);
        testUtil.assertAllSame(originalService.getStatusInfo(), checkedOpenApi.getService().getStatusInfo());
    }

    @Test
    public void testSaveNewWsdl() {
        // member (5) -> subsystem (6) -> service (3) -> wsdl (*new*)
        Service oldService = serviceRepository.findOne(3L);
        ServiceId originalServiceId = oldService.createKey();
        SubsystemId originalSubsystemId = oldService.getSubsystem().createKey();
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        Wsdl newWsdl = new Wsdl();
        final String data = "<testwsdl/>";
        catalogService.saveWsdl(originalSubsystemId, originalServiceId, data);
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        Service checkedService = serviceRepository.findOne(3L);
        Wsdl checkedWsdl = checkedService.getWsdl();
        log.info("externalId [{}]", checkedWsdl.getExternalId());
        assertNotNull(checkedWsdl.getExternalId());
        assertEquals(data, checkedWsdl.getData());
        assertEquals(checkedWsdl.getService().createKey(), originalServiceId);
        assertNotNull(checkedWsdl.getStatusInfo().getCreated());
        assertNotNull(checkedWsdl.getStatusInfo().getChanged());
        assertNotNull(checkedWsdl.getStatusInfo().getFetched());
        assertNull(checkedWsdl.getStatusInfo().getRemoved());
        testUtil.assertAllSame(oldService.getStatusInfo(), checkedWsdl.getService().getStatusInfo());
    }

    @Test
    public void testSaveNewOpenApi() {
        // member (5) -> subsystem (6) -> service (3) -> wsdl (*new*)
        Service oldService = serviceRepository.findOne(12L);
        ServiceId originalServiceId = oldService.createKey();
        SubsystemId originalSubsystemId = oldService.getSubsystem().createKey();
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        OpenApi newOpenApi = new OpenApi();
        final String data = "<testopenapi/>";
        catalogService.saveOpenApi(originalSubsystemId, originalServiceId, data);
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        Service checkedService = serviceRepository.findOne(12L);
        OpenApi checkedOpenApi = checkedService.getOpenApi();
        log.info("externalId [{}]", checkedOpenApi.getExternalId());
        assertNotNull(checkedOpenApi.getExternalId());
        assertEquals(data, checkedOpenApi.getData());
        assertEquals(checkedOpenApi.getService().createKey(), originalServiceId);
        assertNotNull(checkedOpenApi.getStatusInfo().getCreated());
        assertNotNull(checkedOpenApi.getStatusInfo().getChanged());
        assertNotNull(checkedOpenApi.getStatusInfo().getFetched());
        assertNull(checkedOpenApi.getStatusInfo().getRemoved());
        testUtil.assertAllSame(oldService.getStatusInfo(), checkedOpenApi.getService().getStatusInfo());
    }

    @Test
    public void testResurrectWsdl() {
        // member (7) -> subsystem (8) -> service (9, removed) -> wsdl (7, removed)
        Service oldService = serviceRepository.findOne(9L);
        // fix test data so that service is not removed
        oldService.getStatusInfo().setRemoved(null);
        ServiceId originalServiceId = oldService.createKey();
        SubsystemId originalSubsystemId = oldService.getSubsystem().createKey();
        Wsdl originalWsdl = oldService.getWsdl();
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        catalogService.saveWsdl(originalSubsystemId, originalServiceId, originalWsdl.getData());
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        Service checkedService = serviceRepository.findOne(9L);
        Wsdl checkedWsdl = checkedService.getWsdl();
        assertEquals(originalWsdl.getExternalId(), checkedWsdl.getExternalId());
        assertEquals(7L, checkedWsdl.getId());
        assertEquals(originalWsdl.getData(), checkedWsdl.getData());
        assertEquals(checkedWsdl.getService().createKey(), originalServiceId);
        testUtil.assertEqualities(originalWsdl.getStatusInfo(), checkedWsdl.getStatusInfo(),
                true, false, false, false);
        assertNull(checkedWsdl.getStatusInfo().getRemoved());
    }

    @Test
    public void testResurrectOpenApi() {
        // member (7) -> subsystem (8) -> service (9, removed) -> wsdl (7, removed)
        Service oldService = serviceRepository.findOne(11L);
        // fix test data so that service is not removed
        oldService.getStatusInfo().setRemoved(null);
        ServiceId originalServiceId = oldService.createKey();
        SubsystemId originalSubsystemId = oldService.getSubsystem().createKey();
        OpenApi originalOpenApi = oldService.getOpenApi();
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        catalogService.saveOpenApi(originalSubsystemId, originalServiceId, originalOpenApi.getData());
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        Service checkedService = serviceRepository.findOne(11L);
        OpenApi checkedOpenApi = checkedService.getOpenApi();
        assertEquals(originalOpenApi.getExternalId(), checkedOpenApi.getExternalId());
        assertEquals(1L, checkedOpenApi.getId());
        assertEquals(originalOpenApi.getData(), checkedOpenApi.getData());
        assertEquals(checkedOpenApi.getService().createKey(), originalServiceId);
        testUtil.assertEqualities(originalOpenApi.getStatusInfo(), checkedOpenApi.getStatusInfo(),
                true, false, false, false);
        assertNull(checkedOpenApi.getStatusInfo().getRemoved());
    }

    @Test
    public void testSaveWsdlFailsForRemovedService() {
        // member (7) -> subsystem (8) -> service (9, removed) -> wsdl (7, removed)
        Service oldService = serviceRepository.findOne(9L);
        ServiceId originalServiceId = oldService.createKey();
        SubsystemId originalSubsystemId = oldService.getSubsystem().createKey();
        Wsdl originalWsdl = oldService.getWsdl();
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        try {
            catalogService.saveWsdl(originalSubsystemId, originalServiceId, originalWsdl.getData());
            fail("should have throw exception since service is removed");
        } catch (Exception expected) {
            // Exception is expected }
        }
    }

    @Test
    public void testSaveOpenApiFailsForRemovedService() {
        // member (7) -> subsystem (8) -> service (9, removed) -> wsdl (7, removed)
        Service oldService = serviceRepository.findOne(9L);
        ServiceId originalServiceId = oldService.createKey();
        SubsystemId originalSubsystemId = oldService.getSubsystem().createKey();
        OpenApi originalOpenApi = oldService.getOpenApi();
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        try {
            catalogService.saveWsdl(originalSubsystemId, originalServiceId, originalOpenApi.getData());
            fail("should have throw exception since service is removed");
        } catch (Exception expected) {
            // Exception is expected }
        }
    }

    private void verifySavedStatusInfo(StatusInfo statusInfo) {
        assertNotNull(statusInfo.getCreated());
        assertNotNull(statusInfo.getChanged());
        assertNotNull(statusInfo.getFetched());
        assertNull(statusInfo.getRemoved());
    }
}
