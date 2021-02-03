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
package fi.vrk.xroad.catalog.collector.util;

import fi.vrk.xroad.catalog.collector.wsimport.ClientList;
import fi.vrk.xroad.catalog.collector.wsimport.ClientType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadClientIdentifierType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadObjectType;

import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.ErrorLog;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


/**
 * Helper for client list
 */
public class ClientListUtil {


    private ClientListUtil() {
        // Private empty constructor
    }

    public static ClientList clientListFromResponse(String url, CatalogService catalogService) {
        RestTemplate restTemplate = new RestTemplate();
        JSONArray members = new JSONArray();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject json = new JSONObject(response.getBody());
            members = json.getJSONArray("member");
        } catch (Exception e) {
            ErrorLog errorLog = MethodListUtil.createErrorLog(null,
                    "Error when fetching listClients(url: " + url + "): " + e.getMessage(),
                    "500");
            catalogService.saveErrorLog(errorLog);
        }

        ClientList clientList = new ClientList();
        for (int i = 0; i < members.length(); i++)
        {
            ClientType clientType = new ClientType();
            JSONObject id = members.getJSONObject(i).getJSONObject("id");
            XRoadClientIdentifierType xRoadClientIdentifierType = new XRoadClientIdentifierType();
            xRoadClientIdentifierType.setXRoadInstance(id.optString("xroad_instance"));
            xRoadClientIdentifierType.setMemberClass(id.optString("member_class"));
            xRoadClientIdentifierType.setMemberCode(id.optString("member_code"));
            xRoadClientIdentifierType.setSubsystemCode(id.optString("subsystem_code"));
            xRoadClientIdentifierType.setGroupCode(id.optString("group_code"));
            xRoadClientIdentifierType.setServiceCode(id.optString("service_code"));
            xRoadClientIdentifierType.setServiceVersion(id.optString("service_version"));
            xRoadClientIdentifierType.setSecurityCategoryCode(id.optString("security_category_code"));
            xRoadClientIdentifierType.setServerCode(id.optString("server_code"));
            xRoadClientIdentifierType.setObjectType(XRoadObjectType.fromValue(id.optString("object_type")));
            clientType.setId(xRoadClientIdentifierType);
            clientType.setName((String) members.getJSONObject(i).get("name"));
            clientList.getMember().add(clientType);
        }

        return clientList;
    }



}
