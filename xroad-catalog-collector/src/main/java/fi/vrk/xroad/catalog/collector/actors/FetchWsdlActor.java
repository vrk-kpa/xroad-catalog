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

    public String getHost() {
        return host;
    }

    @Autowired
    @Qualifier("wsdlRestOperations")
    private RestOperations restOperations;

    @Autowired
    protected CatalogService catalogService;


    @Override
    protected boolean handleMessage(Object message) throws Exception {
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
        ServiceId serviceId = new ServiceId(service.getServiceCode(),
                service.getServiceVersion());
        return serviceId;
    }

    private SubsystemId createSubsystemId(XRoadServiceIdentifierType service) {
        SubsystemId subsystemId = new SubsystemId(service.getXRoadInstance(),
                service.getMemberClass(),
                service.getMemberCode(),
                service.getSubsystemCode());
        return subsystemId;
    }

    private String buildUri(XRoadServiceIdentifierType service) {
        assert service.getXRoadInstance() != null;
        assert service.getMemberClass() != null;
        assert service.getMemberCode() != null;
        assert service.getServiceCode() != null;
        assert service.getServiceVersion() != null;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getHost())
                .path(WSDL_CONTEXT_PATH)
                .queryParam("xRoadInstance", service.getXRoadInstance())
                .queryParam("memberClass", service.getMemberClass())
                .queryParam("memberCode", service.getMemberCode())
                .queryParam("serviceCode", service.getServiceCode())
                .queryParam("version", service.getServiceVersion());
        if (!Strings.isNullOrEmpty(service.getSubsystemCode())) {
            builder = builder.queryParam("subsystemCode", service.getSubsystemCode());
        }
        return builder.toUriString();
    }
}
