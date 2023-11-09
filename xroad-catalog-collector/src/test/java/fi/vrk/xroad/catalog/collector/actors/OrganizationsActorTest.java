/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2016-2023 Finnish Digital Agency
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
import akka.actor.InternalActorRef;
import akka.testkit.TestActorRef;
import fi.vrk.xroad.catalog.collector.configuration.DevelopmentConfiguration;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import fi.vrk.xroad.catalog.collector.wsimport.ClientType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadClientIdentifierType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadObjectType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = DevelopmentConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "xroad-catalog.fetch-organizations-limit=3",
        "xroad-catalog.fetch-companies-limit=3",
        "xroad-catalog.max-organizations-per-request=3",
        "xroad-catalog.fetch-companies-run-unlimited=true",
        "xroad-catalog.flush-log-time-after-hour=0",
        "xroad-catalog.flush-log-time-before-hour=23"
})
public class OrganizationsActorTest {

    @MockBean
    CatalogService catalogService;

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    SpringExtension springExtension;

    @Test
    public void testBasicPlumbing() {
        TestActorRef listMethodsActor = TestActorRef.create(actorSystem,
                springExtension.props("organizationsActor",
                        mock(InternalActorRef.class),
                        mock(InternalActorRef.class),
                        mock(InternalActorRef.class),
                        mock(InternalActorRef.class),
                        mock(InternalActorRef.class)));
        ClientType clientType = new ClientType();
        XRoadClientIdentifierType value = new XRoadClientIdentifierType();
        value.setXRoadInstance("INSTANCE");
        value.setMemberClass("CLASS");
        value.setMemberCode("CODE");
        value.setSubsystemCode("SUBSYSTEM");
        value.setServiceCode("aService");
        value.setServiceVersion("v1");
        value.setObjectType(XRoadObjectType.SUBSYSTEM);
        clientType.setId(value);
        listMethodsActor.tell(clientType, ActorRef.noSender());
        verify(catalogService, times(1)).saveServices(any(), any());
    }

    @Test
    public void testBasicPlumbingWithWrongMessageType() {
        TestActorRef listMethodsActor = TestActorRef.create(actorSystem,
                springExtension.props("organizationsActor",
                        mock(InternalActorRef.class),
                        mock(InternalActorRef.class),
                        mock(InternalActorRef.class),
                        mock(InternalActorRef.class),
                        mock(InternalActorRef.class)));
        listMethodsActor.tell("Wrong message type object", ActorRef.noSender());
        verify(catalogService, times(0)).saveServices(any(), any());
    }

}

