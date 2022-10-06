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

import fi.vrk.xroad.catalog.persistence.dto.EndpointData;
import fi.vrk.xroad.catalog.persistence.dto.ServiceEndpointsResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ListerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"xroad-catalog.shared-params-file=src/test/resources/shared-params.xml"})
public class ServiceControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void testGetOrganization() throws JSONException {
        testGetOrganizationWithOrganizationData();
        testGetOrganizationWithCompanyData();

        // Get Organization not found
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganization/0-12345", String.class);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    private void testGetOrganizationWithOrganizationData() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganization/0123456-9", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONObject organizationData = json.optJSONObject("organizationData");
        JSONObject companyData = json.optJSONObject("companyData");
        assertNull(companyData);
        assertEquals(14, organizationData.length());
        assertEquals("0123456-9", organizationData.optString("businessCode"));
        assertEquals("abcdef123456", organizationData.optString("guid"));
        assertEquals("Municipality", organizationData.optString("organizationType"));
        assertEquals("Published", organizationData.optString("publishingStatus"));
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
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganization/1710128-9", String.class);
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
        assertEquals("1710128-9", companyData.optString("businessCode"));
        assertEquals("OYJ", companyData.optString("companyForm"));
        assertEquals("", companyData.optString("detailsUri"));
        assertEquals("Gofore Oyj", companyData.optString("name"));
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

