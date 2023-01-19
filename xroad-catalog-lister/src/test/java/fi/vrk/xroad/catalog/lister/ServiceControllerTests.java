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

import fi.vrk.xroad.catalog.lister.util.ServiceUtil;
import fi.vrk.xroad.catalog.persistence.dto.EndpointData;
import fi.vrk.xroad.catalog.persistence.dto.ServiceEndpointsResponse;
import fi.vrk.xroad.catalog.persistence.entity.Endpoint;
import fi.vrk.xroad.catalog.persistence.entity.ErrorLog;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Rest;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.StatusInfo;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.repository.ErrorLogRepository;
import fi.vrk.xroad.catalog.persistence.repository.MemberRepository;
import fi.vrk.xroad.catalog.persistence.repository.RestRepository;
import fi.vrk.xroad.catalog.persistence.repository.ServiceRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = ListerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"xroad-catalog.shared-params-file=src/test/resources/shared-params.xml"})
@ActiveProfiles({"default","fi"})
public class ServiceControllerTests {

    private static final String xRoadInstance = "DEV";
    private static final String anotherInstance = "ICE";
    private static final String memberClass = "GOV";
    private static final String anotherMemberClass = "COM";
    private static final String memberCode = "1234";
    private static final String anotherMemberCode = "12345";
    private static final String firstSubsystem = "TestSubsystem";
    private static final String secondSubsystem = "AnotherTestSubsystem";
    private static final String errorMessage = "Service not found";

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    ErrorLogRepository errorLogRepository;

    @MockBean
    ServiceRepository serviceRepository;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    RestRepository restRepository;

    @Test
    public void testListErrorsForSubsystem() throws JSONException {
        String startDate = "2014-01-01";
        String endDate = "2022-01-01";
        mockFindErrorLogForSubsystem(startDate, endDate);
        String url = "/api/listErrors/" + xRoadInstance + "/" + memberClass + "/"
                + memberCode +"/" + firstSubsystem + "?startDate=" + startDate + "&endDate=" + endDate;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(1, errorList.length());

        for (int i = 0; i < errorList.length(); i++) {
            assertEquals(errorMessage, errorList.optJSONObject(i).optString("message"));
            assertEquals(xRoadInstance, errorList.optJSONObject(i).optString("xroadInstance"));
            assertEquals(memberClass, errorList.optJSONObject(i).optString("memberClass"));
            assertEquals(memberCode, errorList.optJSONObject(i).optString("memberCode"));
            assertEquals(firstSubsystem, errorList.optJSONObject(i).optString("subsystemCode"));
        }
    }

    @Test
    public void testListErrorsForSubsystemWithPagination() throws JSONException {
        String startDate = "2014-01-01";
        String endDate = "2022-01-01";
        mockFindErrorLogForSubsystem(startDate, endDate);
        String url = "/api/listErrors/" + xRoadInstance + "/" + memberClass + "/"
                + memberCode +"/" + firstSubsystem + "?startDate=" + startDate + "&endDate=" + endDate + "&page=0&limit=100";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(1, errorList.length());

        for (int i = 0; i < errorList.length(); i++) {
            assertEquals(errorMessage, errorList.optJSONObject(i).optString("message"));
            assertEquals(xRoadInstance, errorList.optJSONObject(i).optString("xroadInstance"));
            assertEquals(memberClass, errorList.optJSONObject(i).optString("memberClass"));
            assertEquals(memberCode, errorList.optJSONObject(i).optString("memberCode"));
            assertEquals(firstSubsystem, errorList.optJSONObject(i).optString("subsystemCode"));
        }
    }

