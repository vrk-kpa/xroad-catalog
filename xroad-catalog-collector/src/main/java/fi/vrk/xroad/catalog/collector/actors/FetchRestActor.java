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
package fi.vrk.xroad.catalog.collector.actors;

import fi.vrk.xroad.catalog.collector.util.ClientTypeUtil;
import fi.vrk.xroad.catalog.collector.util.Endpoint;
import fi.vrk.xroad.catalog.collector.util.MethodListUtil;
import fi.vrk.xroad.catalog.collector.util.XRoadRestServiceIdentifierType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.ServiceId;
import fi.vrk.xroad.catalog.persistence.entity.SubsystemId;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Scope("prototype")
@Slf4j
public class FetchRestActor extends XRoadCatalogActor {

    private static AtomicInteger restCounter = new AtomicInteger(0);

    private static final String METHOD = "method";

    private static final String PATH = "path";

    @Value("${xroad-catalog.security-server-host}")
    private String xroadSecurityServerHost;

    @Value("${xroad-catalog.xroad-instance}")
    private String xroadInstance;

    @Value("${xroad-catalog.member-code}")
    private String memberCode;

    @Value("${xroad-catalog.member-class}")
    private String memberClass;

    @Value("${xroad-catalog.subsystem-code}")
    private String subsystemCode;

    @Value("${xroad-catalog.webservices-endpoint}")
    private String webservicesEndpoint;

    @Autowired
    protected CatalogService catalogService;

    @Override
    protected boolean handleMessage(Object message) {
        if (message instanceof XRoadRestServiceIdentifierType) {
            XRoadRestServiceIdentifierType service = (XRoadRestServiceIdentifierType) message;
            log.info("Fetching rest [{}] {}", restCounter.addAndGet(1), ClientTypeUtil.toString(service));
            List<fi.vrk.xroad.catalog.collector.util.Endpoint> endpointList = MethodListUtil.getEndpointList(service);
            String endpointData = "{\"endpoint_data\":";
            JSONArray endPointsJSONArray = new JSONArray();
            JSONObject endpointJson;
            catalogService.prepareEndpoints(createSubsystemId(service), createServiceId(service));
            for (Endpoint endpoint: endpointList) {
                endpointJson = new JSONObject();
                endpointJson.put(METHOD, endpoint.getMethod());
                endpointJson.put(PATH, endpoint.getPath());
                endPointsJSONArray.put(endpointJson);
                catalogService.saveEndpoint(createSubsystemId(service), createServiceId(service), endpoint.getMethod(), endpoint.getPath());
            }
            endpointData += endPointsJSONArray + "}";
            catalogService.saveRest(createSubsystemId(service), createServiceId(service), endpointData);
            log.info("Saved rest successfully");
            return true;
        } else {
            return false;
        }
    }

    private ServiceId createServiceId(XRoadRestServiceIdentifierType service) {
        return new ServiceId(service.getServiceCode(),
                service.getServiceVersion());
    }

    private SubsystemId createSubsystemId(XRoadRestServiceIdentifierType service) {
        return new SubsystemId(service.getXRoadInstance(),
                service.getMemberClass(),
                service.getMemberCode(),
                service.getSubsystemCode());
    }

}
