package fi.vrk.xroad.catalog.collector.configuration;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import fi.vrk.xroad.catalog.collector.mock.MockMetaServicesImpl;
import fi.vrk.xroad.catalog.collector.mock.MockRestTemplate;
import fi.vrk.xroad.catalog.collector.wsimport.MetaServicesPort;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestOperations;

@Configuration
@Lazy
@ComponentScan(basePackages = {
        "fi.vrk.xroad.catalog.collector.services",
        "fi.vrk.xroad.catalog.collector.actors",
        "fi.vrk.xroad.catalog.collector.mock",
        "fi.vrk.xroad.catalog.collector.extension",
        "fi.vrk.xroad.catalog" +
        ".persistence" })
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
    public RestOperations getRestOperations() {
        return new MockRestTemplate();
    }

    // to start CXF (for mock web service)
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new CXFServlet(), "/*");
    }

    @Bean
    @Lazy(value = false)
    public EndpointImpl metaServicesService(Bus bus) {
        EndpointImpl endpoint = new EndpointImpl(bus, metaServices());
        log.info("publishing generator end point {}", endpoint);
        endpoint.publish("/metaservices");
        return endpoint;
    }

    @Bean
    public MetaServicesPort metaServices() {
        return new MockMetaServicesImpl();
    }
}
