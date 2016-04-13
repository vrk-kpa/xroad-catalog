package fi.vrk.xroad.catalog.collector;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fi.vrk.xroad.catalog.collector.actors.Supervisor;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Main collector application.
 * Initiates a supervisor
 */
@Slf4j
@Configuration
@EnableAutoConfiguration
@ComponentScan("fi.vrk.xroad.catalog.collector.configuration")
public class XRoadCatalogCollector  {


    static private long collectorInterval;

    @Value("${xroad-catalog.collector-interval-min}")
    public void setCollectorInterval(long collectorIntervalMin) {
        XRoadCatalogCollector.collectorInterval = collectorIntervalMin;
    }


    public static void main(String[] args) throws Exception {

        ApplicationContext context =
            SpringApplication.run(XRoadCatalogCollector.class, args);

        ActorSystem system = context.getBean(ActorSystem.class);

        final LoggingAdapter log = Logging.getLogger(system, "Application");

        log.info("Starting up");


        SpringExtension ext = context.getBean(SpringExtension.class);

        // Use the Spring Extension to create props for a named actor bean
        ActorRef supervisor = system.actorOf(
                ext.props("supervisor"));

        system.scheduler().schedule(Duration.Zero(), Duration.create(collectorInterval, TimeUnit.MINUTES), supervisor, Supervisor
                .START_COLLECTING,
                system.dispatcher(), null);

    }

}
