package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActorContext;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * For getting ActorRefs to other actors in supervisor-created pools
 */
public class RelativeActorRefUtil {
    private UntypedActorContext context;
    public RelativeActorRefUtil(UntypedActorContext context) {
        this.context = context;
    }

    /**
     * Resolve ActorRef to a pool with given name from inside a pool
     * in same level
     */
    public ActorRef resolvePoolRef(String poolname) throws Exception {
        // path to supervisor is two layers up
        // 1 = pool
        // 2 = supervisor which created the pool
        ActorPath parentRelativePath = context.self().path().parent().parent().child(poolname);
        ActorSelection selection = context.actorSelection(parentRelativePath);
        Timeout resolveTimeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));
        ActorRef ref = Await.result(selection.resolveOne(resolveTimeout), Duration.create(5, TimeUnit.SECONDS));
        return ref;
    }
}
