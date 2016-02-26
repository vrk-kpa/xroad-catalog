package fi.vrk.xroad.catalog.collector.util;

import eu.x_road.xsd.identifiers.*;
import eu.x_road.xsd.xroad.ClientType;
import fi.vrk.xroad.catalog.collector.wsimport.*;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadClientIdentifierType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadIdentifierType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadObjectType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadServiceIdentifierType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by sjk on 25.2.2016.
 */
@Slf4j
public class XRoadClient {

    public static List<XRoadServiceIdentifierType> getMethods(ClientType client) {


        XRoadClientIdentifierType clientIdentifierType = new XRoadClientIdentifierType();
        copyIdentifierType(clientIdentifierType, client.getId());

        XRoadServiceIdentifierType serviceIdentifierType = new XRoadServiceIdentifierType();
        copyIdentifierType(serviceIdentifierType, client.getId());


        //setHeaders(serviceIdentifierType);
        //serviceIdentifierType.setSubsystemCode("SS1");
        serviceIdentifierType.setServiceCode("listMethods");
        serviceIdentifierType.setServiceVersion("v1");


        serviceIdentifierType.setObjectType(XRoadObjectType.SERVICE);

        ProducerPortService service = new ProducerPortService();


        ListMethodsResponse response = service.getMetaServicesPortSoap11().listMethods(new ListMethods(),
                clientIdentifierType, serviceIdentifierType, "EE1234567890", "ID11234", "4.x");

        List<XRoadServiceIdentifierType> methods = response.getService();

        return methods;
    }

    //TODO two different wsdl generations and two different types. This should be fixed
    protected static void copyIdentifierType
    (XRoadIdentifierType target, eu.x_road.xsd.identifiers.XRoadIdentifierType source) {

        target.setGroupCode(source.getGroupCode());
        target.setObjectType(XRoadObjectType.fromValue(source.getObjectType().value()));
        target.setMemberCode(source.getMemberCode());
        target.setServiceVersion(source.getServiceVersion());
        target.setMemberClass(source.getMemberClass());
        target.setServiceCode(source.getServiceCode());
        target.setSecurityCategoryCode(source.getSecurityCategoryCode());
        target.setServerCode(source.getServerCode());
        target.setXRoadInstance(source.getXRoadInstance());
        target.setSubsystemCode(source.getSubsystemCode());
    }

    protected static void setHeaders(XRoadIdentifierType type) {
        type.setXRoadInstance("FI");
        type.setMemberClass("GOV");
        type.setMemberCode("1710128-9");

    }
}
