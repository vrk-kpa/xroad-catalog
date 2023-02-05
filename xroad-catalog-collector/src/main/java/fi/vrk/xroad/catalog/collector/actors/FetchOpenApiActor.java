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
package fi.vrk.xroad.catalog.collector.actors;

import fi.vrk.xroad.catalog.collector.util.ClientTypeUtil;
import fi.vrk.xroad.catalog.collector.util.Endpoint;
import fi.vrk.xroad.catalog.collector.util.MethodListUtil;
import fi.vrk.xroad.catalog.collector.util.XRoadClient;
import fi.vrk.xroad.catalog.collector.util.XRoadRestServiceIdentifierType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.ServiceId;
import fi.vrk.xroad.catalog.persistence.entity.SubsystemId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Scope("prototype")
@Slf4j
public class FetchOpenApiActor extends XRoadCatalogActor {

    private static AtomicInteger openApiCounter = new AtomicInteger(0);

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

    private XRoadClient xroadClient;

    @Override
    public void preStart() throws Exception {
        xroadClient = new XRoadClient(
                ClientTypeUtil.toSubsystem(xroadInstance, memberClass, memberCode, subsystemCode),
                new URL(webservicesEndpoint));
    }

    @Override
    protected boolean handleMessage(Object message) {
        if (message instanceof XRoadRestServiceIdentifierType) {
            XRoadRestServiceIdentifierType service = (XRoadRestServiceIdentifierType) message;
            log.info("Fetching openApi [{}] {}", openApiCounter.addAndGet(1), ClientTypeUtil.toString(service));
            String openApi = xroadClient.getOpenApi(service, xroadSecurityServerHost, xroadInstance, memberClass, memberCode, subsystemCode, catalogService);
            catalogService.saveOpenApi(createSubsystemId(service), createServiceId(service), openApi);
            List<Endpoint> endpointList = MethodListUtil.getEndpointList(service);
            catalogService.prepareEndpoints(createSubsystemId(service), createServiceId(service));
            for (Endpoint endpoint: endpointList) {
                catalogService.saveEndpoint(createSubsystemId(service), createServiceId(service), endpoint.getMethod(), endpoint.getPath());
            }
            log.info("Saved openApi successfully");
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
