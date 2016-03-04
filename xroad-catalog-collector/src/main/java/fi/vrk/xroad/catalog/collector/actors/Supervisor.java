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
import fi.vrk.xroad.catalog.persistence.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Supervisor to get list of all clients in system and initiate a ClientActor for each
 * <p/>
 * A router is configured at startup time, managing a pool of task actors.
 */
@Component
@Scope("prototype")
@Slf4j
public class Supervisor extends UntypedActor {

    public static final String START_COLLECTING = "StartCollecting";

    public static final String LIST_CLIENTS_ACTOR_ROUTER = "list-clients-actor-router";
    public static final String LIST_METHODS_ACTOR_ROUTER = "list-methods-actor-router";
    public static final String FETCH_WSDL_ACTOR_ROUTER = "fetch-wsdl-actor-router";

    @Autowired
    private SpringExtension springExtension;

    @Autowired
    protected CatalogService catalogService;

    private ActorRef listClientsPoolRouter;
    private ActorRef listMethodsPoolRouter;
    private ActorRef fetchWsdlPoolRouter;

    @Override
    public void preStart() throws Exception {

        log.info("Starting up");

        listClientsPoolRouter = getContext().actorOf(new SmallestMailboxPool(1)
                .props(springExtension.props("listClientsActor")),
                LIST_CLIENTS_ACTOR_ROUTER);

        listMethodsPoolRouter = getContext().actorOf(new SmallestMailboxPool(5)
                        .props(springExtension.props("listMethodsActor")),
                LIST_METHODS_ACTOR_ROUTER);

        fetchWsdlPoolRouter = getContext().actorOf(new SmallestMailboxPool(10)
                        .props(springExtension.props("fetchWsdlActor")),
                FETCH_WSDL_ACTOR_ROUTER);

        super.preStart();
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (START_COLLECTING.equals(message)) {
            listClientsPoolRouter.tell(ListClientsActor.START_COLLECTING, getSelf());
        } else if (message instanceof Terminated) {
            throw new RuntimeException("Terminated: " + message);
        } else {
            log.error("Unable to handle message {}", message);
        }
    }

    @Override
    public void postStop() throws Exception {
        log.info("postStop");
        super.postStop();
    }

}