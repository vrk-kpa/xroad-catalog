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
import fi.vrk.xroad.catalog.collector.util.MethodListUtil;
import fi.vrk.xroad.catalog.collector.util.XRoadClient;
import fi.vrk.xroad.catalog.collector.util.XRoadRestServiceIdentifierType;
import fi.vrk.xroad.catalog.collector.wsimport.ClientType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadObjectType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadServiceIdentifierType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import akka.actor.ActorRef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Scope("prototype")
@Slf4j
public class ListMethodsActor extends XRoadCatalogActor {

    private static AtomicInteger methodCounter = new AtomicInteger(0);

    private static final String SERVICE_TYPE_REST = "REST";

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

    @Value("${xroad-catalog.error-log-length-in-days}")
    private Integer errorLogLengthInDays;

    @Value("${xroad-catalog.flush-log-time-after-hour}")
    private Integer flushLogTimeAfterHour;

    @Value("${xroad-catalog.flush-log-time-before-hour}")
    private Integer flushLogTimeBeforeHour;

    @Autowired
    protected CatalogService catalogService;

    // supervisor-created pool of list methods actors
    private ActorRef fetchWsdlPoolRef;
    private ActorRef fetchOpenApiPoolRef;
    private ActorRef fetchRestPoolRef;
    private XRoadClient xroadClient;

    public ListMethodsActor(ActorRef fetchWsdlPoolRef, ActorRef fetchOpenApiPoolRef, ActorRef fetchRestPoolRef) {
        this.fetchWsdlPoolRef = fetchWsdlPoolRef;
        this.fetchOpenApiPoolRef = fetchOpenApiPoolRef;
        this.fetchRestPoolRef = fetchRestPoolRef;
    }

    @Override
    public void preStart() throws Exception {
        xroadClient = new XRoadClient(
                ClientTypeUtil.toSubsystem(xroadInstance, memberClass, memberCode, subsystemCode),
                new URL(webservicesEndpoint));
    }

    @Override
    protected boolean handleMessage(Object message) {
        if (message instanceof ClientType) {
            log.info("{} onReceive {}", methodCounter.addAndGet(1), this.hashCode());
            ClientType clientType = (ClientType) message;
            flushErrorLogs();
            if (XRoadObjectType.SUBSYSTEM.equals(clientType.getId().getObjectType())) {
                saveSubsystemsAndServices(clientType);
            }
            return true;
        } else {
            return false;
        }
    }

    private void flushErrorLogs() {
        if (MethodListUtil.shouldFlushLogEntries(flushLogTimeAfterHour, flushLogTimeBeforeHour)) {
            catalogService.deleteOldErrorLogEntries(errorLogLengthInDays);
        }
    }

    private void saveSubsystemsAndServices(ClientType clientType) {
        Subsystem subsystem = new Subsystem(
                new Member(clientType.getId().getXRoadInstance(), clientType.getId().getMemberClass(),
                        clientType.getId().getMemberCode(), clientType.getName()),
                clientType.getId().getSubsystemCode());

        log.info("{} Handling subsystem {} ", methodCounter, subsystem);

        List<XRoadRestServiceIdentifierType> restServices = MethodListUtil.methodListFromResponse(clientType,
                xroadSecurityServerHost, xroadInstance, memberClass, memberCode, subsystemCode, catalogService);
        log.info("Received all REST methods for client {} ", ClientTypeUtil.toString(clientType));

        List<XRoadServiceIdentifierType> soapServices = xroadClient.getMethods(clientType.getId(), catalogService);
        log.info("Received all SOAP methods for client {} ", ClientTypeUtil.toString(clientType));

        List<Service> services = new ArrayList<>();
        for (XRoadRestServiceIdentifierType service : restServices) {
            services.add(new Service(subsystem, service.getServiceCode(), service.getServiceVersion()));
        }
        for (XRoadServiceIdentifierType service : soapServices) {
            services.add(new Service(subsystem, service.getServiceCode(), service.getServiceVersion()));
        }

        catalogService.saveServices(subsystem.createKey(), services);

        for (XRoadServiceIdentifierType service : soapServices) {
            fetchWsdlPoolRef.tell(service, getSender());
        }

        for (XRoadRestServiceIdentifierType service : restServices) {
            if (service.getServiceType().equalsIgnoreCase(SERVICE_TYPE_REST)) {
                fetchRestPoolRef.tell(service, getSender());
            } else {
                fetchOpenApiPoolRef.tell(service, getSender());
            }
        }
    }
}
