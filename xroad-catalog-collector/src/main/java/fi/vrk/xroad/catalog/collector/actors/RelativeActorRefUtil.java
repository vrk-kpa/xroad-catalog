/**
 * The MIT License
 * Copyright (c) 2016, Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
        return Await.result(selection.resolveOne(resolveTimeout), Duration.create(5, TimeUnit.SECONDS));
    }
}
