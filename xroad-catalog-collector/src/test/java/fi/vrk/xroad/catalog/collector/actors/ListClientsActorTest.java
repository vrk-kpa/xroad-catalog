/**
 * The MIT License
 * Copyright (c) 2016, Population Register Centre (VRK)
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

import akka.actor.ActorSystem;
import akka.actor.InternalActorRef;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import akka.testkit.TestKit;
import com.google.common.collect.HashMultiset;
import fi.vrk.xroad.catalog.collector.wsimport.ClientList;
import fi.vrk.xroad.catalog.collector.wsimport.ClientType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadClientIdentifierType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadObjectType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.CatalogServiceImpl;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestOperations;

import java.util.*;

import static org.mockito.Mockito.*;

/**
 * Test for client actor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ListClientsActor.class})
@ActiveProfiles("development")
public class ListClientsActorTest extends TestKit {

    @Mock
    private CatalogService catalogService = new CatalogServiceImpl();

    @Mock
    private RestOperations restOperations;


    private InternalActorRef listMethodsPoolRef;

    @InjectMocks
    protected ListClientsActor listClientsActor;

    @Captor ArgumentCaptor<Collection<Member>> argumentCaptor;

    static ActorSystem _system;

    public ListClientsActorTest() {
        super(_system);
    }

    @BeforeClass
    public static void setupTest() {
        _system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        JavaTestKit.shutdownActorSystem(_system);
        _system = null;
    }

    /**
     * Setup test
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        listMethodsPoolRef = PowerMockito.mock(InternalActorRef.class);

        final Props clientsProps = Props.create(ListClientsActor.class, listMethodsPoolRef);
        final TestActorRef<ListClientsActor> clientsRef = TestActorRef.apply(clientsProps, _system);

        listClientsActor = clientsRef.underlyingActor();

        ReflectionTestUtils.setField(listClientsActor, "host", "http://localhost");

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnReceive() throws Exception {



        List<ClientType> memberlist = new ArrayList<>();


        memberlist.add(createClientType(XRoadObjectType.MEMBER, "member1", null));
        memberlist.add(createClientType(XRoadObjectType.SUBSYSTEM, "member1", "sub1"));
        memberlist.add(createClientType(XRoadObjectType.SUBSYSTEM, "member1", "sub2"));
        memberlist.add(createClientType(XRoadObjectType.SUBSYSTEM, "member1", "sub3"));

        memberlist.add(createClientType(XRoadObjectType.MEMBER, "member2", null));
        memberlist.add(createClientType(XRoadObjectType.SUBSYSTEM, "member2", "sssub1"));
        memberlist.add(createClientType(XRoadObjectType.SUBSYSTEM, "member2", "sssub2"));

        ClientList cMock = mock(ClientList.class);
        doReturn(cMock).when(restOperations).getForObject("http://localhost/listClients", ClientList.class);
        when(cMock.getMember()).thenReturn(memberlist);

//        doNothing().when(listMethodsPoolRef).tell(Matchers.anyObject(), Matchers.anyObject());
    //    doNothing().when(listMethodsPoolRef).tell(Matchers.any(ClientType.class), Matchers.any(ActorRef.class));

        // Call onReceive
        listClientsActor.onReceive(ListClientsActor.START_COLLECTING);

        Set<Member> expectedMembers = new HashSet<>();
        Member member1 = new Member("FI", "GOV", "member1", "member1");
        Set<Subsystem> subsystems = new HashSet<>();
        subsystems.add(new Subsystem(member1, "sub1"));
        subsystems.add(new Subsystem(member1, "sub2"));
        subsystems.add(new Subsystem(member1, "sub3"));
        member1.setSubsystems(subsystems);



        Member member2 = new Member("FI", "GOV", "member2", "member2");
        subsystems = new HashSet<>();
        subsystems.add(new Subsystem(member2, "sssub1"));
        subsystems.add(new Subsystem(member2, "sssub2"));
        member2.setSubsystems(subsystems);

        expectedMembers.add(member1);
        expectedMembers.add(member2);

        // Verify that the save method was called with correct member collection
        verify(catalogService).saveAllMembersAndSubsystems(argumentCaptor.capture());

        Collection<Member> resultMembers = argumentCaptor.getValue();
        Assert.assertEquals(HashMultiset.create(expectedMembers), HashMultiset.create(resultMembers));

        Assert.assertEquals(HashMultiset.create(member1.getAllSubsystems()), HashMultiset.create(resultMembers.stream()
                .filter(m -> member1.equals(m)).findAny().get().getAllSubsystems()));

        Assert.assertEquals(HashMultiset.create(member2.getAllSubsystems()), HashMultiset.create(resultMembers.stream()
                .filter(m -> member2.equals(m)).findAny().get().getAllSubsystems()));
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
