package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.SmallestMailboxPool;
import akka.util.Timeout;
import eu.x_road.xsd.xroad.ClientListType;
import eu.x_road.xsd.xroad.ClientType;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import fi.vrk.xroad.catalog.collector.util.ClientTypeUtil;
import fi.vrk.xroad.catalog.collector.util.XRoadClient;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadServiceIdentifierType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import scala.concurrent.Await;
import scala.concurrent.CanAwait;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;
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
    private RestOperations restOperations;

    @Autowired
    private SpringExtension springExtension;

    @Autowired
    protected CatalogService catalogService;

    // supervisor-created pool of list methods actors
    private ActorRef listMethodsPoolRef;

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

            ClientListType clientList = restOperations.getForObject("http://localhost/listClients", ClientListType
                    .class);

            int counter = 1;
            for (ClientType clientType : clientList.getMember()) {
                log.info("{} - ClientType {}  ", counter++, ClientTypeUtil.toString(clientType));
                listMethodsPoolRef.tell(clientType, getSelf());
            }
            log.info("all clients (" + (counter-1) + ") sent to actor");
        } else if (message instanceof Terminated) {
            throw new RuntimeException("Terminated: " + message);
        } else {
            log.error("Unable to handle message {}", message);
        }
    }
}
