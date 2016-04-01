package fi.vrk.xroad.catalog.collector.mock;

import fi.vrk.xroad.catalog.collector.util.ClientTypeUtil;
import fi.vrk.xroad.catalog.collector.wsimport.*;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadServiceIdentifierType;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceContext;
import java.util.List;

/**
 * Mock metaservices -service which answers something valid and semi-reasonable
 * dummy data when queried for listMethods
 */
@WebService
@Slf4j
public class MockMetaServicesImpl implements MetaServicesPort {

    @Resource
    private WebServiceContext ctx;

    @Override
    public List<XRoadServiceIdentifierType> allowedMethods() {
        return null;
    }

    @Override
    public ListMethodsResponse listMethods(ListMethods listMethods,
                                           Holder<XRoadClientIdentifierType> client,
                                           Holder<XRoadServiceIdentifierType> service,
                                           Holder<String> userId,
                                           Holder<String> id,
                                           Holder<String> protocolVersion) {
        log.info("mock listMethods");
        log.info("client= {}", ClientTypeUtil.toString(client.value));

        ListMethodsResponse response = new ListMethodsResponse();
        response.getService().add(generateService("testServiceFoo", "v1", service.value));
        response.getService().add(generateService("testServiceBar", "v1", service.value));
        response.getService().add(generateService("testServiceBaz", "v1", service.value));
        return response;
    }


    private XRoadServiceIdentifierType generateService(String serviceCode,
                                                       String serviceVersion,
                                                       XRoadServiceIdentifierType serviceHeader) {
        XRoadServiceIdentifierType service = new XRoadServiceIdentifierType();
        service.setXRoadInstance(serviceHeader.getXRoadInstance());
        service.setMemberClass(serviceHeader.getMemberClass());
        service.setMemberCode(serviceHeader.getMemberCode());
        service.setSubsystemCode(serviceHeader.getSubsystemCode());
        service.setServiceCode(serviceCode);
        service.setServiceVersion(serviceVersion);
        service.setObjectType(XRoadObjectType.SERVICE);
        return service;
    }
}
