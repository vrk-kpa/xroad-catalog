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
package fi.vrk.xroad.catalog.collector.actors;

import fi.vrk.xroad.catalog.collector.util.ClientTypeUtil;
import fi.vrk.xroad.catalog.collector.util.MethodListUtil;
import fi.vrk.xroad.catalog.collector.util.XRoadClient;
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

/**
 * Actor which fetches all clients, and delegates listing
 * their methods to ListMethodsActors
 */
@Component
@Scope("prototype")
@Slf4j
public class ListMethodsActor extends XRoadCatalogActor {

    private static AtomicInteger COUNTER = new AtomicInteger(0);

    private static boolean organizationsFetched = false;

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

    @Value("${xroad-catalog.fetch-companies-time-after-hour}")
    private Integer fetchCompaniesTimeAfterHour;

    @Value("${xroad-catalog.fetch-companies-time-before-hour}")
    private Integer fetchCompaniesTimeBeforeHour;

    @Value("${xroad-catalog.fetch-companies-run-unlimited}")
    private Boolean fetchCompaniesUnlimited;

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
    private ActorRef fetchOrganizationsPoolRef;
    private ActorRef fetchCompaniesPoolRef;
    private XRoadClient xroadClient;

    public ListMethodsActor(ActorRef fetchWsdlPoolRef,
                            ActorRef fetchOpenApiPoolRef,
                            ActorRef fetchOrganizationsPoolRef,
                            ActorRef fetchCompaniesPoolRef) {
        this.fetchWsdlPoolRef = fetchWsdlPoolRef;
        this.fetchOpenApiPoolRef = fetchOpenApiPoolRef;
        this.fetchOrganizationsPoolRef = fetchOrganizationsPoolRef;
        this.fetchCompaniesPoolRef = fetchCompaniesPoolRef;
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

            log.info("{} onReceive {}", COUNTER.addAndGet(1), this.hashCode());
            ClientType clientType = (ClientType) message;

            // Fetch organizations only once, not for each client
            if (!organizationsFetched) {
                fetchOrganizationsPoolRef.tell(clientType, getSelf());
                organizationsFetched = true;
            }

            // Fetch companies only during a limited period if not unlimited
            if (MethodListUtil.shouldFetchCompanies(fetchCompaniesUnlimited,
                    fetchCompaniesTimeAfterHour, fetchCompaniesTimeBeforeHour)) {
                fetchCompaniesPoolRef.tell(clientType, getSelf());
            }

            // Flush errorLog entries only during a limited period
            if (MethodListUtil.shouldFlushLogEntries(flushLogTimeAfterHour, flushLogTimeBeforeHour)) {
                catalogService.deleteOldErrorLogEntries(errorLogLengthInDays);
            }

            if (XRoadObjectType.SUBSYSTEM.equals(clientType.getId().getObjectType())) {

                Subsystem subsystem = new Subsystem(
                        new Member(clientType.getId().getXRoadInstance(), clientType.getId().getMemberClass(),
                                clientType.getId().getMemberCode(), clientType.getName()),
                        clientType.getId().getSubsystemCode());

                log.info("{} Handling subsystem {} ", COUNTER, subsystem);

                List<XRoadServiceIdentifierType> restServices = MethodListUtil.methodListFromResponse(clientType,
                        xroadSecurityServerHost, catalogService);
                log.info("Received all REST methods for client {} ", ClientTypeUtil.toString(clientType));

                // fetch the methods
                //List<XRoadServiceIdentifierType> soapServices = xroadClient.getMethods(clientType.getId());
                //log.info("Received all SOAP methods for client {} ", ClientTypeUtil.toString(clientType));

                // Save services for subsystems
                List<Service> services = new ArrayList<>();
                for (XRoadServiceIdentifierType service : restServices) {
                    services.add(new Service(subsystem, service.getServiceCode(), service.getServiceVersion()));
                }
                //for (XRoadServiceIdentifierType service : soapServices) {
                //    services.add(new Service(subsystem, service.getServiceCode(), service.getServiceVersion()));
                //}

                catalogService.saveServices(subsystem.createKey(), services);

                // get wsdls
                //for (XRoadServiceIdentifierType service : soapServices) {
                //    fetchWsdlPoolRef.tell(service, getSender());
                //}

                // get openApis
                //for (XRoadServiceIdentifierType service : restServices) {
                //    fetchOpenApiPoolRef.tell(service, getSender());
                //}
            }

            return true;

        } else {
            return false;
        }
    }
}
