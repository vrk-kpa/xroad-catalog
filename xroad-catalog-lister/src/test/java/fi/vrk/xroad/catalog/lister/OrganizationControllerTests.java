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

import fi.vrk.xroad.catalog.persistence.entity.Address;
import fi.vrk.xroad.catalog.persistence.entity.BusinessAddress;
import fi.vrk.xroad.catalog.persistence.entity.BusinessAuxiliaryName;
import fi.vrk.xroad.catalog.persistence.entity.BusinessIdChange;
import fi.vrk.xroad.catalog.persistence.entity.BusinessLine;
import fi.vrk.xroad.catalog.persistence.entity.BusinessName;
import fi.vrk.xroad.catalog.persistence.entity.Company;
import fi.vrk.xroad.catalog.persistence.entity.CompanyForm;
import fi.vrk.xroad.catalog.persistence.entity.ContactDetail;
import fi.vrk.xroad.catalog.persistence.entity.Email;
import fi.vrk.xroad.catalog.persistence.entity.Language;
import fi.vrk.xroad.catalog.persistence.entity.Liquidation;
import fi.vrk.xroad.catalog.persistence.entity.Organization;
import fi.vrk.xroad.catalog.persistence.entity.OrganizationDescription;
import fi.vrk.xroad.catalog.persistence.entity.OrganizationName;
import fi.vrk.xroad.catalog.persistence.entity.PhoneNumber;
import fi.vrk.xroad.catalog.persistence.entity.RegisteredEntry;
import fi.vrk.xroad.catalog.persistence.entity.RegisteredOffice;
import fi.vrk.xroad.catalog.persistence.entity.StatusInfo;
import fi.vrk.xroad.catalog.persistence.entity.WebPage;
import fi.vrk.xroad.catalog.persistence.repository.CompanyRepository;
import fi.vrk.xroad.catalog.persistence.repository.OrganizationRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = ListerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"xroad-catalog.shared-params-file=src/test/resources/shared-params.xml"})
@ActiveProfiles({"default","fi"})
public class OrganizationControllerTests {

    private static final String organizationBusinessCode = "0123456-9";
    private static final String organizationGuid = "abcdef123456";
    private static final String organizationType = "Municipality";
    private static final String organizationPublishingStatus = "Published";
    private static final String companyBusinessId = "1710128-9";
    private static final String companyForm = "OYJ";
    private static final String companyName = "Gofore Oyj";

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    OrganizationRepository organizationRepository;

    @MockBean
    CompanyRepository companyRepository;

