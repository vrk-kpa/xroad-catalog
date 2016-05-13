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
package fi.vrk.xroad.catalog.collector.configuration;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * Common conf for development and production
 */
@Configuration
@Lazy
@ComponentScan(basePackages = {
        "fi.vrk.xroad.catalog.collector.services",
        "fi.vrk.xroad.catalog.collector.actors",
        "fi.vrk.xroad.catalog.collector.mock",
        "fi.vrk.xroad.catalog.collector.extension",
        "fi.vrk.xroad.catalog"
        + ".persistence" })
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
@Slf4j
public class ApplicationConfiguration extends SpringBootServletInitializer {

    // The application context is needed to initialize the Akka Spring
    // Extension
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SpringExtension springExtension;

    /**
     * Actor system singleton for this application.
     */
    @Bean
    public ActorSystem actorSystem() {

        ActorSystem system = ActorSystem
                .create("AkkaTaskProcessing", akkaConfiguration());

        // Initialize the application context in the Akka Spring Extension
        springExtension.initialize(applicationContext);
        return system;
    }

    /**
     * Read configuration from application.conf file
     */
    @Bean
    public Config akkaConfiguration() {
        return ConfigFactory.load();
    }

    @Bean
    @Qualifier("listClientsRestOperations")
    public RestOperations getRestOperations() {
        log.info("-------------- Configuration");
        return new RestTemplate();
    }

    @Bean
    @Qualifier("wsdlRestOperations")
    public RestOperations getDynamicWsdlRestOperations() {
        log.info("-------------- Configuration");
        return new RestTemplate();
    }

    @Value("${xroad-catalog.collector-interval-min}")
    private Long collectorInterval;

    @Bean
    public Long getCollectorInterval() {
        return collectorInterval;
    };

}
