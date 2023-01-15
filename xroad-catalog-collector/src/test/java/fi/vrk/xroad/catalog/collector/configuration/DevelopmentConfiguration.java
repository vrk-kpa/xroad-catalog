/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS) Copyright (c) 2016-2022 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.collector.configuration;

import fi.vrk.xroad.catalog.collector.mock.MockMetaServicesImpl;
import fi.vrk.xroad.catalog.collector.mock.MockRestTemplate;
import fi.vrk.xroad.catalog.collector.wsimport.MetaServicesPort;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestOperations;


@Configuration
@Profile( {"development", "default", "fi"})
@Slf4j
public class DevelopmentConfiguration extends ApplicationConfiguration {

    @Bean
    @Qualifier("listClientsRestOperations")
    public RestOperations getRestOperations() {
        log.info("--------------DEVELOPMENT Configuration");
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
