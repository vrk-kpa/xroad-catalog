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
package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.dto.ListOfServicesRequest;
import fi.vrk.xroad.catalog.persistence.dto.ServiceStatisticsRequest;
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
@TestPropertySource(properties = {"xroad-catalog.max-history-length-in-days=90",
                                  "xroad-catalog.shared-params-file=src/test/resources/shared-params.xml"})
public class ServiceControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void testGetServiceStatistics() throws JSONException {
        ServiceStatisticsRequest serviceStatisticsRequest = ServiceStatisticsRequest.builder().historyAmountInDays(60L).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getServiceStatistics", serviceStatisticsRequest, String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        JSONArray serviceStatisticsList = json.getJSONArray("serviceStatisticsList");
        assertEquals(60, serviceStatisticsList.length());

        for (int i = 0; i < serviceStatisticsList.length(); i++) {
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfSoapServices") > 0);
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("numberOfRestServices") > 0);
            assertTrue(serviceStatisticsList.optJSONObject(i).optLong("totalNumberOfDistinctServices") > 0);
        }
    }

    @Test
    public void testGetServiceStatisticsHistoryParameterZeroException() {
        ServiceStatisticsRequest serviceStatisticsRequest = ServiceStatisticsRequest.builder().historyAmountInDays(0L).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getServiceStatistics", serviceStatisticsRequest, String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetServiceStatisticsHistoryParameterMoreThanMaximumException() {
        ServiceStatisticsRequest serviceStatisticsRequest = ServiceStatisticsRequest.builder().historyAmountInDays(91L).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getServiceStatistics", serviceStatisticsRequest, String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetServiceStatisticsHistoryParameterNullException() {
        ServiceStatisticsRequest serviceStatisticsRequest = ServiceStatisticsRequest.builder().historyAmountInDays(null).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getServiceStatistics", serviceStatisticsRequest, String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetServiceStatisticsCSV() {
        ServiceStatisticsRequest serviceStatisticsRequest = ServiceStatisticsRequest.builder().historyAmountInDays(60L).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getServiceStatisticsCSV", serviceStatisticsRequest, String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        List<String> csvContent = Arrays.asList(response.getBody().split("\r\n"));
        assertEquals(61, csvContent.size());
        List<String> csvHeader = Arrays.asList(csvContent.get(0).split(","));
        assertEquals(4, csvHeader.size());
        assertEquals("Date", csvHeader.get(0));
        assertEquals("Number of REST services", csvHeader.get(1));
        assertEquals("Number of SOAP services", csvHeader.get(2));
        assertEquals("Total distinct services", csvHeader.get(3));

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
        ServiceStatisticsRequest serviceStatisticsRequest = ServiceStatisticsRequest.builder().historyAmountInDays(0L).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getServiceStatisticsCSV", serviceStatisticsRequest, String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetServiceStatisticsCSVHistoryParameterMoreThanMaximumException() {
        ServiceStatisticsRequest serviceStatisticsRequest = ServiceStatisticsRequest.builder().historyAmountInDays(91L).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getServiceStatisticsCSV", serviceStatisticsRequest, String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetServiceStatisticsCSVHistoryParameterNullException() {
        ServiceStatisticsRequest serviceStatisticsRequest = ServiceStatisticsRequest.builder().historyAmountInDays(null).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getServiceStatisticsCSV", serviceStatisticsRequest, String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServices() throws JSONException {
        ListOfServicesRequest listOfServicesRequest = ListOfServicesRequest.builder().historyAmountInDays(60L).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getListOfServices", listOfServicesRequest, String.class);
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
            assertEquals(2, memberDataListJson.optJSONObject(0).optJSONArray("subsystemList")
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
        ListOfServicesRequest listOfServicesRequest = ListOfServicesRequest.builder().historyAmountInDays(0L).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getListOfServices", listOfServicesRequest, String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServicesHistoryParameterMoreThanMaximumException() {
        ListOfServicesRequest listOfServicesRequest = ListOfServicesRequest.builder().historyAmountInDays(91L).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getListOfServices", listOfServicesRequest, String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServicesHistoryParameterNullException() {
        ListOfServicesRequest listOfServicesRequest = ListOfServicesRequest.builder().historyAmountInDays(null).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getListOfServices", listOfServicesRequest, String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServicesCSV() {
        ServiceStatisticsRequest serviceStatisticsRequest = ServiceStatisticsRequest.builder().historyAmountInDays(60L).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getListOfServicesCSV", serviceStatisticsRequest, String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        List<String> csvContent = Arrays.asList(response.getBody().split("\r\n"));
        assertEquals(1144, csvContent.size());
        List<String> csvHeader = Arrays.asList(csvContent.get(0).split(","));
        assertEquals(11, csvHeader.size());
        assertEquals("Date", csvHeader.get(0));
        assertEquals("XRoad instance", csvHeader.get(1));
        assertEquals("Member class", csvHeader.get(2));
        assertEquals("Member code", csvHeader.get(3));
        assertEquals("Member name", csvHeader.get(4));
        assertEquals("Member created", csvHeader.get(5));
        assertEquals("Subsystem code", csvHeader.get(6));
        assertEquals("Subsystem created", csvHeader.get(7));
        assertEquals("Service code", csvHeader.get(8));
        assertEquals("Service version", csvHeader.get(9));
        assertEquals("Service created", csvHeader.get(10));

        for (int i = 1; i < csvContent.size() - 1; i++) {
            List<String> csvRowContent = Arrays.asList(csvContent.get(i).split(","));
            assertTrue(csvRowContent.size() >= 1);
            assertTrue(csvRowContent.get(0).length() >= 3 || csvRowContent.get(1).length() >= 3);
        }
    }

    @Test
    public void testGetListOfServicesCSVHistoryParameterZeroException() {
        ListOfServicesRequest listOfServicesRequest = ListOfServicesRequest.builder().historyAmountInDays(0L).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getListOfServicesCSV", listOfServicesRequest, String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServicesCSVHistoryParameterMoreThanMaximumException() {
        ListOfServicesRequest listOfServicesRequest = ListOfServicesRequest.builder().historyAmountInDays(91L).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getListOfServicesCSV", listOfServicesRequest, String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServicesCSVHistoryParameterNullException() {
        ListOfServicesRequest listOfServicesRequest = ListOfServicesRequest.builder().historyAmountInDays(null).build();
        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/getListOfServicesCSV", listOfServicesRequest, String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

}
