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

import fi.vrk.xroad.catalog.collector.configuration.DevelopmentConfiguration;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import fi.vrk.xroad.catalog.collector.util.ClientListUtil;
import fi.vrk.xroad.catalog.collector.wsimport.ClientList;
import fi.vrk.xroad.catalog.collector.wsimport.ClientType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadClientIdentifierType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadObjectType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import akka.actor.ActorSystem;
import akka.actor.InternalActorRef;
import akka.testkit.javadsl.TestKit;
import akka.testkit.TestActorRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("development")
@SpringBootTest(classes = DevelopmentConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListClientsActorTest extends TestKit {


    @MockBean
    CatalogService catalogService;

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    SpringExtension springExtension;

    private InternalActorRef listMethodsPoolRef;

    ListClientsActor listClientsActor;

    static ActorSystem _system;

    public ListClientsActorTest() {
        super(_system);
    }

    @BeforeTestClass
    public static void setupTest() {
        _system = ActorSystem.create();
    }

    @AfterTestClass
    public static void teardown() {
        TestKit.shutdownActorSystem(_system);
        _system = null;
    }

    @BeforeEach
    public void setup() throws Exception {
        listMethodsPoolRef = mock(InternalActorRef.class);
        TestActorRef<ListClientsActor> clientsRef = TestActorRef.create(actorSystem, springExtension.props("listClientsActor", listMethodsPoolRef));
        listClientsActor = clientsRef.underlyingActor();
        ReflectionTestUtils.setField(listClientsActor, "host", "http://localhost");
        ReflectionTestUtils.setField(listClientsActor, "fetchUnlimited", Boolean.TRUE);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnReceiveWhenFetchUnlimited() throws Exception {

        try (MockedStatic<ClientListUtil> mocked = Mockito.mockStatic(ClientListUtil.class)) {
            ClientList clientList = new ClientList();
            clientList.getMember().add(createClientType(XRoadObjectType.MEMBER, "member1", null));
            clientList.getMember().add(createClientType(XRoadObjectType.SUBSYSTEM, "member1", "sub1"));
            clientList.getMember().add(createClientType(XRoadObjectType.SUBSYSTEM, "member1", "sub2"));
            clientList.getMember().add(createClientType(XRoadObjectType.SUBSYSTEM, "member1", "sub3"));
            clientList.getMember().add(createClientType(XRoadObjectType.MEMBER, "member2", null));
            clientList.getMember().add(createClientType(XRoadObjectType.SUBSYSTEM, "member2", "sssub1"));
            clientList.getMember().add(createClientType(XRoadObjectType.SUBSYSTEM, "member2", "sssub2"));

            mocked.when(() -> ClientListUtil.clientListFromResponse(any(String.class), any(CatalogService.class))).thenReturn(clientList);

            listClientsActor.onReceive(ListClientsActor.START_COLLECTING);

            verify(catalogService, times(1)).saveAllMembersAndSubsystems(any());
        }
    }

    @Test
    public void testOnReceiveWhenFetchNotUnlimitedButTimeIsInBetween() throws Exception {
        try (MockedStatic mocked = mockStatic(ClientListUtil.class)) {
            ReflectionTestUtils.setField(listClientsActor, "host", "http://localhost");
            ReflectionTestUtils.setField(listClientsActor, "fetchUnlimited", Boolean.FALSE);
            ReflectionTestUtils.setField(listClientsActor, "fetchTimeAfterHour", 0);
            ReflectionTestUtils.setField(listClientsActor, "fetchTimeBeforeHour", 23);

            ClientList clientList = new ClientList();
            clientList.getMember().add(createClientType(XRoadObjectType.MEMBER, "member1", null));
            clientList.getMember().add(createClientType(XRoadObjectType.SUBSYSTEM, "member1", "sub1"));
            clientList.getMember().add(createClientType(XRoadObjectType.SUBSYSTEM, "member1", "sub2"));
            clientList.getMember().add(createClientType(XRoadObjectType.SUBSYSTEM, "member1", "sub3"));
            clientList.getMember().add(createClientType(XRoadObjectType.MEMBER, "member2", null));
            clientList.getMember().add(createClientType(XRoadObjectType.SUBSYSTEM, "member2", "sssub1"));
            clientList.getMember().add(createClientType(XRoadObjectType.SUBSYSTEM, "member2", "sssub2"));

            mocked.when(() -> ClientListUtil.clientListFromResponse(any(String.class), any(CatalogService.class))).thenReturn(clientList);

            listClientsActor.onReceive(ListClientsActor.START_COLLECTING);

            verify(catalogService, times(1)).saveAllMembersAndSubsystems(any());
        }
    }

    @Test
    public void testOnReceiveWithEmptyMemberList() throws Exception {
        try (MockedStatic mocked = mockStatic(ClientListUtil.class)) {
            ClientList clientList = new ClientList();
            mocked.when(() -> ClientListUtil.clientListFromResponse(any(String.class), any(CatalogService.class))).thenReturn(clientList);
            listClientsActor.onReceive(ListClientsActor.START_COLLECTING);
            verify(catalogService, times(1)).saveAllMembersAndSubsystems(any());
        }
    }

    @Test
    public void testSaveErrorLog() throws Exception {
        listClientsActor.onReceive(ListClientsActor.START_COLLECTING);
        verify(catalogService, times(1)).saveErrorLog(any());
    }

    protected ClientType createClientType(XRoadObjectType objectType, String memberCode, String subsystemCode) {
        ClientType c = new ClientType();
        XRoadClientIdentifierType xrcit = new XRoadClientIdentifierType();

        xrcit.setXRoadInstance("FI");
        xrcit.setMemberClass("GOV");
        xrcit.setMemberCode(memberCode);
        xrcit.setSubsystemCode(subsystemCode);
        xrcit.setObjectType(objectType);
        c.setId(xrcit);
        c.setName(memberCode);
        return c;

    }
}
