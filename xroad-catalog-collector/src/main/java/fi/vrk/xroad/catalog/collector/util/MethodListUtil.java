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
package fi.vrk.xroad.catalog.collector.util;

import fi.vrk.xroad.catalog.collector.wsimport.*;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.ErrorLog;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper for method list
 */
@Slf4j
public class MethodListUtil {

    private static SecurityServerMetadata securityServerMetadata;

    private MethodListUtil() {
        // Private empty constructor
    }

    public static Boolean shouldFetchCompanies(boolean fetchUnlimited, int fetchHourAfter, int fetchHourBefore) {
        if (fetchUnlimited) {
            return true;
        }
        return isTimeBetweenHours(fetchHourAfter, fetchHourBefore);
    }

    public static Boolean shouldFlushLogEntries(int fetchHourAfter, int fetchHourBefore) {
        return isTimeBetweenHours(fetchHourAfter, fetchHourBefore);
    }

    public static List<XRoadServiceIdentifierType> methodListFromResponse(ClientType clientType, String host, CatalogService catalogService) {
        final String url = new StringBuilder().append(host).append("/r1/")
                .append(clientType.getId().getXRoadInstance()).append("/")
                .append(clientType.getId().getMemberClass()).append("/")
                .append(clientType.getId().getMemberCode()).append("/")
                .append(clientType.getId().getSubsystemCode()).append("/listMethods").toString();

        List<XRoadServiceIdentifierType> restServices = new ArrayList<>();
        JSONObject json = MethodListUtil.getJSON(url, clientType, catalogService);
        if (json != null) {
            JSONArray serviceList = json.getJSONArray("service");
            for (int i = 0; i < serviceList.length(); i++) {
                JSONObject service = serviceList.getJSONObject(i);
                XRoadServiceIdentifierType xRoadServiceIdentifierType = new XRoadServiceIdentifierType();
                xRoadServiceIdentifierType.setMemberCode(service.optString("member_code"));
                xRoadServiceIdentifierType.setSubsystemCode(service.optString("subsystem_code"));
                xRoadServiceIdentifierType.setMemberClass(service.optString("member_class"));
                xRoadServiceIdentifierType.setServiceCode(service.optString("service_code"));
                xRoadServiceIdentifierType.setServiceVersion(service.optString("service_version"));
                xRoadServiceIdentifierType.setXRoadInstance(service.optString("xroad_instance"));
                xRoadServiceIdentifierType.setObjectType(XRoadObjectType.fromValue(service.optString("object_type")));
                restServices.add(xRoadServiceIdentifierType);
            }
        }

        return restServices;
    }

    public static String openApiFromResponse(ClientType clientType, String host, CatalogService catalogService) {
        final String url = new StringBuilder().append(host).append("/r1/")
                .append(clientType.getId().getXRoadInstance()).append("/")
                .append(clientType.getId().getMemberClass()).append("/")
                .append(clientType.getId().getMemberCode()).append("/")
                .append(clientType.getId().getSubsystemCode()).append("/getOpenAPI?serviceCode=")
                .append(clientType.getId().getServiceCode()).toString();

        JSONObject json = MethodListUtil.getJSON(url, clientType, catalogService);

        return json.toString();
    }

    public static ErrorLog createErrorLog(ClientType clientType, String message, String code) {
        if (clientType != null) {
            return ErrorLog.builder()
                    .created(LocalDateTime.now())
                    .message(message)
                    .code(code)
                    .xRoadInstance(clientType.getId().getXRoadInstance())
                    .memberClass(clientType.getId().getMemberClass())
                    .memberCode(clientType.getId().getMemberCode())
                    .groupCode(clientType.getId().getGroupCode())
                    .securityCategoryCode(clientType.getId().getSecurityCategoryCode())
                    .serverCode(clientType.getId().getServerCode())
                    .serviceCode(clientType.getId().getServiceCode())
                    .serviceVersion(clientType.getId().getServiceVersion())
                    .subsystemCode(clientType.getId().getSubsystemCode())
                    .build();
        }
        return ErrorLog.builder().created(LocalDateTime.now()).message(message).code(code).build();
    }

    private static boolean isTimeBetweenHours(int fetchHourAfter, int fetchHourBefore) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime fetchTimeFrom = LocalDate.now().atTime(fetchHourAfter, 0);
        LocalDateTime fetchTimeTo = LocalDate.now().atTime(fetchHourBefore, 0);
        return (today.isAfter(fetchTimeFrom) && today.isBefore(fetchTimeTo));
    }

    private static String createHeader(ClientType clientType) {
        return new StringBuilder()
                .append(clientType.getId().getXRoadInstance()).append("/")
                .append(clientType.getId().getMemberClass()).append("/")
                .append(clientType.getId().getMemberCode()).append("/")
                .append(clientType.getId().getSubsystemCode()).toString();
    }

    private static JSONObject getJSON(String url, ClientType clientType, CatalogService catalogService) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        headers.setAccept(mediaTypes);
        headers.set("X-Road-Client", MethodListUtil.createHeader(clientType));
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JSONObject json = new JSONObject(response.getBody());
            return json;
        } catch (Exception e) {
            SecurityServerMetadata newSecurityServerMetadata = SecurityServerMetadata.builder()
                    .xRoadInstance(clientType.getId().getXRoadInstance())
                    .memberClass(clientType.getId().getMemberClass())
                    .memberCode(clientType.getId().getMemberCode())
                    .build();
            if (!newSecurityServerMetadata.equals(securityServerMetadata)) {
                log.error("Fetch of REST services failed: " + e.getMessage());
                ErrorLog errorLog = createErrorLog(clientType,
                        "Fetch of REST services failed(url: " + url + "): " + e.getMessage(),
                        "500");
                catalogService.saveErrorLog(errorLog);
                securityServerMetadata = SecurityServerMetadata.builder()
                        .xRoadInstance(clientType.getId().getXRoadInstance())
                        .memberClass(clientType.getId().getMemberClass())
                        .memberCode(clientType.getId().getMemberCode())
                        .build();
            }
            return null;
        }
    }
}
