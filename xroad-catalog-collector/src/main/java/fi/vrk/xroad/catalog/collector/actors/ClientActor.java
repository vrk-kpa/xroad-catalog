package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.SmallestMailboxPool;
import eu.x_road.xsd.xroad.ClientType;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import fi.vrk.xroad.catalog.collector.util.XRoadClient;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadServiceIdentifierType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Scope("prototype")
public class ClientActor extends UntypedActor {

    private static AtomicInteger COUNTER = new AtomicInteger(0);
    // to test fault handling
    private static boolean FORCE_FAILURES = false;

    private static final int NO_OF_ACTORS = 1;



    @Autowired
    private SpringExtension springExtension;

    @Autowired
    private XRoadClient xRoadClient;

    @Autowired
    private RestOperations restOperations;


    private ActorRef router;

    private final LoggingAdapter log = Logging
        .getLogger(getContext().system(), "ClientActor");

    @Autowired
    protected CatalogService catalogService;

    @Override
    public void preStart() throws Exception {
        log.info("preStart {}", this.hashCode());

        router = getContext().actorOf(new SmallestMailboxPool(NO_OF_ACTORS)
                .props(springExtension.props("methodActor")), "method-actor-router");

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
        log.info("{} onReceive {}", COUNTER.addAndGet(1), this.hashCode());

        ClientType clientType = (ClientType)message;

        log.info("{} ClientType. XRoadInstance: {} memberCode: {} subsystemCode {}", COUNTER, clientType.getId()
                .getXRoadInstance()
                , clientType.getId().getMemberClass(),
                clientType.getId().getMemberCode());



        maybeFail();

        Member member = new Member(clientType.getId().getXRoadInstance(), clientType.getId().getMemberClass(),
                clientType.getId().getMemberCode(), clientType.getName());

        member = catalogService.saveMember(member);
        log.info("{} Member {} saved", COUNTER, member);

        List<XRoadServiceIdentifierType> result = XRoadClient.getMethods(clientType);

        log.info("{} ListMethodsResponse {} ", COUNTER, result.toString());

        maybeFail();
        for (XRoadServiceIdentifierType service : result){
            log.info("{} Sending service {} to new MethodActor ", COUNTER, service.getServiceCode());
            router.tell(service, getSender());
        }

    }
}
