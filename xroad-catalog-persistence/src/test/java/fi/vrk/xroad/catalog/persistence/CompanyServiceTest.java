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
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.*;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class CompanyServiceTest {

    @Autowired
    CompanyService companyService;

    @Test
    public void testGetCompanies() {
        Iterable<Company> companies = companyService.getCompanies("1710128-9");
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
    public void testSaveCompany() {
        Company company = Company.builder()
                .companyForm("OYJ")
                .businessId("123456789-1")
                .detailsUri("123")
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .name("A company").build();
        Company savedCompany = companyService.saveCompany(company);
        assertNotNull(savedCompany);
        assertNotNull(savedCompany.getId());
        assertEquals("123456789-1", savedCompany.getBusinessId());
        assertEquals("123", savedCompany.getDetailsUri());
        assertEquals("OYJ", savedCompany.getCompanyForm());
        assertEquals("A company", savedCompany.getName());
        assertEquals(LocalDate.of(2020, 4, 30), savedCompany.getRegistrationDate().toLocalDate());
        TestUtil.verifySavedStatusInfo(savedCompany.getStatusInfo());
    }

    @Test
    public void testSaveCompanyWhenAlreadyExists() {
        Company company = Company.builder()
                .companyForm("OYJ")
                .businessId("1710128-9")
                .detailsUri("123")
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .name("Gofore Oyj").build();
        Company savedCompany = companyService.saveCompany(company);
        assertNotNull(savedCompany);
        assertNotNull(savedCompany.getId());
        assertEquals("1710128-9", savedCompany.getBusinessId());
        assertEquals("123", savedCompany.getDetailsUri());
        assertEquals("OYJ", savedCompany.getCompanyForm());
        assertEquals("Gofore Oyj", savedCompany.getName());
        assertEquals(LocalDate.of(2020, 4, 30), savedCompany.getRegistrationDate().toLocalDate());
        TestUtil.verifySavedStatusInfo(savedCompany.getStatusInfo());
    }

    @Test
    public void testSaveBusinessName() {
        Iterable<Company> companies = companyService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        BusinessName businessName = BusinessName.builder()
                .name("").language("FI").ordering(0).source(0).version(0)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        companyService.saveBusinessName(businessName);
        Iterable<Company> foundCompanies = companyService.getCompanies("1710128-9");
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
        TestUtil.verifySavedStatusInfo(companies.iterator().next().getAllBusinessNames().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveBusinessAuxiliaryName() {
        Iterable<Company> companies = companyService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        BusinessAuxiliaryName businessAuxiliaryName = BusinessAuxiliaryName.builder()
                .name("Solinor").language("").ordering(5).source(1).version(1)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        companyService.saveBusinessAuxiliaryName(businessAuxiliaryName);
        Iterable<Company> foundCompanies = companyService.getCompanies("1710128-9");
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
        TestUtil.verifySavedStatusInfo(companies.iterator().next().getAllBusinessAuxiliaryNames().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveBusinessAddress() {
        Iterable<Company> companies = companyService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        BusinessAddress businessAddress = BusinessAddress.builder().careOf("").city("Tampere").country("Finland")
                .language("FI").postCode("30123").source(0).street("Katu 1").type(2).version(1)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        companyService.saveBusinessAddress(businessAddress);
        Iterable<Company> foundCompanies = companyService.getCompanies("1710128-9");
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
        Iterable<Company> companies = companyService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        BusinessIdChange businessIdChange = BusinessIdChange.builder()
                .language("").change("44").description("Change description").reason("Change reason")
                .source(2).oldBusinessId("1796717-0").newBusinessId("1710128-9").changeDate("2020-01-25")
                .company(companies.iterator().next()).build();
        companyService.saveBusinessIdChange(businessIdChange);
        Iterable<Company> foundCompanies = companyService.getCompanies("1710128-9");
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
        TestUtil.verifySavedStatusInfo(companies.iterator().next().getAllBusinessAuxiliaryNames().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveBusinessLine() {
        Iterable<Company> companies = companyService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        BusinessLine businessLine = BusinessLine.builder()
                .name("Dataprogrammering").language("SE").ordering(0).source(2).version(1)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        companyService.saveBusinessLine(businessLine);
        Iterable<Company> foundCompanies = companyService.getCompanies("1710128-9");
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
        TestUtil.verifySavedStatusInfo(companies.iterator().next().getAllBusinessLines().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveCompanyForm() {
        Iterable<Company> companies = companyService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        CompanyForm companyForm = CompanyForm.builder()
                .name("Public limited company").language("EN").source(1).version(1).type(0)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        companyService.saveCompanyForm(companyForm);
        Iterable<Company> foundCompanies = companyService.getCompanies("1710128-9");
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
        TestUtil.verifySavedStatusInfo(companies.iterator().next().getAllCompanyForms().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveContactDetail() {
        Iterable<Company> companies = companyService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        ContactDetail contactDetail = ContactDetail.builder()
                .value("VALUE").language("EN").source(0).version(1).type("0")
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        companyService.saveContactDetail(contactDetail);
        Iterable<Company> foundCompanies = companyService.getCompanies("1710128-9");
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
        TestUtil.verifySavedStatusInfo(companies.iterator().next().getAllContactDetails().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveLanguage() {
        Iterable<Company> companies = companyService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        Language language = Language.builder()
                .name("Finska").language("SE").source(0).version(1)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        companyService.saveLanguage(language);
        Iterable<Company> foundCompanies = companyService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(foundCompanies));
        assertEquals(1, companies.iterator().next().getAllLanguages().size());
        assertEquals("SE", companies.iterator().next().getAllLanguages().iterator().next().getLanguage());
        assertEquals("Finska", companies.iterator().next().getAllLanguages().iterator().next().getName());
        assertEquals(0, companies.iterator().next().getAllLanguages().iterator().next().getSource());
        assertEquals(1, companies.iterator().next().getAllLanguages().iterator().next().getVersion());
        assertEquals(LocalDate.of(2020, 4, 30),
                companies.iterator().next().getAllLanguages().iterator().next().getRegistrationDate().toLocalDate());
        assertNull(companies.iterator().next().getAllLanguages().iterator().next().getEndDate());
        TestUtil.verifySavedStatusInfo(companies.iterator().next().getAllLanguages().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveLiquidation() {
        Iterable<Company> companies = companyService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        Liquidation liquidation = Liquidation.builder()
                .name("Liquidation").language("FI").source(0).version(0).type(1)
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        companyService.saveLiquidation(liquidation);
        Iterable<Company> foundCompanies = companyService.getCompanies("1710128-9");
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
        TestUtil.verifySavedStatusInfo(companies.iterator().next().getAllLiquidations().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveRegisteredEntry() {
        Iterable<Company> companies = companyService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        RegisteredEntry registeredEntry = RegisteredEntry.builder()
                .status(2).authority(2).register(1).description("Unregistered")
                .language("EN").registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        companyService.saveRegisteredEntry(registeredEntry);
        Iterable<Company> foundCompanies = companyService.getCompanies("1710128-9");
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
        TestUtil.verifySavedStatusInfo(companies.iterator().next().getAllRegisteredEntries().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveRegisteredOffice() {
        Iterable<Company> companies = companyService.getCompanies("1710128-9");
        assertEquals(1, Iterables.size(companies));
        RegisteredOffice registeredOffice = RegisteredOffice.builder().source(0).ordering(0)
                .name("Registered Office").version(0).language("FI")
                .registrationDate(LocalDateTime.of(2020, 4, 30, 0, 0 ,0))
                .endDate(null).company(companies.iterator().next()).build();
        companyService.saveRegisteredOffice(registeredOffice);
        Iterable<Company> foundCompanies = companyService.getCompanies("1710128-9");
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
        TestUtil.verifySavedStatusInfo(companies.iterator().next().getAllRegisteredOffices().iterator().next().getStatusInfo());
    }
}
