package fi.vrk.xroad.catalog.collector.util;

import eu.x_road.metadata.ListMethods;
import eu.x_road.metadata.ListMethodsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

/**
 * Created by sjk on 25.2.2016.
 */
@Slf4j
public class XRoadClient extends WebServiceGatewaySupport {

    public ListMethodsResponse getMethods() {
        ListMethods request = new ListMethods();

        /*ListMethodsResponse clientList = restOperations.getForObject("http://localhost/listMethods",
                ListMethodsResponse
                .class);
*/

        ListMethodsResponse result = (ListMethodsResponse)getWebServiceTemplate().marshalSendAndReceive
                ("http://localhost/listMethods", request);
        log.info("ListMethodsResponse {} ", result.toString());
        return result;
    }
}
