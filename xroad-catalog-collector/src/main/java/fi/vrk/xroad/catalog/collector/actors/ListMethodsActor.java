package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import scala.concurrent.Await;
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
public class ListMethodsActor extends UntypedActor {

    private static AtomicInteger COUNTER = new AtomicInteger(0);
    // to test fault handling
    private static boolean FORCE_FAILURES = false;

    @Value("${local.server.port}")
    private int port = 0;

    private static final int NO_OF_ACTORS = 5;

    @Autowired
    private RestOperations restOperations;

    @Autowired
    private SpringExtension springExtension;

    @Autowired
    protected CatalogService catalogService;

    // supervisor-created pool of list methods actors
    private ActorRef fetchWsdlPoolRef;

    @Override
    public void preStart() throws Exception {
        log.info("preStart {}", this.hashCode());
        fetchWsdlPoolRef = new RelativeActorRefUtil(getContext())
                .resolvePoolRef(Supervisor.FETCH_WSDL_ACTOR_ROUTER);
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

        if (message instanceof ClientType) {

            log.info("{} onReceive {}", COUNTER.addAndGet(1), this.hashCode());
            ClientType clientType = (ClientType) message;

            // save member to DB
            Member member = new Member(clientType.getId().getXRoadInstance(), clientType.getId().getMemberClass(),
                    clientType.getId().getMemberCode(), clientType.getName());

            // no such method anymore...this class needs to be rewritten...
//            member = catalogService.saveMember(member);
            log.info("{} Member {} saved", COUNTER, member);
            log.info("Fetching methods for the client with listMethods -service...");

            // fetch the methods
            List<XRoadServiceIdentifierType> result = XRoadClient.getMethods(clientType);

            log.info("Received all methods for client {} ", clientType);
            log.info("{} ListMethodsResponse {} ", COUNTER, result.toString());

            maybeFail();

            // get wsdls
            for (XRoadServiceIdentifierType service : result) {
                log.info("{} Sending service {} to new MethodActor ", COUNTER, service.getServiceCode());
                fetchWsdlPoolRef.tell(service, getSender());
            }

        } else if (message instanceof Terminated) {
            throw new RuntimeException("Terminated: " + message);
        } else {
            log.error("Unable to handle message {}", message);
        }


    }
}
