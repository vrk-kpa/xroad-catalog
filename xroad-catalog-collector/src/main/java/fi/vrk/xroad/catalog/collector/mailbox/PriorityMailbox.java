package fi.vrk.xroad.catalog.collector.mailbox;

import akka.actor.ActorSystem;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedPriorityMailbox;
import com.typesafe.config.Config;
import fi.vrk.xroad.catalog.collector.wsimport.ClientType;

/**
 * Simple priority queue mapping the task priority to the mailbox priority.
 */
public class PriorityMailbox extends UnboundedPriorityMailbox {

    public PriorityMailbox(ActorSystem.Settings settings, Config config) {

        // Create a new PriorityGenerator, lower priority means more important
        super(new PriorityGenerator() {

            @Override
            public int gen(Object message) {
                if (message instanceof ClientType) {
                    return 1;
                } else {
                    // default
                    return 100;
                }
            }
        });

    }
}