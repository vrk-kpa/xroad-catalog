package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import eu.x_road.xsd.identifiers.XRoadObjectType;
import eu.x_road.xsd.xroad.ClientListType;
import eu.x_road.xsd.xroad.ClientType;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import fi.vrk.xroad.catalog.collector.util.ClientTypeUtil;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.MemberId;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Actor which fetches all clients, and delegates listing
 * their methods to ListMethodsActors
 */
@Component
@Scope("prototype")
@Slf4j
public class ListClientsActor extends UntypedActor {

    public static final String START_COLLECTING = "StartCollecting";

    private static AtomicInteger COUNTER = new AtomicInteger(0);
    // to test fault handling
    private static boolean FORCE_FAILURES = false;

    private static final int NO_OF_ACTORS = 5;

    @Autowired
    @Qualifier("listClientsRestOperations")
    private RestOperations restOperations;

    @Autowired
    private SpringExtension springExtension;

    @Autowired
    protected CatalogService catalogService;


    @Value("${xroad-catalog.list-clients-url}")
    private String listClientsUrl;


    // supervisor-created pool of list methods actors
    protected ActorRef listMethodsPoolRef;

    @Override
    public void preStart() throws Exception {
        log.info("preStart {}", this.hashCode());
        listMethodsPoolRef = new RelativeActorRefUtil(getContext())
                .resolvePoolRef(Supervisor.LIST_METHODS_ACTOR_ROUTER);
        super.preStart();
    }

    @Override
    public void postStop() throws Exception {
        log.info("postStop {}", this.hashCode());
        super.postStop();
    }

    private void maybeFail() {
        if (FORCE_FAILURES) {
            if (COUNTER.get() % 3 == 0) {
                log.info("sending test failure {}", hashCode());
                throw new RuntimeException("test failure at " + hashCode());
            }
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (START_COLLECTING.equals(message)) {

            log.info("Getting client list from {}", listClientsUrl);
            ClientListType clientList = restOperations.getForObject(listClientsUrl, ClientListType
                    .class);

            int counter = 1;
            HashMap<MemberId, Member> m = new HashMap();

            List<Subsystem> subsystems = new ArrayList<>();
            for (ClientType clientType : clientList.getMember()) {
                log.info("{} - ClientType {}  ", counter++, ClientTypeUtil.toString(clientType));
                Member newMember = new Member(clientType.getId().getXRoadInstance(), clientType.getId()
                        .getMemberClass(),
                        clientType.getId().getMemberCode(), clientType.getName());
                newMember.setSubsystems(new HashSet<>());
                if (m.get(newMember.createKey()) == null) {
                    m.put(newMember.createKey(), newMember);
                }

                if (XRoadObjectType.SUBSYSTEM.equals(clientType.getId().getObjectType())) {
                    Subsystem newSubsystem = new Subsystem(newMember, clientType.getId().getSubsystemCode());
                    m.get(newMember.createKey()).getAllSubsystems().add(newSubsystem);
                }
            }

            // Save members
            catalogService.saveAllMembersAndSubsystems(m.values());
            for (ClientType clientType : clientList.getMember()) {
                if (XRoadObjectType.SUBSYSTEM.equals(clientType.getId().getObjectType())) {
                    listMethodsPoolRef.tell(clientType, getSelf());
                }
            }
            log.info("all clients (" + (counter-1) + ") sent to actor");
        } else if (message instanceof Terminated) {
            throw new RuntimeException("Terminated: " + message);
        } else {
            log.error("Unable to handle message {}", message);
        }
    }
}
