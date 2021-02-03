/**
 * The MIT License
 * Copyright (c) 2021, Population Register Centre (VRK)
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
package fi.vrk.xroad.catalog.collector;

import fi.vrk.xroad.catalog.collector.actors.Supervisor;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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
public class XRoadCatalogCollector {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(XRoadCatalogCollector.class, args);

        final Environment env = context.getEnvironment();

        final String keystore = env.getProperty("xroad-catalog.ssl-keystore");
        final String keystorePw = env.getProperty("xroad-catalog.ssl-keystore-password");
        if (keystore != null && keystorePw != null) {
            System.setProperty("javax.net.ssl.keyStore", keystore);
            System.setProperty("javax.net.ssl.keyStorePassword", keystorePw);
        }

        ActorSystem system = context.getBean(ActorSystem.class);

        final LoggingAdapter log = Logging.getLogger(system, "Application");
        Long collectorInterval = (Long) context.getBean("getCollectorInterval");

        log.info("Starting up catalog collector with collector interval of {}", collectorInterval);

        SpringExtension ext = context.getBean(SpringExtension.class);

        // Use the Spring Extension to create props for a named actor bean
        ActorRef supervisor = system.actorOf(ext.props("supervisor"));

        system.scheduler().schedule(Duration.Zero(),
                Duration.create(collectorInterval, TimeUnit.MINUTES),
                supervisor,
                Supervisor.START_COLLECTING,
                system.dispatcher(),
                ActorRef.noSender());
    }

}