    @Test
    public void testListErrors() throws JSONException {
        String startDate = "2014-01-01";
        String endDate = "2022-01-01";
        mockFindErrorLogForMemberCode(startDate, endDate);
        mockFindErrorLogForMemberClass(startDate, endDate);
        mockFindErrorLogForInstance(startDate, endDate);
        mockFindErrorLogForAll(startDate, endDate);

        // testListErrorsForMemberCode
        String url = "/api/listErrors/" + xRoadInstance + "/" + memberClass + "/" + memberCode + "?startDate=" + startDate + "&endDate=" + endDate;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        JSONArray errorList = json.getJSONArray("errorLogList");
        assertEquals(2, errorList.length());

        // testListErrorsForMemberCodeWithPagination
        url = "/api/listErrors/" + xRoadInstance + "/" + memberClass + "/" + memberCode + "?startDate=" + startDate + "&endDate=" + endDate + "&page=0&limit=100";
        response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(2, errorList.length());

        // testListErrorsForMemberClass
        url = "/api/listErrors/" + xRoadInstance + "/" + memberClass + "?startDate=" + startDate + "&endDate=" + endDate;
        response = restTemplate.getForEntity(url, String.class);
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(3, errorList.length());

        // testListErrorsForMemberClassWithPagination
        url = "/api/listErrors/" + xRoadInstance + "/" + memberClass + "?startDate=" + startDate + "&endDate=" + endDate + "&page=0&limit=100";
        response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(3, errorList.length());

        // testListErrorsForInstance
        url = "/api/listErrors/" + xRoadInstance + "?startDate=" + startDate + "&endDate=" + endDate;
        response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(4, errorList.length());

        // testListErrorsForInstanceWithPagination
        url = "/api/listErrors/" + xRoadInstance + "?startDate=" + startDate + "&endDate=" + endDate + "&page=0&limit=100";
        response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(4, errorList.length());

        // testListErrorsForAll
        url = "/api/listErrors?startDate=" + startDate + "&endDate=" + endDate;
        response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(5, errorList.length());

        // testListErrorsForAllWithPagination
        url = "/api/listErrors?startDate=" + startDate + "&endDate=" + endDate + "&page=0&limit=100";
        response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCodeValue());
        json = new JSONObject(response.getBody());
        errorList = json.getJSONArray("errorLogList");
        assertEquals(5, errorList.length());

        // testListErrorsInvalidDateFormatException
        startDate = "01-01-2014";
        url = "/api/listErrors?startDate=" + startDate + "&endDate=" + endDate;
        response = restTemplate.getForEntity(url, String.class);
        assertEquals(400, response.getStatusCodeValue());

