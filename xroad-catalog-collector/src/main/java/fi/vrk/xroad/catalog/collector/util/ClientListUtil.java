package fi.vrk.xroad.catalog.collector.util;

import fi.vrk.xroad.catalog.collector.wsimport.ClientList;
import fi.vrk.xroad.catalog.collector.wsimport.ClientType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadClientIdentifierType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadObjectType;

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


    public static ClientList clientListFromResponse(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONObject json = new JSONObject(response.getBody());
        JSONArray members = json.getJSONArray("member");

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
