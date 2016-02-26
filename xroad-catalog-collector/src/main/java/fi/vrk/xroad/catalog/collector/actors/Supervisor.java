package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.SmallestMailboxPool;
import eu.x_road.xsd.xroad.ClientListType;
import eu.x_road.xsd.xroad.ClientType;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

/**
 * Supervisor to get list of all clients in system and initiate a ClientActor for each
 * <p/>
 * A router is configured at startup time, managing a pool of task actors.
 */
@Component
@Scope("prototype")
public class Supervisor extends UntypedActor {

    private static final int NO_OF_ACTORS = 1;

    private final LoggingAdapter log = Logging
        .getLogger(getContext().system(), "Supervisor");

    @Autowired
    private SpringExtension springExtension;

    private ActorRef router;

    @Autowired
    protected CatalogService catalogService;


    @Autowired
    private RestOperations restOperations;


    @Override
    public void preStart() throws Exception {

        log.info("Starting up");

        router = getContext().actorOf(new SmallestMailboxPool(NO_OF_ACTORS)
                .props(springExtension.props("clientActor")), "client-actor-router");

        // TODO: according to documentation, we should be defining supervisor strategy
        // so that the router (and all routees) are not restarted when actor fails.
        // however this does not work that way now, and something (correctly)
        // restarts only the failing actor
        // http://doc.akka.io/docs/akka/2.4.1/java/routing.html
        // This means that if you have not specified supervisorStrategy of the router or its parent a failure in a routee will escalate to the parent of the router, which will by default restart the router, which will restart all routees (it uses Escalate and does not stop routees during restart). The reason is to make the default behave such that adding withRouter to a child’s definition does not change the supervision strategy applied to the child. This might be an inefficiency that you can avoid by specifying the strategy when defining the router.

        super.preStart();
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof ClientListType) {

            ClientListType clientList = restOperations.getForObject("http://localhost/listClients", ClientListType
                    .class);

            log.info("clientlist {}", clientList.toString());
            int counter = 1;
            for (ClientType clientType : clientList.getMember()) {
                log.info("clientType {} {} {}", counter++, clientType.getName(), clientType.getId().getMemberCode());
                router.tell(clientType, getSender());
            }
            log.info("all clients (" + (counter-1) + ") sent to actor");


            //stream().forEach(c -> log.info("clientType {}", c));

        } else if (message instanceof Terminated) {
            throw new RuntimeException("Terminated: " + message);
        } else {
            log.error("Unable to handle message {}", message);
        }
    }

    @Override
    public void postStop() throws Exception {

        log.info("PostStop");
        log.info("Found members from database: {}", catalogService.getMembers());
        log.info("Shutting down");
        super.postStop();
    }
}