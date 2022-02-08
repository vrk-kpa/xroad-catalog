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
package fi.vrk.xroad.catalog.lister;

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

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ListerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"xroad-catalog.max-history-length-in-days=4000",
                                  "xroad-catalog.shared-params-file=src/test/resources/shared-params.xml"})
public class ServiceControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void testGetOrganizationWithOrganizationData() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganization/0123456-9", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONObject organizationData = json.optJSONObject("organizationData");
        JSONObject companyData = json.optJSONObject("companyData");
        assertEquals(null, companyData);
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

    @Test
    public void testGetOrganizationWithCompanyData() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganization/1710128-9", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONObject organizationData = json.optJSONObject("organizationData");
        JSONObject companyData = json.optJSONObject("companyData");
        assertEquals(null, organizationData);
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
    public void testGetOrganizationNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganization/0-12345", String.class);
        assertEquals(204, response.getStatusCodeValue());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testGetOrganizationChangesForCompanyDataOlderValues() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganizationChanges/1710128-9/2010-01-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONArray changedValues = json.optJSONArray("changedValueList");
        assertEquals(true, json.optBoolean("changed"));
        assertEquals(12, changedValues.length());
    }

    @Test
    public void testGetOrganizationChangesForOrganizationDataOlderValues() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganizationChanges/0123456-9/2010-01-01", String.class);
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONArray changedValues = json.optJSONArray("changedValueList");
        assertEquals(true, json.optBoolean("changed"));
        assertEquals(19, changedValues.length());
    }

    @Test
    public void testGetOrganizationChangesForCompanyDataNewerValues() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganizationChanges/1710128-9/2020-09-04", String.class);
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONArray changedValues = json.optJSONArray("changedValueList");
        assertEquals(true, json.optBoolean("changed"));
        assertEquals(1, changedValues.length());
    }

    @Test
    public void testGetOrganizationChangesForOrganizationDataNewerValues() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganizationChanges/0123456-9/2019-12-31", String.class);
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONArray changedValues = json.optJSONArray("changedValueList");
        assertEquals(true, json.optBoolean("changed"));
        assertEquals(1, changedValues.length());
    }

    @Test
    public void testGetOrganizationChangesForCompanyDataNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganizationChanges/1710128-9/2022-01-01", String.class);
        assertEquals(204, response.getStatusCodeValue());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testGetOrganizationChangesForOrganizationDataNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganizationChanges/0123456-9/2022-01-01", String.class);
        assertEquals(204, response.getStatusCodeValue());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testGetOrganizationChangesInvalidDateException() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/getOrganizationChanges/0123456-9/01-01-2022", String.class);
        String errMsg = "Exception parsing since parameter: Text \'01-01-2022\' could not be parsed at index 0";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testListErrorsForSubsystem() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/DEV/GOV/1234/TestSubsystem/4000", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(1, errorList.length());

        for (int i = 0; i < errorList.length(); i++) {
            assertEquals(errorList.optJSONObject(i).optString("message"), "Service not found");
            assertEquals(errorList.optJSONObject(i).optString("xroadInstance"), "DEV");
            assertEquals(errorList.optJSONObject(i).optString("memberClass"), "GOV");
            assertEquals(errorList.optJSONObject(i).optString("memberCode"), "1234");
            assertEquals(errorList.optJSONObject(i).optString("subsystemCode"), "TestSubsystem");
        }
    }

    @Test
    public void testListErrorsForSubsystemWithPagination() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/DEV/GOV/1234/TestSubsystem/4000?page=0&limit=100", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(1, errorList.length());

        for (int i = 0; i < errorList.length(); i++) {
            assertEquals(errorList.optJSONObject(i).optString("message"), "Service not found");
            assertEquals(errorList.optJSONObject(i).optString("xroadInstance"), "DEV");
            assertEquals(errorList.optJSONObject(i).optString("memberClass"), "GOV");
            assertEquals(errorList.optJSONObject(i).optString("memberCode"), "1234");
            assertEquals(errorList.optJSONObject(i).optString("subsystemCode"), "TestSubsystem");
        }
    }

    @Test
    public void testListErrorsForMemberCode() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/DEV/GOV/1234/4000", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(2, errorList.length());
    }

    @Test
    public void testListErrorsForMemberCodeWithPagination() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/DEV/GOV/1234/4000?page=0&limit=100", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(2, errorList.length());
    }

    @Test
    public void testListErrorsForMemberClass() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/DEV/GOV/4000", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(2, errorList.length());
    }

    @Test
    public void testListErrorsForMemberClassWithPagination() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/DEV/GOV/1234/4000?page=0&limit=100", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(2, errorList.length());
    }

    @Test
    public void testListErrorsForInstance() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/DEV/4000", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(3, errorList.length());
    }

    @Test
    public void testListErrorsForInstanceWithPagination() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/DEV/4000?page=0&limit=100", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(3, errorList.length());
    }

    @Test
    public void testListErrorsForAll() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/4000", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(6, errorList.length());
    }

    @Test
    public void testListErrorsForAllWithPagination() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/4000?page=0&limit=100", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(6, errorList.length());
    }

    @Test
    public void testListErrorsHistoryParameterZeroException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/DEV/GOV/1234/TestSubsystem/0", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testListErrorsHistoryParameterMoreThanMaximumException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/listErrors/DEV/GOV/1234/TestSubsystem/4001", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetDistinctServiceStatistics() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getDistinctServiceStatistics/60", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray serviceStatisticsList = json.getJSONArray("distinctServiceStatisticsList");
        assertEquals(60, serviceStatisticsList.length());

        for (int i = 0; i < serviceStatisticsList.length(); i++) {
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfDistinctServices") > 0);
        }
    }

    @Test
    public void testGetDistinctServiceStatisticsHistoryParameterZeroException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getDistinctServiceStatistics/0", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetDistinctServiceStatisticsHistoryParameterMoreThanMaximumException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getDistinctServiceStatistics/4001", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetDistinctServiceStatisticsHistoryParameterNullException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getDistinctServiceStatistics", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetServiceStatistics() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatistics/60", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray serviceStatisticsList = json.getJSONArray("serviceStatisticsList");
        assertEquals(60, serviceStatisticsList.length());

        for (int i = 0; i < serviceStatisticsList.length(); i++) {
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfSoapServices") > 0);
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfRestServices") > 0);
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfOpenApiServices") > 0);
        }
    }

    @Test
    public void testGetServiceStatisticsHistoryParameterZeroException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatistics/0", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetServiceStatisticsHistoryParameterMoreThanMaximumException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatistics/4001", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetServiceStatisticsHistoryParameterNullException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatistics", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetServiceStatisticsCSV() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatisticsCSV/60", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        List<String> csvContent = Arrays.asList(response.getBody().split("\r\n"));
        assertEquals(61, csvContent.size());
        List<String> csvHeader = Arrays.asList(csvContent.get(0).split(","));
        assertEquals(4, csvHeader.size());
        assertEquals("Date", csvHeader.get(0));
        assertEquals("Number of REST services", csvHeader.get(1));
        assertEquals("Number of SOAP services", csvHeader.get(2));
        assertEquals("Number of OpenApi services", csvHeader.get(3));

        for (int i = 1; i < csvContent.size() - 1; i++) {
            List<String> csvRowContent = Arrays.asList(csvContent.get(i).split(","));
            assertEquals(4, csvRowContent.size());
            assertEquals(23, csvRowContent.get(0).length());
            assertTrue(Integer.parseInt(csvRowContent.get(1)) > 0);
            assertTrue(Integer.parseInt(csvRowContent.get(2)) > 0);
            assertTrue(Integer.parseInt(csvRowContent.get(3)) > 0);
        }
    }

    @Test
    public void testGetServiceStatisticsCSVHistoryParameterZeroException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatisticsCSV/0", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetServiceStatisticsCSVHistoryParameterMoreThanMaximumException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatisticsCSV/4001", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetServiceStatisticsCSVHistoryParameterNullException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatisticsCSV", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetListOfServices() throws JSONException {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServices/60", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray memberData = json.getJSONArray("memberData");
        JSONArray securityServerData = json.getJSONArray("securityServerData");
        assertEquals(60, memberData.length());
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
    public void testGetListOfServicesHistoryParameterZeroException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServices/0", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServicesHistoryParameterMoreThanMaximumException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServices/4001", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServicesHistoryParameterNullException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServices", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetListOfServicesCSV() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServicesCSV/60", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        List<String> csvContent = Arrays.asList(response.getBody().split("\r\n"));
        assertEquals(1264, csvContent.size());
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
    public void testGetListOfServicesCSVHistoryParameterZeroException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServicesCSV/0", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServicesCSVHistoryParameterMoreThanMaximumException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServicesCSV/4001", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServicesCSVHistoryParameterNullException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServicesCSV", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 4000 days";
        assertEquals(404, response.getStatusCodeValue());
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
        JSONObject json = new JSONObject(response.getBody());
        JSONArray descriptorInfoList = json.getJSONArray("descriptorInfoList");
        assertEquals(2, descriptorInfoList.length());
        assertEquals("GOV", descriptorInfoList.optJSONObject(0).optString("member_class"));
        assertEquals("1234", descriptorInfoList.optJSONObject(0).optString("member_code"));
        assertEquals("ACME", descriptorInfoList.optJSONObject(0).optString("member_name"));
        assertEquals("MANAGEMENT", descriptorInfoList.optJSONObject(0).optString("subsystem_code"));
        assertEquals("DEV", descriptorInfoList.optJSONObject(0).optString("x_road_instance"));
        assertEquals("Subsystem Name EN", descriptorInfoList.optJSONObject(0)
                .optJSONObject("subsystem_name").getString("en"));
        assertEquals("Subsystem Name ET", descriptorInfoList.optJSONObject(0)
                .optJSONObject("subsystem_name").getString("et"));
        assertEquals("Firstname Lastname", descriptorInfoList.optJSONObject(0)
                .optJSONObject("email").getString("name"));
        assertEquals("yourname@yourdomain", descriptorInfoList.optJSONObject(0)
                .optJSONObject("email").getString("email"));
        assertEquals("GOV", descriptorInfoList.optJSONObject(1).optString("member_class"));
        assertEquals("1234", descriptorInfoList.optJSONObject(1).optString("member_code"));
        assertEquals("ACME", descriptorInfoList.optJSONObject(1).optString("member_name"));
        assertEquals("TEST", descriptorInfoList.optJSONObject(1).optString("subsystem_code"));
        assertEquals("DEV", descriptorInfoList.optJSONObject(1).optString("x_road_instance"));
        assertEquals("Subsystem Name EN", descriptorInfoList.optJSONObject(1)
                .optJSONObject("subsystem_name").getString("en"));
        assertEquals("Subsystem Name ET", descriptorInfoList.optJSONObject(1)
                .optJSONObject("subsystem_name").getString("et"));
        assertEquals("Firstname Lastname", descriptorInfoList.optJSONObject(1)
                .optJSONObject("email").getString("name"));
        assertEquals("yourname@yourdomain", descriptorInfoList.optJSONObject(1)
                .optJSONObject("email").getString("email"));
    }

}
