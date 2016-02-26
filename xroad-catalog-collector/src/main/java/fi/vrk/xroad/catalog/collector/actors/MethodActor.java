package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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



    }
}
