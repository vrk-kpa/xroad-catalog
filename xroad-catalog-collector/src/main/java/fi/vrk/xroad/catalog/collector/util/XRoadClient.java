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


        fi.vrk.xroad.catalog.collector.wsimport.XRoadClientIdentifierType clientIdentifierType =
                (fi.vrk.xroad.catalog.collector.wsimport.XRoadClientIdentifierType) convertIdentifierType(client.getId());

        XRoadServiceIdentifierType serviceIdentifierType = new XRoadServiceIdentifierType();
        setHeaders(serviceIdentifierType);
        serviceIdentifierType.setSubsystemCode("SS1");
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
    protected static XRoadIdentifierType convertIdentifierType
    (eu.x_road.xsd.identifiers.XRoadIdentifierType source) {
        XRoadIdentifierType result = null;
        if (source instanceof eu.x_road.xsd.identifiers.XRoadClientIdentifierType) {
            result = new XRoadClientIdentifierType();
        } else if (source instanceof eu.x_road.xsd.identifiers.XRoadServiceIdentifierType) {
            result = new XRoadServiceIdentifierType();
        } else {
            result = new XRoadIdentifierType();
        }
        result.setGroupCode(source.getGroupCode());
        result.setObjectType(XRoadObjectType.fromValue(source.getObjectType().value()));
        result.setMemberCode(source.getMemberCode());
        result.setServiceVersion(source.getServiceVersion());
        result.setMemberClass(source.getMemberClass());
        result.setServiceCode(source.getServiceCode());
        result.setSecurityCategoryCode(source.getSecurityCategoryCode());
        result.setServerCode(source.getServerCode());
        result.setXRoadInstance(source.getXRoadInstance());
        result.setSubsystemCode(source.getSubsystemCode());

        return result;
    }

    protected static void setHeaders(XRoadIdentifierType type) {
        type.setXRoadInstance("FI");
        type.setMemberClass("GOV");
        type.setMemberCode("1710128-9");

    }
}