    @Test
    public void testGetOrganization() throws JSONException {
        testGetOrganizationWithOrganizationData();
        testGetOrganizationWithCompanyData();

        // Get Organization not found
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganization/0-12345", String.class);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testGetOrganizationChanges() throws JSONException {
        // Get OrganizationChanges for CompanyData older values
        mockOrganizationWithCompanyData(companyBusinessId);
        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/getOrganizationChanges/" + companyBusinessId +"?startDate=2010-01-01&endDate=2022-01-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONArray changedValues = json.optJSONArray("changedValueList");
        assertTrue(json.optBoolean("changed"));
        assertEquals(12, changedValues.length());

        // Get OrganizationChanges for OrganizationData older values
        mockOrganizationWithOrganizationData(organizationBusinessCode);
        response = restTemplate
                .getForEntity("/api/getOrganizationChanges/" + organizationBusinessCode + "?startDate=2010-01-01&endDate=2022-01-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        changedValues = json.optJSONArray("changedValueList");
        assertTrue(json.optBoolean("changed"));
        assertEquals(7, changedValues.length());

        // Get OrganizationChanges for CompanyData newer values
        response = restTemplate
                .getForEntity("/api/getOrganizationChanges/" + companyBusinessId +"?startDate=2021-01-01&endDate=2022-01-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        changedValues = json.optJSONArray("changedValueList");
        assertTrue(json.optBoolean("changed"));
        assertEquals(1, changedValues.length());

        // Get OrganizationChanges for OrganizationData newer values
        response = restTemplate
                .getForEntity("/api/getOrganizationChanges/" + organizationBusinessCode + "?startDate=2021-01-01&endDate=2022-01-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        changedValues = json.optJSONArray("changedValueList");
        assertTrue(json.optBoolean("changed"));
        assertEquals(1, changedValues.length());

        // Get OrganizationChanges when date parameter in wrong format
        response = restTemplate
                .getForEntity("/api/getOrganizationChanges/" + organizationBusinessCode + "?startDate=01-01-2022&endDate=2022-06-01", String.class);
        assertEquals(400, response.getStatusCodeValue());

        // Get OrganizationChanges for CompanyData not found
        response = restTemplate
                .getForEntity("/api/getOrganizationChanges/" + companyBusinessId + "?startDate=2022-01-01&endDate=2022-06-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("{\"changed\":false,\"changedValueList\":[]}", response.getBody());

        // Get OrganizationChanges for OrganizationData not found
        response = restTemplate
                .getForEntity("/api/getOrganizationChanges/" + organizationBusinessCode + "?startDate=2022-01-01&endDate=2022-06-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("{\"changed\":false,\"changedValueList\":[]}", response.getBody());

        // Get OrganizationChanges when business code is invalid
        response = restTemplate
                .getForEntity("/api/getOrganizationChanges/" + organizationBusinessCode + "?startDate=2022-01-01&endDate=2022-06-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("{\"changed\":false,\"changedValueList\":[]}", response.getBody());

        // Get OrganizationChanges when dates are null
        response = restTemplate.getForEntity("/api/getOrganizationChanges/" + organizationBusinessCode, String.class);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("{\"changed\":false,\"changedValueList\":[]}", response.getBody());
    }

    private void testGetOrganizationWithOrganizationData() throws JSONException {
        mockOrganizationWithOrganizationData(organizationBusinessCode);
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganization/" + organizationBusinessCode, String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONObject organizationData = json.optJSONObject("organizationData");
        JSONObject companyData = json.optJSONObject("companyData");
        assertNull(companyData);
        assertEquals(14, organizationData.length());
        assertEquals(organizationBusinessCode, organizationData.optString("businessCode"));
        assertEquals(organizationGuid, organizationData.optString("guid"));
        assertEquals(organizationType, organizationData.optString("organizationType"));
        assertEquals(organizationPublishingStatus, organizationData.optString("publishingStatus"));
        assertNotNull(organizationData.opt("changed"));
        assertNotNull(organizationData.opt("created"));
        assertNotNull(organizationData.opt("fetched"));
        assertNotNull(organizationData.opt("removed"));
        assertEquals(1, organizationData.optJSONArray("organizationDescriptions").length());
        assertEquals(1, organizationData.optJSONArray("organizationNames").length());
        assertEquals(1, organizationData.optJSONArray("addresses").length());
        assertEquals(1, organizationData.optJSONArray("emails").length());
        assertEquals(1, organizationData.optJSONArray("phoneNumbers").length());
        assertEquals(1, organizationData.optJSONArray("webPages").length());
    }

    private void testGetOrganizationWithCompanyData() throws JSONException {
        mockOrganizationWithCompanyData(companyBusinessId);
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganization/" + companyBusinessId, String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONObject organizationData = json.optJSONObject("organizationData");
        JSONObject companyData = json.optJSONObject("companyData");
        assertNull(organizationData);
        assertEquals(20, companyData.length());
        assertNotNull(companyData.opt("changed"));
        assertNotNull(companyData.opt("created"));
        assertNotNull(companyData.opt("fetched"));
        assertNotNull(companyData.opt("removed"));
        assertNotNull(companyData.opt("registrationDate"));
        assertEquals(companyBusinessId, companyData.optString("businessCode"));
        assertEquals(companyForm, companyData.optString("companyForm"));
        assertEquals("", companyData.optString("detailsUri"));
        assertEquals(companyName, companyData.optString("name"));
        assertEquals(1, companyData.optJSONArray("businessAddresses").length());
        assertEquals(1, companyData.optJSONArray("businessAuxiliaryNames").length());
        assertEquals(1, companyData.optJSONArray("businessIdChanges").length());
        assertEquals(1, companyData.optJSONArray("businessLines").length());
        assertEquals(1, companyData.optJSONArray("businessNames").length());
        assertEquals(1, companyData.optJSONArray("companyForms").length());
        assertEquals(1, companyData.optJSONArray("contactDetails").length());
        assertEquals(1, companyData.optJSONArray("languages").length());
        assertEquals(1, companyData.optJSONArray("liquidations").length());
        assertEquals(1, companyData.optJSONArray("registeredEntries").length());
        assertEquals(1, companyData.optJSONArray("registeredOffices").length());
    }

    private void mockOrganizationWithOrganizationData(String businessCode) {
        LocalDateTime created = LocalDateTime.of(2010, 1, 10, 10, 10, 10);
        LocalDateTime changed = LocalDateTime.of(2020, 1, 10, 10, 10, 10);
        LocalDateTime fetched = LocalDateTime.of(2020, 1, 10, 10, 10, 10);
        Organization organization = Organization.builder()
                .businessCode(businessCode)
                .organizationType(organizationType)
                .guid(organizationGuid)
                .publishingStatus(organizationPublishingStatus)
                .organizationNames(new HashSet<>(Arrays.asList(OrganizationName.builder()
                        .statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .organizationDescriptions(new HashSet<>(Arrays.asList(OrganizationDescription.builder()
                        .statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .addresses(new HashSet<>(Arrays.asList(Address.builder()
                        .statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .emails(new HashSet<>(Arrays.asList(Email.builder().statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .phoneNumbers(new HashSet<>(Arrays.asList(PhoneNumber.builder()
                        .statusInfo(new StatusInfo(created, changed, fetched, null))
                        .isFinnishServiceNumber(false).build())))
                .webPages(new HashSet<>(Arrays.asList(WebPage.builder().statusInfo(new StatusInfo(created, changed.plusYears(1), fetched, null)).build())))
                .statusInfo(new StatusInfo(created, changed, fetched, null))
                .build();
        Set<Organization> organizations = new HashSet<>(Arrays.asList(organization));
        given(organizationRepository.findAllByBusinessCode(businessCode)).willReturn(organizations);
        given(organizationRepository.findAnyByOrganizationGuid(organizationGuid)).willReturn(Optional.ofNullable(organization));
    }

    private void mockOrganizationWithCompanyData(String businessId) {
        LocalDateTime created = LocalDateTime.of(2010, 1, 10, 10, 10, 10);
        LocalDateTime changed = LocalDateTime.of(2020, 1, 10, 10, 10, 10);
        LocalDateTime fetched = LocalDateTime.of(2020, 1, 10, 10, 10, 10);
        Company company = Company.builder()
                .businessId(businessId)
                .companyForm(companyForm)
                .detailsUri("")
                .name(companyName)
                .statusInfo(new StatusInfo(created, changed, fetched, null))
                .companyForms(new HashSet<>(Arrays.asList(CompanyForm.builder().statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .businessAddresses(new HashSet<>(Arrays.asList(BusinessAddress.builder().statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .businessAuxiliaryNames(new HashSet<>(Arrays.asList(BusinessAuxiliaryName.builder().statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .businessIdChanges(new HashSet<>(Arrays.asList(BusinessIdChange.builder().statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .businessLines(new HashSet<>(Arrays.asList(BusinessLine.builder().statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .businessNames(new HashSet<>(Arrays.asList(BusinessName.builder().statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .contactDetails(new HashSet<>(Arrays.asList(ContactDetail.builder().statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .liquidations(new HashSet<>(Arrays.asList(Liquidation.builder().statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .languages(new HashSet<>(Arrays.asList(Language.builder().statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .registeredOffices(new HashSet<>(Arrays.asList(RegisteredOffice.builder().statusInfo(new StatusInfo(created, changed, fetched, null)).build())))
                .registeredEntries(new HashSet<>(Arrays.asList(RegisteredEntry.builder().statusInfo(new StatusInfo(created, changed.plusYears(1), fetched, null)).build())))
                .build();
        Set<Company> companies = new HashSet<>(Arrays.asList(company));
        given(companyRepository.findAllByBusinessId(businessId)).willReturn(companies);
    }

}
