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

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestActorRef;
import fi.vrk.xroad.catalog.collector.configuration.DevelopmentConfiguration;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import fi.vrk.xroad.catalog.collector.util.Endpoint;
import fi.vrk.xroad.catalog.collector.util.XRoadRestServiceIdentifierType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadObjectType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = DevelopmentConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "xroad-catalog.webservices-endpoint=http://localhost:${local.server.port}/metaservices"
})
public class FetchOpenApiActorTest {

    @MockBean
    CatalogService catalogService;

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    SpringExtension springExtension;

    @Test
    public void testBasicPlumbing() {
        TestActorRef fetchOpenApiActor = TestActorRef.create(actorSystem, springExtension.props("fetchOpenApiActor"));
        XRoadRestServiceIdentifierType service = new XRoadRestServiceIdentifierType();
        service.setObjectType(XRoadObjectType.SERVICE);
        service.setXRoadInstance("INSTANCE");
        service.setMemberClass("CLASS");
        service.setMemberCode("CODE");
        service.setSubsystemCode("SUBSYSTEM");
        service.setServiceCode("aService");
        service.setServiceVersion("v1");
        service.setServiceType("OPENAPI");
        List<Endpoint> endpointList = new ArrayList<>();
        Endpoint endpoint = new Endpoint();
        endpoint.setMethod("GET");
        endpoint.setPath("/getServices");
        endpointList.add(endpoint);
        service.setEndpoints(endpointList);
        fetchOpenApiActor.tell(service, ActorRef.noSender());
        verify(catalogService, times(1)).saveOpenApi(any(), any(), any());
        verify(catalogService, times(1)).saveEndpoint(any(), any(), any(), any());
    }
}

