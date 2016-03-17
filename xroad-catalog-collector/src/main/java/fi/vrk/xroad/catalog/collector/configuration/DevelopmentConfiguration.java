package fi.vrk.xroad.catalog.collector.configuration;

import fi.vrk.xroad.catalog.collector.mock.MockMetaServicesImpl;
import fi.vrk.xroad.catalog.collector.mock.MockRestTemplate;
import fi.vrk.xroad.catalog.collector.wsimport.MetaServicesPort;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestOperations;

/**
 * Created by sjk on 17.3.2016.
 */
@Configuration
@Slf4j
public class DevelopmentConfiguration extends ApplicationConfiguration {
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