        // testListErrorsNotFoundException
        startDate = "2010-01-01";
        endDate = "2010-06-01";
        mockErrorLogWithNoContent(startDate, endDate);
        url = "/api/listErrors?startDate=" + startDate + "&endDate=" + endDate;
        response = restTemplate.getForEntity(url, String.class);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testGetDistinctServiceStatistics() throws JSONException {
        // testGetDistinctServiceStatistics
        String startDate = "2014-01-01";
        String endDate = "2023-01-01";
        mockServices();
        String url = "/api/getDistinctServiceStatistics?startDate=" + startDate + "&endDate=" + endDate;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray serviceStatisticsList = json.getJSONArray("distinctServiceStatisticsList");
        assertTrue(serviceStatisticsList.length() > 0);

        for (int i = 0; i < serviceStatisticsList.length(); i++) {
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfDistinctServices") > 0);
        }

        // testGetDistinctServiceStatisticsInvalidDateFormatException
        response =
                restTemplate.getForEntity("/api/getDistinctServiceStatistics?startDate=01-01-2014&endDate=2022-01-01", String.class);
        assertEquals(400, response.getStatusCodeValue());

        // testGetDistinctServiceStatisticsNotFoundException
        response =
                restTemplate.getForEntity("/api/getDistinctServiceStatistics?startDate=2030-01-01&endDate=2030-06-01", String.class);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testGetServiceStatistics() throws JSONException, IOException {
        // testGetServiceStatistics
        String startDate = "2014-01-01";
        String endDate = "2023-01-01";
        mockServices();
        String url = "/api/getServiceStatistics?startDate=" + startDate + "&endDate=" + endDate;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray serviceStatisticsList = json.getJSONArray("serviceStatisticsList");
        assertTrue(serviceStatisticsList.length() > 0);

        for (int i = 0; i < serviceStatisticsList.length(); i++) {
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfSoapServices") >= 0);
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfRestServices") >= 0);
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfOpenApiServices") >= 0);
        }

        // testGetServiceStatisticsInvalidDateFormatException
        startDate = "01-01-2014";
        url = "/api/getServiceStatistics?startDate=" + startDate + "&endDate=" + endDate;
        response = restTemplate.getForEntity(url, String.class);
        assertEquals(400, response.getStatusCodeValue());

        // testGetServiceStatisticsCSV
        startDate = "2014-01-01";
        url = "/api/getServiceStatisticsCSV?startDate=" + startDate + "&endDate=" + endDate;
        response = restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        List<String> csvContent = Arrays.asList(response.getBody().split("\r\n"));
        assertTrue(csvContent.size() > 0);
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
            assertTrue(Integer.parseInt(csvRowContent.get(1)) >= 0);
            assertTrue(Integer.parseInt(csvRowContent.get(2)) >= 0);
            assertTrue(Integer.parseInt(csvRowContent.get(3)) >= 0);
        }

        // testGetServiceStatisticsCSVInvalidDateFormatException
        startDate = "01-01-2014";
        endDate = "2022-01-01";
        url = "/api/getServiceStatisticsCSV?startDate=" + startDate + "&endDate=" + endDate;
        response = restTemplate.getForEntity(url, String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testGetListOfServices() throws JSONException {
        String startDate = "2014-01-01";
        String endDate = "2023-01-01";
        mockMembers();
        String url = "/api/getListOfServices?startDate=" + startDate + "&endDate=" + endDate;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray memberData = json.getJSONArray("memberData");
        JSONArray securityServerData = json.getJSONArray("securityServerData");
        assertTrue(memberData.length() > 0);
        assertEquals(1, securityServerData.length());

        for (int i = 0; i < memberData.length(); i++) {
            JSONArray memberDataListJson = memberData.optJSONObject(i).optJSONArray("memberDataList");
            assertEquals(3, memberDataListJson.length());
            assertEquals(1, memberDataListJson.optJSONObject(0).optJSONArray("subsystemList").length());

            assertEquals("TestSubsystem", memberDataListJson.optJSONObject(0).optJSONArray("subsystemList")
                    .optJSONObject(0).optString("subsystemCode"));
            assertEquals(0, memberDataListJson.optJSONObject(0).optJSONArray("subsystemList")
                    .optJSONObject(0).optJSONArray("serviceList").length());

            assertEquals(1, memberDataListJson.optJSONObject(1).optJSONArray("subsystemList").length());

            assertEquals(1, memberDataListJson.optJSONObject(2).optJSONArray("subsystemList").length());
            assertEquals("TestSubsystem", memberDataListJson.optJSONObject(2).optJSONArray("subsystemList")
                    .optJSONObject(0).optString("subsystemCode"));
            assertEquals(0, memberDataListJson.optJSONObject(2).optJSONArray("subsystemList")
                    .optJSONObject(0).optJSONArray("serviceList").length());
        }

        assertEquals("GOV", securityServerData.optJSONObject(0).optString("memberClass"));
        assertEquals("1234", securityServerData.optJSONObject(0).optString("memberCode"));
        assertEquals("SS1", securityServerData.optJSONObject(0).optString("serverCode"));
        assertEquals("10.18.150.48", securityServerData.optJSONObject(0).optString("address"));
    }

    @Test
    public void testGetListOfServicesInvalidDaServiceStatisticsCSVteFormatException() {
        String startDate = "01-01-2014";
        String endDate = "2022-01-01";
        String url = "/api/getListOfServices?startDate=" + startDate + "&endDate=" + endDate;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testGetListOfServicesCSV() {
        String startDate = "2014-01-01";
        String endDate = "2023-01-01";
        mockMembers();
        String url = "/api/getListOfServicesCSV?startDate=" + startDate + "&endDate=" + endDate;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        List<String> csvContent = Arrays.asList(response.getBody().split("\r\n"));
        assertTrue(csvContent.size() > 0);
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
    public void testGetListOfServicesCSVInvalidDateFormatException() {
        String startDate = "01-01-2014";
        String endDate = "2022-01-01";
        String url = "/api/getListOfServicesCSV?startDate=" + startDate + "&endDate=" + endDate;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testListSecurityServers() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/listSecurityServers", String.class);
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
        ResponseEntity<String> response = restTemplate.getForEntity("/api/listDescriptors", String.class);
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
        mockServicesWithEndpointsByMemberServiceAndSubsystem();
        String url = "/api/getEndpoints/" + xRoadInstance + "/" + memberClass + "/" + memberCode + "/" + firstSubsystem + "/aService";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
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
        assertEquals(xRoadInstance, json.optJSONArray("listOfServices").optJSONObject(0).optString(endpointsResponse.getXRoadInstance()));
        assertEquals(memberClass, json.optJSONArray("listOfServices").optJSONObject(0).optString(endpointsResponse.getMemberClass()));
        assertEquals(memberCode, json.optJSONArray("listOfServices").optJSONObject(0).optString(endpointsResponse.getMemberCode()));
        assertEquals(firstSubsystem, json.optJSONArray("listOfServices").optJSONObject(0).optString(endpointsResponse.getSubsystemCode()));
        assertEquals("aService", json.optJSONArray("listOfServices").optJSONObject(0).optString(endpointsResponse.getServiceCode()));
        assertEquals("v1", json.optJSONArray("listOfServices").optJSONObject(0).optString(endpointsResponse.getServiceVersion()));
        assertEquals(1, json.optJSONArray("listOfServices").optJSONObject(0).optJSONArray("endpointList").length());
    }

    @Test
    public void testGetRest() throws JSONException {
        mockServicesWithRestByMemberServiceAndSubsystem();
        String url = "/api/getRest/" + xRoadInstance + "/" + memberClass + "/" + memberCode + "/" + firstSubsystem + "/aService";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCodeValue());
        JSONObject json = new JSONObject(response.getBody());
        assertEquals(1, json.length());
        assertEquals(xRoadInstance, json.optJSONArray("listOfServices").optJSONObject(0).optString("xroadInstance"));
        assertEquals(memberClass, json.optJSONArray("listOfServices").optJSONObject(0).optString("memberClass"));
        assertEquals(memberCode, json.optJSONArray("listOfServices").optJSONObject(0).optString("memberCode"));
        assertEquals(firstSubsystem, json.optJSONArray("listOfServices").optJSONObject(0).optString("subsystemCode"));
        assertEquals("aService", json.optJSONArray("listOfServices").optJSONObject(0).optString("serviceCode"));
        assertEquals("v1", json.optJSONArray("listOfServices").optJSONObject(0).optString("serviceVersion"));
        assertEquals(1, json.optJSONArray("listOfServices").optJSONObject(0).optJSONArray("endpointList").length());
    }

    private void mockErrorLogWithNoContent(String startDate, String endDate) {
        List<ErrorLog> errorLogList = new ArrayList<>();
        Page<ErrorLog> errors = new PageImpl<>(errorLogList, PageRequest.of(0, 100), 1);
        given(errorLogRepository.findAnyByCreated(ServiceUtil.convertStringToLocalDateTime(startDate),
                ServiceUtil.convertStringToLocalDateTime(endDate),
                PageRequest.of(0, 100))).willReturn(errors);
    }

    private void mockFindErrorLogForSubsystem(String startDate, String endDate) {
        List<ErrorLog> errorLogList = new ArrayList<>();
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(memberCode)
                .subsystemCode(firstSubsystem)
                .message(errorMessage)
                .build());
        Page<ErrorLog> errors = new PageImpl<>(errorLogList, PageRequest.of(0, 100), 20);
        given(errorLogRepository.findAnyByAllParameters(ServiceUtil.convertStringToLocalDateTime(startDate),
                ServiceUtil.convertStringToLocalDateTime(endDate),
                xRoadInstance,
                memberClass,
                memberCode,
                firstSubsystem,
                PageRequest.of(0, 100))).willReturn(errors);
    }

    private void mockFindErrorLogForMemberCode(String startDate, String endDate) {
        List<ErrorLog> errorLogList = new ArrayList<>();
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(memberCode)
                .subsystemCode(firstSubsystem)
                .message(errorMessage)
                .build());
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(memberCode)
                .subsystemCode(secondSubsystem)
                .message(errorMessage)
                .build());
        Page<ErrorLog> errors = new PageImpl<>(errorLogList, PageRequest.of(0, 100), 20);
        given(errorLogRepository.findAnyByMemberCode(ServiceUtil.convertStringToLocalDateTime(startDate),
                ServiceUtil.convertStringToLocalDateTime(endDate),
                xRoadInstance,
                memberClass,
                memberCode,
                PageRequest.of(0, 100))).willReturn(errors);
    }

