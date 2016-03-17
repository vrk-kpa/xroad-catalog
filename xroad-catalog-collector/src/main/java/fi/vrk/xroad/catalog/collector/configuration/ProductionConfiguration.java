package fi.vrk.xroad.catalog.collector.configuration;

import fi.vrk.xroad.catalog.collector.mock.MockMetaServicesImpl;
import fi.vrk.xroad.catalog.collector.wsimport.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Holder;
import java.util.List;

/**
 * Created by sjk on 17.2.2016.
 */
@Configuration
@Profile("production")
@Slf4j
public class ProductionConfiguration extends ApplicationConfiguration {

    @Override
    @Bean
    @Qualifier("listClientsRestOperations")
    public RestOperations getRestOperations() {
        log.info("getrestop aproduction");
        return new RestTemplate();
    }

    @Override
    @Bean
    @Qualifier("wsdlRestOperations")
    public RestOperations getDynamicWsdlRestOperations() {
        log.info("getrestop aproduction");
        return new RestTemplate();
    }
}
