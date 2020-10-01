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

}
