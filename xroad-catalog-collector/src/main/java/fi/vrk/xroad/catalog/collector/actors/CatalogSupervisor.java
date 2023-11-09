/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2016-2023 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.routing.SmallestMailboxPool;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

import static akka.actor.SupervisorStrategy.restart;

/**
 * Supervisor to get list of all clients in system and initiate a ClientActor for each
 * <p/>
 * A router is configured at startup time, managing a pool of task actors.
 */
@Component("CatalogSupervisor")
@Scope("prototype")
@Slf4j
public class CatalogSupervisor extends XRoadCatalogActor {

    public static final String LIST_CLIENTS_ACTOR_ROUTER = "list-clients-actor-router";
    public static final String LIST_METHODS_ACTOR_ROUTER = "list-methods-actor-router";
    public static final String FETCH_WSDL_ACTOR_ROUTER = "fetch-wsdl-actor-router";
    public static final String FETCH_OPENAPI_ACTOR_ROUTER = "fetch-openapi-actor-router";
    public static final String FETCH_REST_ACTOR_ROUTER = "fetch-rest-actor-router";

    @Autowired
    private SpringExtension springExtension;

    private ActorRef listClientsPoolRouter;
    private ActorRef listMethodsPoolRouter;
    private ActorRef fetchWsdlPoolRouter;
    private ActorRef fetchOpenApiPoolRouter;
    private ActorRef fetchRestPoolRouter;

    @Value("${xroad-catalog.list-methods-pool-size}")
    private int listMethodsPoolSize;

    @Value("${xroad-catalog.fetch-wsdl-pool-size}")
    private int fetchWsdlPoolSize;

    @Value("${xroad-catalog.fetch-openapi-pool-size}")
    private int fetchOpenApiPoolSize;

    @Value("${xroad-catalog.fetch-rest-pool-size}")
    private int fetchRestPoolSize;

    @Override
    public void preStart() throws Exception {

        log.info("Starting up");

        // prepare each pool. Give ActorRefs to other pools, as needed.
        // To make this possible, create pools in correct order

        // for each pool, supervisor strategy restarts each individual actor if it fails.
        // default is to restart the whole pool, which is not what we want:
        // http://doc.akka.io/docs/akka/current/java/routing.html#Supervision

        fetchWsdlPoolRouter = getContext().actorOf(new SmallestMailboxPool(fetchWsdlPoolSize)
                        .withSupervisorStrategy(new OneForOneStrategy(-1,
                                Duration.Inf(),
                                (Throwable t) -> restart()))
                        .props(springExtension.props("fetchWsdlActor")),
                FETCH_WSDL_ACTOR_ROUTER);

        fetchOpenApiPoolRouter = getContext().actorOf(new SmallestMailboxPool(fetchOpenApiPoolSize)
                        .withSupervisorStrategy(new OneForOneStrategy(-1,
                                Duration.Inf(),
                                (Throwable t) -> restart()))
                        .props(springExtension.props("fetchOpenApiActor")),
                FETCH_OPENAPI_ACTOR_ROUTER);

        fetchRestPoolRouter = getContext().actorOf(new SmallestMailboxPool(fetchRestPoolSize)
                        .withSupervisorStrategy(new OneForOneStrategy(-1,
                                Duration.Inf(),
                                (Throwable t) -> restart()))
                        .props(springExtension.props("fetchRestActor")),
                FETCH_REST_ACTOR_ROUTER);

        listMethodsPoolRouter = getContext().actorOf(new SmallestMailboxPool(listMethodsPoolSize)
                        .withSupervisorStrategy(new OneForOneStrategy(-1,
                                Duration.Inf(),
                                (Throwable t) -> restart()))
                        .props(springExtension.props("listMethodsActor", fetchWsdlPoolRouter, fetchOpenApiPoolRouter, fetchRestPoolRouter)),
                LIST_METHODS_ACTOR_ROUTER);

        listClientsPoolRouter = getContext().actorOf(new SmallestMailboxPool(1)
                        .withSupervisorStrategy(new OneForOneStrategy(-1,
                                Duration.Inf(),
                                (Throwable t) -> restart()))
                .props(springExtension.props("listClientsActor", listMethodsPoolRouter)),
                LIST_CLIENTS_ACTOR_ROUTER);

        super.preStart();
    }

    @Override
    protected boolean handleMessage(Object message) {

        if (START_COLLECTING.equals(message)) {
            listClientsPoolRouter.tell(ListClientsActor.START_COLLECTING, getSelf());
            return true;
        } else {
            return false;
        }
    }
}
