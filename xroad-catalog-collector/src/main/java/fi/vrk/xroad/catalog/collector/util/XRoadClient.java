package fi.vrk.xroad.catalog.collector.util;

import fi.vrk.xroad.catalog.collector.wsimport.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by sjk on 25.2.2016.
 */
@Slf4j
public class XRoadClient {

    public static List<XRoadServiceIdentifierType> getMethods() {

        XRoadClientIdentifierType clientIdentifierType = new XRoadClientIdentifierType();
        setHeaders(clientIdentifierType);
        clientIdentifierType.setObjectType(XRoadObjectType.MEMBER);

        XRoadServiceIdentifierType serviceIdentifierType = new XRoadServiceIdentifierType();
        setHeaders(serviceIdentifierType);
        serviceIdentifierType.setSubsystemCode("SS1");
        serviceIdentifierType.setServiceCode("listMethods");
        serviceIdentifierType.setServiceVersion("v1");

        serviceIdentifierType.setObjectType(XRoadObjectType.SERVICE);

        ProducerPortService service = new ProducerPortService();


        ListMethodsResponse response = service.getMetaServicesPortSoap11().listMethods(new ListMethods(),
                clientIdentifierType, serviceIdentifierType, "EE1234567890", "ID11234", "4.x");

        List<XRoadServiceIdentifierType> methods = response.getServiceCode();
        log.info("Methods {} ", methods.toString());


        return methods;
    }

    protected static void setHeaders(XRoadIdentifierType type) {
        type.setXRoadInstance("FI");
        type.setMemberClass("GOV");
        type.setMemberCode("1710128-9");

    }
}
