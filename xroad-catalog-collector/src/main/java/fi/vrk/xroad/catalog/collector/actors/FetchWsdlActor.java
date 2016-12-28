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
package fi.vrk.xroad.catalog.collector.actors;

import com.google.common.base.Strings;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadServiceIdentifierType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.ServiceId;
import fi.vrk.xroad.catalog.persistence.entity.SubsystemId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Actor which fetches one wsdl
 */
@Component
@Scope("prototype")
@Slf4j
public class FetchWsdlActor extends XRoadCatalogActor {

    private static AtomicInteger COUNTER = new AtomicInteger(0);
    private static final String WSDL_CONTEXT_PATH = "/wsdl";

    @Value("${xroad-catalog.fetch-wsdl-host}")
    private String host;

    @Autowired
    @Qualifier("wsdlRestOperations")
    private RestOperations restOperations;

    @Autowired
    protected CatalogService catalogService;

    public String getHost() {
        return host;
    }


    @Override
    protected boolean handleMessage(Object message)  {
        if (message instanceof XRoadServiceIdentifierType) {
            log.info("fetching wsdl [{}] {}", COUNTER.addAndGet(1), message);
            XRoadServiceIdentifierType service = (XRoadServiceIdentifierType) message;
            // get wsdl
            String url = buildUri(service);
            String wsdl = restOperations.getForObject(url, String.class);
            log.debug("url: {} received wsdl: {} for ", url, wsdl);
            catalogService.saveWsdl(createSubsystemId(service),
                    createServiceId(service),
                    wsdl);
            log.info("saved wsdl successfully");
            return true;
        } else {
            return false;
        }
    }

    private ServiceId createServiceId(XRoadServiceIdentifierType service) {
        return new ServiceId(service.getServiceCode(),
                service.getServiceVersion());
    }

    private SubsystemId createSubsystemId(XRoadServiceIdentifierType service) {
        return new SubsystemId(service.getXRoadInstance(),
                service.getMemberClass(),
                service.getMemberCode(),
                service.getSubsystemCode());
    }

    private String buildUri(XRoadServiceIdentifierType service) {
        assert service.getXRoadInstance() != null;
        assert service.getMemberClass() != null;
        assert service.getMemberCode() != null;
        assert service.getServiceCode() != null;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getHost())
                .path(WSDL_CONTEXT_PATH)
                .queryParam("xRoadInstance", service.getXRoadInstance())
                .queryParam("memberClass", service.getMemberClass())
                .queryParam("memberCode", service.getMemberCode())
                .queryParam("serviceCode", service.getServiceCode());
        if (!Strings.isNullOrEmpty(service.getSubsystemCode())) {
            builder = builder.queryParam("subsystemCode", service.getSubsystemCode());
        }
        if (!Strings.isNullOrEmpty(service.getServiceVersion())) {
            builder = builder.queryParam("version", service.getServiceVersion());
        }
        return builder.toUriString();
    }
}
