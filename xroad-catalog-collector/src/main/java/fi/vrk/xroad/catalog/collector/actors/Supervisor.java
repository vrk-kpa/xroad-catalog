package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.*;
import akka.actor.SupervisorStrategy.Directive;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import akka.routing.SmallestMailboxPool;
import eu.x_road.xsd.xroad.ClientListType;
import eu.x_road.xsd.xroad.ClientType;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import scala.Option;
import scala.concurrent.duration.Duration;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;

/**
 * Supervisor to get list of all clients in system and initiate a ClientActor for each
 * <p/>
 * A router is configured at startup time, managing a pool of task actors.
 */
@Component
@Scope("prototype")
public class Supervisor extends UntypedActor {

    private static final int NO_OF_ACTORS = 3;

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

        // supervisor strategy restarts each clientActor if it fails.
        // currently this is not needed and could "resume" just as well
        router = getContext().actorOf(new SmallestMailboxPool(NO_OF_ACTORS)
                .withSupervisorStrategy(new OneForOneStrategy(-1,
                        Duration.Inf(),
                        (Throwable t) -> restart()))
            .props(springExtension.props("clientActor")), "client-actor-router");

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
                Thread.sleep(100);
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

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        log.info("preRestart {} {} {} ", this.hashCode(), reason, message);
        super.preRestart(reason, message);
    }

}