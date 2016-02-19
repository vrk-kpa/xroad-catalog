package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import eu.x_road.xsd.xroad.ClientType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.Option;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Scope("prototype")
public class ClientActor extends UntypedActor {

    // remove these when ready for production
    private static AtomicInteger DEBUG_MESSAGE_PROCESSING_COUNTER = new AtomicInteger(0);
    private static boolean DEBUG_FORCE_FAILURES = true;
    private static int DEBUG_EVERY_NTH_FAILS = 5;
    private static AtomicInteger DEBUG_IDENTITY_SEQUENCE = new AtomicInteger(0);
    private int debugIdentity;

    private final LoggingAdapter log = Logging
        .getLogger(getContext().system(), "ClientActor");

    public ClientActor() {
        debugIdentity = DEBUG_IDENTITY_SEQUENCE.getAndAdd(1);
        log.debug("instantiated new actor {} ", getDebugIdentity());
    }

    public String getDebugIdentity() {
        return "actor-" + debugIdentity;
    }
    private void debugMaybeFail() {
        if (DEBUG_FORCE_FAILURES) {
            if (DEBUG_MESSAGE_PROCESSING_COUNTER.get() % DEBUG_EVERY_NTH_FAILS == 0) {
                log.info("AAA sending test failure {}", getDebugIdentity());
                throw new RuntimeException("test failure thrown from (" + getDebugIdentity() + ")");
            }
        }
    }

    @Autowired
    protected CatalogService catalogService;

    @Override
    public void postRestart(Throwable reason) throws Exception {
        log.debug("postRestart {} reason: {} ", getDebugIdentity(), reason);
        super.postRestart(reason);
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        log.debug("preRestart {} reason: {} message: {} ", getDebugIdentity(), reason, message);
        super.preRestart(reason, message);
    }

    @Override
    public void preStart() throws Exception {
        log.debug("preStart {}", getDebugIdentity());
        super.preStart();
    }

    @Override
    public void postStop() throws Exception {
        log.debug("postStop {}", getDebugIdentity());
        super.postStop();
    }

    @Override
    public void onReceive(Object message) throws Exception {

        log.debug("onReceive {} {} {}", DEBUG_MESSAGE_PROCESSING_COUNTER.addAndGet(1), message.toString(), getDebugIdentity());
        ClientType clientType = (ClientType)message;

        debugMaybeFail();

        Member member = new Member();
        member.setName(clientType.getName());
        member.setMemberCode(clientType.getId().getMemberCode());
        member.setMemberClass(clientType.getId().getMemberClass());

        member = catalogService.saveMember(member);

        log.info("Member {} saved", member);

    }
}
