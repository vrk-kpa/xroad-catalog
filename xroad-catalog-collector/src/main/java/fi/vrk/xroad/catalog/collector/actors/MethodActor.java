package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import eu.x_road.metadata.ListMethods;
import eu.x_road.metadata.ListMethodsResponse;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * List methods for current client
 * Created by sjk on 19.2.2016.
 */
@Component
@Scope("prototype")
public class MethodActor extends UntypedActor {
    private static AtomicInteger COUNTER = new AtomicInteger(0);
    // to test fault handling
    private static boolean FORCE_FAILURES = false;

    private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

    @Value("${local.server.port}")
    private int port = 0;


    private final LoggingAdapter log = Logging
            .getLogger(getContext().system(), "MethodActor");

    @Override
    public void preStart() throws Exception {
        log.info("preStart {}", this.hashCode());
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

        log.info("onReceive {} {} {}", COUNTER.addAndGet(1), message.toString(), this.hashCode());




        ListMethods request = new ListMethods();

        ListMethodsResponse result = (ListMethodsResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive("http://gdev-ss1.i.palveluvayla.com"
                + "/listMethods", request);
        maybeFail();


        log.info("ListMethodsResponse {} ", result.toString());
        log.info("Servicecodes {} ", result.getServiceCode().stream().map(s -> s.toString()));


    }
}
