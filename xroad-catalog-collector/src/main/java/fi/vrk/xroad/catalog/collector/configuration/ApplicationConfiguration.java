package fi.vrk.xroad.catalog.collector.configuration;

import akka.actor.ActorSystem;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import fi.vrk.xroad.catalog.collector.mock.MockRestTemplate;
import fi.vrk.xroad.catalog.collector.mock.MockWebServiceTemplate;
import fi.vrk.xroad.catalog.collector.util.XRoadClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestOperations;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.bind.Marshaller;
import java.util.Properties;

@Configuration
@Lazy
@ComponentScan(basePackages = { "fi.vrk.xroad.catalog.collector.services",
    "fi.vrk.xroad.catalog.collector.actors", "fi.vrk.xroad.catalog.collector.extension", "fi.vrk.xroad.catalog" +
        ".persistence" })
@Slf4j
public class ApplicationConfiguration {

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

    @Bean
    public WebServiceTemplate getWebServiceTemplate() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("list-methods.wsdl");
        return new MockWebServiceTemplate(marshaller);

    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("list-methods.wsdl");
        return marshaller;
    }


    @Bean
    public XRoadClient xRoadClient(Jaxb2Marshaller marshaller) {
        XRoadClient client = new XRoadClient();
        client.setWebServiceTemplate(getWebServiceTemplate());
        client.setDefaultUri("http://localhost/listMethods");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);

        return client;
    }
}
