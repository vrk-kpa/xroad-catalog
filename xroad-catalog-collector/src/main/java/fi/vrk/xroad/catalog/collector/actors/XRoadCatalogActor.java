package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.Terminated;
import akka.actor.UntypedActor;
import lombok.extern.slf4j.Slf4j;
import scala.Option;

/**
 * Created by sjk on 6.4.2016.
 */
@Slf4j
public abstract class XRoadCatalogActor extends UntypedActor {

    abstract protected boolean handleMessage(Object message) throws Exception;

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("{} handleXRoadCatalogMessage {}", this.hashCode());
        if (handleMessage(message)) {
            return;
        }else if (message instanceof Terminated) {
            throw new RuntimeException("Terminated: " + message);
        } else {
            log.error("Unable to handle message {}", message);
            throw new RuntimeException("Unable to handle message");
        }
    }

    @Override
    public void postStop() throws Exception {
        log.info("postStop");
        super.postStop();
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        log.info("preRestart {} {} {} ", this.hashCode(), reason, message);
        super.preRestart(reason, message);
    }
}