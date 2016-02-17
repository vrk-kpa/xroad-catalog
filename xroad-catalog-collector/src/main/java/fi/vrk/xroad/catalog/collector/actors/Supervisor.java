package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.Routee;
import akka.routing.Router;
import akka.routing.SmallestMailboxRoutingLogic;
import eu.x_road.xsd.xroad.ClientListType;
import eu.x_road.xsd.xroad.ClientType;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A sample supervisor which should handle exceptions and general feedback
 * for the actual {@link ClientActor}
 * <p/>
 * A router is configured at startup time, managing a pool of task actors.
 */
@Component
@Scope("prototype")
public class Supervisor extends UntypedActor {

    private final LoggingAdapter log = Logging
        .getLogger(getContext().system(), "Supervisor");

    @Autowired
    private SpringExtension springExtension;

    private Router router;

    @Autowired
    protected CatalogService catalogService;


    @Override
    public void preStart() throws Exception {

        log.info("Starting up");

        List<Routee> routees = new ArrayList<Routee>();
        for (int i = 0; i < 100; i++) {
            ActorRef actor = getContext().actorOf(springExtension.props(
                "clientActor"));
            getContext().watch(actor);
            routees.add(new ActorRefRoutee(actor));
        }
        router = new Router(new SmallestMailboxRoutingLogic(), routees);
        super.preStart();
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof ClientListType) {
            RestTemplate restTemplate = new RestTemplate();
            ClientListType clientList = restTemplate.getForObject("http://localhost/listClients.xml", ClientListType
                    .class);

            ActorRef actor = getContext().actorOf(springExtension.props
                    ("clientActor"));

            log.info("clientlist {}", clientList.toString());
            for (ClientType clientType : clientList.getMember()) {

                log.info("clientType {} {}", clientType.getName(), clientType.getId().getMemberCode());
                router.route(clientType, getSender());
            }


            //stream().forEach(c -> log.info("clientType {}", c));

        } else if (message instanceof Terminated) {
            // Readd task actors if one failed
            router = router.removeRoutee(((Terminated) message).actor());
            ActorRef actor = getContext().actorOf(springExtension.props
                ("clientListerActor"));
            getContext().watch(actor);
            router = router.addRoutee(new ActorRefRoutee(actor));
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