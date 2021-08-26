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
@TestPropertySource(properties = {"xroad-catalog.max-history-length-in-days=90",
                                  "xroad-catalog.shared-params-file=src/test/resources/shared-params.xml"})
public class ServiceControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

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
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetDistinctServiceStatisticsHistoryParameterMoreThanMaximumException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getDistinctServiceStatistics/91", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetDistinctServiceStatisticsHistoryParameterNullException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getDistinctServiceStatistics", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
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
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetServiceStatisticsHistoryParameterMoreThanMaximumException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatistics/91", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetServiceStatisticsHistoryParameterNullException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatistics", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
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
            assertEquals(5, csvRowContent.size());
            assertEquals(23, csvRowContent.get(0).length());
            assertTrue(Integer.parseInt(csvRowContent.get(1)) > 0);
            assertTrue(Integer.parseInt(csvRowContent.get(2)) > 0);
            assertTrue(Integer.parseInt(csvRowContent.get(3)) > 0);
            assertTrue(Integer.parseInt(csvRowContent.get(4)) > 0);
        }
    }

    @Test
    public void testGetServiceStatisticsCSVHistoryParameterZeroException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatisticsCSV/0", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetServiceStatisticsCSVHistoryParameterMoreThanMaximumException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatisticsCSV/91", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetServiceStatisticsCSVHistoryParameterNullException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getServiceStatisticsCSV", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
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
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServicesHistoryParameterMoreThanMaximumException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServices/91", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServicesHistoryParameterNullException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServices", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
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
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServicesCSVHistoryParameterMoreThanMaximumException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServicesCSV/91", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errMsg, response.getBody());
    }

    @Test
    public void testGetListOfServicesCSVHistoryParameterNullException() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/getListOfServicesCSV", String.class);
        String errMsg = "Input parameter historyAmountInDays must be greater than zero and less than the required maximum of 90 days";
        assertEquals(404, response.getStatusCodeValue());
    }

}