    private void mockFindErrorLogForMemberClass(String startDate, String endDate) {
        List<ErrorLog> errorLogList = new ArrayList<>();
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(memberCode)
                .subsystemCode(firstSubsystem)
                .message(errorMessage)
                .build());
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(memberCode)
                .subsystemCode(secondSubsystem)
                .message(errorMessage)
                .build());
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(anotherMemberCode)
                .subsystemCode(secondSubsystem)
                .message(errorMessage)
                .build());
        Page<ErrorLog> errors = new PageImpl<>(errorLogList, PageRequest.of(0, 100), 20);
        given(errorLogRepository.findAnyByMemberClass(ServiceUtil.convertStringToLocalDateTime(startDate),
                ServiceUtil.convertStringToLocalDateTime(endDate),
                xRoadInstance,
                memberClass,
                PageRequest.of(0, 100))).willReturn(errors);
    }

    private void mockFindErrorLogForInstance(String startDate, String endDate) {
        List<ErrorLog> errorLogList = new ArrayList<>();
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(memberCode)
                .subsystemCode(firstSubsystem)
                .message(errorMessage)
                .build());
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(memberCode)
                .subsystemCode(secondSubsystem)
                .message(errorMessage)
                .build());
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(anotherMemberCode)
                .subsystemCode(secondSubsystem)
                .message(errorMessage)
                .build());
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(anotherMemberClass)
                .memberCode(anotherMemberCode)
                .subsystemCode(secondSubsystem)
                .message(errorMessage)
                .build());
        Page<ErrorLog> errors = new PageImpl<>(errorLogList, PageRequest.of(0, 100), 20);
        given(errorLogRepository.findAnyByInstance(ServiceUtil.convertStringToLocalDateTime(startDate),
                ServiceUtil.convertStringToLocalDateTime(endDate),
                xRoadInstance,
                PageRequest.of(0, 100))).willReturn(errors);
    }

    private void mockFindErrorLogForAll(String startDate, String endDate) {
        List<ErrorLog> errorLogList = new ArrayList<>();
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(memberCode)
                .subsystemCode(firstSubsystem)
                .message(errorMessage)
                .build());
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(memberCode)
                .subsystemCode(secondSubsystem)
                .message(errorMessage)
                .build());
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(anotherMemberCode)
                .subsystemCode(secondSubsystem)
                .message(errorMessage)
                .build());
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(anotherMemberClass)
                .memberCode(anotherMemberCode)
                .subsystemCode(secondSubsystem)
                .message(errorMessage)
                .build());
        errorLogList.add(ErrorLog.builder()
                .xRoadInstance(anotherInstance)
                .memberClass(anotherMemberClass)
                .memberCode(anotherMemberCode)
                .subsystemCode(secondSubsystem)
                .message(errorMessage)
                .build());
        Page<ErrorLog> errors = new PageImpl<>(errorLogList, PageRequest.of(0, 100), 20);
        given(errorLogRepository.findAnyByCreated(ServiceUtil.convertStringToLocalDateTime(startDate),
                ServiceUtil.convertStringToLocalDateTime(endDate),
                PageRequest.of(0, 100))).willReturn(errors);
    }

    private void mockServices() {
        List<Service> services = new ArrayList<>();
        LocalDateTime created = LocalDateTime.of(2015, 1, 1, 1, 1);
        LocalDateTime changed = LocalDateTime.of(2015, 1, 1, 1, 1);
        LocalDateTime fetched = LocalDateTime.of(2015, 1, 1, 1, 1);
        Member member = new Member(xRoadInstance, memberClass, memberCode, "memberX");
        Subsystem subsystem = new Subsystem(member, firstSubsystem);
        Service firstService = new Service(subsystem, "aService", "v1");
        firstService.setStatusInfo(new StatusInfo(created, changed, fetched, null));
        Service secondService = new Service(subsystem, "anotherService", "v1");
        secondService.setStatusInfo(new StatusInfo(created.plusYears(1), changed, fetched, null));
        services.add(firstService);
        services.add(secondService);
        given(serviceRepository.findAllActive()).willReturn(services);
    }

    private void mockServicesWithEndpointsByMemberServiceAndSubsystem() {
        List<Service> services = new ArrayList<>();
        Member member = new Member(xRoadInstance, memberClass, memberCode, "memberX");
        Subsystem subsystem = new Subsystem(member, firstSubsystem);
        Service firstService = new Service(subsystem, "aService", "v1");
        firstService.setEndpoint(new Endpoint(firstService, "GET", "/getServices"));
        firstService.setEndpoint(new Endpoint(firstService, "POST", "/setServices"));
        Service secondService = new Service(subsystem, "anotherService", "v1");
        secondService.setEndpoint(new Endpoint(secondService, "GET", "/getServices"));
        secondService.setEndpoint(new Endpoint(secondService, "POST", "/setServices"));
        services.add(firstService);
        services.add(secondService);
        given(serviceRepository.findServicesByMemberServiceAndSubsystem(xRoadInstance,
                memberClass,
                memberCode,
                "aService",
                firstSubsystem)).willReturn(services);
    }

    private void mockServicesWithRestByMemberServiceAndSubsystem() {
        List<Service> services = new ArrayList<>();
        Member member = new Member(xRoadInstance, memberClass, memberCode, "memberX");
        Subsystem subsystem = new Subsystem(member, firstSubsystem);
        Service firstService = new Service(subsystem, "aService", "v1");
        Service secondService = new Service(subsystem, "anotherService", "v1");
        Rest rest1 = new Rest(firstService, "data123", "abc12345");
        Rest rest2 = new Rest(secondService, "data789", "abc11111");
        firstService.setRest(rest1);
        secondService.setRest(rest2);
        firstService.setEndpoint(new Endpoint(firstService, "GET", "/getServices"));
        firstService.setEndpoint(new Endpoint(firstService, "POST", "/setServices"));
        services.add(firstService);
        services.add(secondService);
        given(serviceRepository.findServicesByMemberServiceAndSubsystem(xRoadInstance,
                memberClass,
                memberCode,
                "aService",
                firstSubsystem)).willReturn(services);
        given(restRepository.findAnyByService(firstService)).willReturn(Arrays.asList(rest1));
    }

    private void mockMembers() {
        List<Member> members = new ArrayList<>();
        LocalDateTime created = LocalDateTime.of(2015, 1, 1, 1, 1);
        LocalDateTime changed = LocalDateTime.of(2015, 1, 1, 1, 1);
        LocalDateTime fetched = LocalDateTime.of(2015, 1, 1, 1, 1);
        Member memberX = new Member(xRoadInstance, memberClass, memberCode, "memberX");
        Member memberY = new Member(xRoadInstance, anotherMemberClass, memberCode, "memberY");
        Member memberZ = new Member(xRoadInstance, memberClass, anotherMemberCode, "memberZ");
        memberX.setStatusInfo(new StatusInfo(created, changed, fetched, null));
        memberY.setStatusInfo(new StatusInfo(created.plusYears(1), changed, fetched, null));
        memberZ.setStatusInfo(new StatusInfo(created.plusYears(2), changed, fetched, null));
        memberX.setSubsystems(new HashSet<>(Arrays.asList(new Subsystem(memberX, firstSubsystem))));
        memberY.setSubsystems(new HashSet<>(Arrays.asList(new Subsystem(memberY, firstSubsystem))));
        memberZ.setSubsystems(new HashSet<>(Arrays.asList(new Subsystem(memberZ, firstSubsystem))));
        members.add(memberX);
        members.add(memberY);
        members.add(memberZ);
        given(memberRepository.findAll()).willReturn(new HashSet<>(members));
    }
}