    @Test
    public void testGetOrganizationChanges() throws JSONException {
        // Get OrganizationChanges for CompanyData older values
        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/getOrganizationChanges/1710128-9?startDate=2010-01-01&endDate=2022-01-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONArray changedValues = json.optJSONArray("changedValueList");
        assertTrue(json.optBoolean("changed"));
        assertEquals(12, changedValues.length());

        // Get OrganizationChanges for OrganizationData older values
        response = restTemplate
                .getForEntity("/api/getOrganizationChanges/0123456-9?startDate=2010-01-01&endDate=2022-01-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        changedValues = json.optJSONArray("changedValueList");
        assertTrue(json.optBoolean("changed"));
        assertEquals(19, changedValues.length());

        // Get OrganizationChanges for CompanyData newer values
        response = restTemplate
                .getForEntity("/api/getOrganizationChanges/1710128-9?startDate=2020-09-04&endDate=2022-01-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        changedValues = json.optJSONArray("changedValueList");
        assertTrue(json.optBoolean("changed"));
        assertEquals(1, changedValues.length());

        // Get OrganizationChanges for OrganizationData newer values
        response = restTemplate
                .getForEntity("/api/getOrganizationChanges/0123456-9?startDate=2019-12-31&endDate=2022-01-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        changedValues = json.optJSONArray("changedValueList");
        assertTrue(json.optBoolean("changed"));
        assertEquals(1, changedValues.length());

        // Get OrganizationChanges when date parameter in wrong format
        response = restTemplate
                .getForEntity("/api/getOrganizationChanges/0123456-9?startDate=01-01-2022&endDate=2022-06-01", String.class);
        assertEquals(400, response.getStatusCodeValue());

        // Get OrganizationChanges for CompanyData not found
        response = restTemplate
                .getForEntity("/api/getOrganizationChanges/1710128-9?startDate=2022-01-01&endDate=2022-06-01", String.class);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());

        // Get OrganizationChanges for OrganizationData not found
        response = restTemplate
                .getForEntity("/api/getOrganizationChanges/0123456-9?startDate=2022-01-01&endDate=2022-06-01", String.class);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testListErrorsForSubsystem() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/DEV/GOV/1234/TestSubsystem?startDate=2014-01-01&endDate=2022-01-01", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(1, errorList.length());

        for (int i = 0; i < errorList.length(); i++) {
            assertEquals("Service not found", errorList.optJSONObject(i).optString("message"));
            assertEquals("DEV", errorList.optJSONObject(i).optString("xroadInstance"));
            assertEquals("GOV", errorList.optJSONObject(i).optString("memberClass"));
            assertEquals("1234", errorList.optJSONObject(i).optString("memberCode"));
            assertEquals("TestSubsystem", errorList.optJSONObject(i).optString("subsystemCode"));
        }
    }

    @Test
    public void testListErrorsForSubsystemWithPagination() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/DEV/GOV/1234/TestSubsystem?startDate=2014-01-01&endDate=2022-01-01&page=0&limit=100", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(1, errorList.length());

        for (int i = 0; i < errorList.length(); i++) {
            assertEquals("Service not found", errorList.optJSONObject(i).optString("message"));
            assertEquals("DEV", errorList.optJSONObject(i).optString("xroadInstance"));
            assertEquals("GOV", errorList.optJSONObject(i).optString("memberClass"));
            assertEquals("1234", errorList.optJSONObject(i).optString("memberCode"));
            assertEquals("TestSubsystem", errorList.optJSONObject(i).optString("subsystemCode"));
        }
    }

    @Test
    public void testListErrors() throws JSONException {
        // testListErrorsForMemberCode
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/DEV/GOV/1234?startDate=2014-01-01&endDate=2022-01-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(2, errorList.length());

        // testListErrorsForMemberCodeWithPagination
        response =
                restTemplate.getForEntity("/api/listErrors/DEV/GOV/1234?startDate=2014-01-01&endDate=2022-01-01&page=0&limit=100", String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(2, errorList.length());

        // testListErrorsForMemberClass
        response = restTemplate.getForEntity("/api/listErrors/DEV/GOV?startDate=2014-01-01&endDate=2022-01-01", String.class);
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(2, errorList.length());

        // testListErrorsForMemberClassWithPagination
        response =
                restTemplate.getForEntity("/api/listErrors/DEV/GOV/1234?startDate=2014-01-01&endDate=2022-01-01&page=0&limit=100", String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(2, errorList.length());

        // testListErrorsForInstance
        response =
                restTemplate.getForEntity("/api/listErrors/DEV?startDate=2014-01-01&endDate=2022-01-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(3, errorList.length());

        // testListErrorsForInstanceWithPagination
        response =
                restTemplate.getForEntity("/api/listErrors?startDate=2014-01-01&endDate=2022-01-01&page=0&limit=100", String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(6, errorList.length());

        // testListErrorsForAll
        response =
                restTemplate.getForEntity("/api/listErrors?startDate=2014-01-01&endDate=2022-01-01&page=0&limit=100", String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(6, errorList.length());

        // testListErrorsForAllWithPagination
        response =
                restTemplate.getForEntity("/api/listErrors?startDate=2014-01-01&endDate=2022-01-01&page=0&limit=100", String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(6, errorList.length());

        // testListErrorsInvalidDateFormatException
        response =
                restTemplate.getForEntity("/api/listErrors?startDate=01-01-2014&endDate=2022-01-01", String.class);
        assertEquals(400, response.getStatusCodeValue());

        // testListErrorsNotFoundException
        response =
                restTemplate.getForEntity("/api/listErrors?startDate=2022-01-01&endDate=2022-06-01", String.class);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testGetDistinctServiceStatistics() throws JSONException {
        // testGetDistinctServiceStatistics
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getDistinctServiceStatistics?startDate=2014-01-01&endDate=2023-01-01", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray serviceStatisticsList = json.getJSONArray("distinctServiceStatisticsList");
        assertEquals(3288, serviceStatisticsList.length());

        for (int i = 0; i < serviceStatisticsList.length(); i++) {
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfDistinctServices") > 0);
        }

        // testGetDistinctServiceStatisticsInvalidDateFormatException
        response =
                restTemplate.getForEntity("/api/getDistinctServiceStatistics?startDate=01-01-2014&endDate=2022-01-01", String.class);
        assertEquals(400, response.getStatusCodeValue());

        // testGetDistinctServiceStatisticsNotFoundException
        response =
                restTemplate.getForEntity("/api/getDistinctServiceStatistics?startDate=2023-01-01&endDate=2023-06-01", String.class);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testGetServiceStatistics() throws JSONException {
        // testGetServiceStatistics
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatistics?startDate=2014-01-01&endDate=2023-01-01", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray serviceStatisticsList = json.getJSONArray("serviceStatisticsList");
        assertEquals(3288, serviceStatisticsList.length());

        for (int i = 0; i < serviceStatisticsList.length(); i++) {
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfSoapServices") > 0);
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfRestServices") > 0);
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfOpenApiServices") > 0);
        }

        // testGetServiceStatisticsInvalidDateFormatException
        response =
                restTemplate.getForEntity("/api/getServiceStatistics?startDate=01-01-2014&endDate=2022-01-01", String.class);
        assertEquals(400, response.getStatusCodeValue());

        // testGetServiceStatisticsCSV
        response =
                restTemplate.getForEntity("/api/getServiceStatisticsCSV?startDate=2014-01-01&endDate=2023-01-01", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        List<String> csvContent = Arrays.asList(response.getBody().split("\r\n"));
        assertEquals(3289, csvContent.size());
        List<String> csvHeader = Arrays.asList(csvContent.get(0).split(","));
        assertEquals(4, csvHeader.size());
        assertEquals("Date", csvHeader.get(0));
        assertEquals("Number of REST services", csvHeader.get(1));
        assertEquals("Number of SOAP services", csvHeader.get(2));
        assertEquals("Number of OpenApi services", csvHeader.get(3));

        for (int i = 1; i < csvContent.size() - 1; i++) {
            List<String> csvRowContent = Arrays.asList(csvContent.get(i).split(","));
            assertEquals(4, csvRowContent.size());
            assertEquals(16, csvRowContent.get(0).length());
            assertTrue(Integer.parseInt(csvRowContent.get(1)) > 0);
            assertTrue(Integer.parseInt(csvRowContent.get(2)) > 0);
            assertTrue(Integer.parseInt(csvRowContent.get(3)) > 0);
        }

        // testGetServiceStatisticsCSVInvalidDateFormatException
        response =
                restTemplate.getForEntity("/api/getServiceStatisticsCSV?startDate=01-01-2014&endDate=2022-01-01", String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testGetListOfServices() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServices?startDate=2014-01-01&endDate=2023-01-01", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray memberData = json.getJSONArray("memberData");
        JSONArray securityServerData = json.getJSONArray("securityServerData");
        assertEquals(3288, memberData.length());
        assertEquals(1, securityServerData.length());

        for (int i = 0; i < memberData.length(); i++) {
            JSONArray memberDataListJson = memberData.optJSONObject(i).optJSONArray("memberDataList");
            assertEquals(3, memberDataListJson.length());
            assertEquals(1, memberDataListJson.optJSONObject(0).optJSONArray("subsystemList").length());

            assertEquals("TestSubSystem", memberDataListJson.optJSONObject(0).optJSONArray("subsystemList")
                    .optJSONObject(0).optString("subsystemCode"));
            assertEquals(4, memberDataListJson.optJSONObject(0).optJSONArray("subsystemList")
                    .optJSONObject(0).optJSONArray("serviceList").length());

            assertEquals(0, memberDataListJson.optJSONObject(1).optJSONArray("subsystemList").length());

            assertEquals(1, memberDataListJson.optJSONObject(2).optJSONArray("subsystemList").length());
            assertEquals("TestSubSystem12345", memberDataListJson.optJSONObject(2).optJSONArray("subsystemList")
                    .optJSONObject(0).optString("subsystemCode"));
            assertEquals(15, memberDataListJson.optJSONObject(2).optJSONArray("subsystemList")
                    .optJSONObject(0).optJSONArray("serviceList").length());
        }

        assertEquals("GOV", securityServerData.optJSONObject(0).optString("memberClass"));
        assertEquals("1234", securityServerData.optJSONObject(0).optString("memberCode"));
        assertEquals("SS1", securityServerData.optJSONObject(0).optString("serverCode"));
        assertEquals("10.18.150.48", securityServerData.optJSONObject(0).optString("address"));
    }

    @Test
    public void testGetListOfServicesInvalidDaServiceStatisticsCSVteFormatException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServices?startDate=01-01-2014&endDate=2022-01-01", String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testGetListOfServicesCSV() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServicesCSV?startDate=2014-01-01&endDate=2023-01-01", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        List<String> csvContent = Arrays.asList(response.getBody().split("\r\n"));
        assertEquals(69052, csvContent.size());
        List<String> csvHeader = Arrays.asList(csvContent.get(0).split(","));
        assertEquals(13, csvHeader.size());
        assertEquals("Date", csvHeader.get(0));
        assertEquals("XRoad instance", csvHeader.get(1));
        assertEquals("Member class", csvHeader.get(2));
        assertEquals("Member code", csvHeader.get(3));
        assertEquals("Member name", csvHeader.get(4));
        assertEquals("Member created", csvHeader.get(5));
        assertEquals("Subsystem code", csvHeader.get(6));
        assertEquals("Subsystem created", csvHeader.get(7));
        assertEquals("Subsystem active", csvHeader.get(8));
        assertEquals("Service code", csvHeader.get(9));
        assertEquals("Service version", csvHeader.get(10));
        assertEquals("Service created", csvHeader.get(11));
        assertEquals("Service active", csvHeader.get(12));

        for (int i = 1; i < csvContent.size() - 1; i++) {
            List<String> csvRowContent = Arrays.asList(csvContent.get(i).split(","));
            assertTrue(csvRowContent.size() >= 1);
            assertTrue(csvRowContent.get(0).length() >= 3 || csvRowContent.get(1).length() >= 3);
        }
    }

    @Test
    public void testGetListOfServicesCSVInvalidDaServiceStatisticsCSVteFormatException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServicesCSV?startDate=01-01-2014&endDate=2022-01-01", String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testListSecurityServers() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listSecurityServers", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONArray securityServerDataList = json.getJSONArray("securityServerDataList");
        assertEquals(1, securityServerDataList.length());

        for (int i = 0; i < securityServerDataList.length(); i++) {
            assertEquals("{\"memberCode\":\"1234\",\"name\":\"ACME\",\"memberClass\":\"GOV\",\"subsystemCode\":null}", securityServerDataList.optJSONObject(i).optString("owner"));
            assertEquals("SS1", securityServerDataList.optJSONObject(i).optString("serverCode"));
            assertEquals("10.18.150.48", securityServerDataList.optJSONObject(i).optString("address"));
            assertEquals("[{\"memberCode\":\"1234\",\"name\":\"ACME\",\"memberClass\":\"GOV\",\"subsystemCode\":\"MANAGEMENT\"}," +
                            "{\"memberCode\":\"1234\",\"name\":\"ACME\",\"memberClass\":\"GOV\",\"subsystemCode\":\"TEST\"}]",
                    securityServerDataList.optJSONObject(i).optString("clients"));
        }
    }

    @Test
    public void testListDescriptors() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listDescriptors", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        JSONArray descriptorInfoList = new JSONArray(response.getBody());
        assertEquals(2, descriptorInfoList.length());
        assertEquals("GOV", descriptorInfoList.optJSONObject(0).optString("memberClass"));
        assertEquals("1234", descriptorInfoList.optJSONObject(0).optString("memberCode"));
        assertEquals("ACME", descriptorInfoList.optJSONObject(0).optString("memberName"));
        assertEquals("MANAGEMENT", descriptorInfoList.optJSONObject(0).optString("subsystemCode"));
        assertEquals("DEV", descriptorInfoList.optJSONObject(0).optString("xroadInstance"));
        assertEquals("Subsystem Name EN", descriptorInfoList.optJSONObject(0)
                .optJSONObject("subsystemName").getString("en"));
        assertEquals("Subsystem Name ET", descriptorInfoList.optJSONObject(0)
                .optJSONObject("subsystemName").getString("et"));
        assertEquals("Firstname Lastname", descriptorInfoList.optJSONObject(0)
                .optJSONArray("email").optJSONObject(0).optString("name"));
        assertEquals("yourname@yourdomain", descriptorInfoList.optJSONObject(0)
                .optJSONArray("email").optJSONObject(0).optString("email"));
        assertEquals("GOV", descriptorInfoList.optJSONObject(1).optString("memberClass"));
        assertEquals("1234", descriptorInfoList.optJSONObject(1).optString("memberCode"));
        assertEquals("ACME", descriptorInfoList.optJSONObject(1).optString("memberName"));
        assertEquals("TEST", descriptorInfoList.optJSONObject(1).optString("subsystemCode"));
        assertEquals("DEV", descriptorInfoList.optJSONObject(1).optString("xroadInstance"));
        assertEquals("Subsystem Name EN", descriptorInfoList.optJSONObject(1)
                .optJSONObject("subsystemName").getString("en"));
        assertEquals("Subsystem Name ET", descriptorInfoList.optJSONObject(1)
                .optJSONObject("subsystemName").getString("et"));
        assertEquals("Firstname Lastname", descriptorInfoList.optJSONObject(1)
                .optJSONArray("email").optJSONObject(0).optString("name"));
        assertEquals("yourname@yourdomain", descriptorInfoList.optJSONObject(1)
                .optJSONArray("email").optJSONObject(0).optString("email"));
    }

    @Test
    public void testGetEndpoints() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getEndpoints/dev-cs/PUB/11/TestSubSystem12345/testService4", String.class);
        assertEquals(200, response.getStatusCodeValue());
        ServiceEndpointsResponse endpointsResponse = new ServiceEndpointsResponse();
        endpointsResponse.setXRoadInstance("xroadInstance");
        endpointsResponse.setMemberClass("memberClass");
        endpointsResponse.setMemberCode("memberCode");
        endpointsResponse.setSubsystemCode("subsystemCode");
        endpointsResponse.setServiceCode("serviceCode");
        endpointsResponse.setServiceVersion("serviceVersion");
        List<EndpointData> endpointList = new ArrayList<>();
        endpointList.add(EndpointData.builder().method("GET").path("/getServices").build());
        endpointList.add(EndpointData.builder().method("POST").path("/setServices").build());
        endpointsResponse.setEndpointList(endpointList);
        JSONObject json = new JSONObject(response.getBody());
        assertEquals(1, json.length());
        assertEquals("dev-cs", json.optJSONArray("listOfServices").optJSONObject(0).optString(endpointsResponse.getXRoadInstance()));
        assertEquals("PUB", json.optJSONArray("listOfServices").optJSONObject(0).optString(endpointsResponse.getMemberClass()));
        assertEquals("11", json.optJSONArray("listOfServices").optJSONObject(0).optString(endpointsResponse.getMemberCode()));
        assertEquals("TestSubSystem12345", json.optJSONArray("listOfServices").optJSONObject(0).optString(endpointsResponse.getSubsystemCode()));
        assertEquals("testService4", json.optJSONArray("listOfServices").optJSONObject(0).optString(endpointsResponse.getServiceCode()));
        assertEquals("v1", json.optJSONArray("listOfServices").optJSONObject(0).optString(endpointsResponse.getServiceVersion()));
        assertEquals(2, json.optJSONArray("listOfServices").optJSONObject(0).optJSONArray("endpointList").length());
    }

    @Test
    public void testGetRest() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getRest/dev-cs/PUB/11/TestSubSystem12345/testService4", String.class);
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        assertEquals(1, json.length());
        assertEquals("dev-cs", json.optJSONArray("listOfServices").optJSONObject(0).optString("xroadInstance"));
        assertEquals("PUB", json.optJSONArray("listOfServices").optJSONObject(0).optString("memberClass"));
        assertEquals("11", json.optJSONArray("listOfServices").optJSONObject(0).optString("memberCode"));
        assertEquals("TestSubSystem12345", json.optJSONArray("listOfServices").optJSONObject(0).optString("subsystemCode"));
        assertEquals("testService4", json.optJSONArray("listOfServices").optJSONObject(0).optString("serviceCode"));
        assertEquals("v1", json.optJSONArray("listOfServices").optJSONObject(0).optString("serviceVersion"));
        assertEquals(2, json.optJSONArray("listOfServices").optJSONObject(0).optJSONArray("endpointList").length());
    }
}
