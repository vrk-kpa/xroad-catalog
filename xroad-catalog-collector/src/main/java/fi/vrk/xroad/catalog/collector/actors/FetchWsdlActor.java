package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.UntypedActor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Actor which fetches one wsdl
 */
@Component
@Scope("prototype")
@Slf4j
public class FetchWsdlActor extends UntypedActor {

    private static AtomicInteger COUNTER = new AtomicInteger(0);

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("fetching wsdl [{}] {}", COUNTER.addAndGet(1), message);
    }
}
