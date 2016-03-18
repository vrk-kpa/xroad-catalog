package fi.vrk.xroad.catalog.collector.util;

import eu.x_road.xsd.identifiers.*;
import eu.x_road.xsd.xroad.ClientType;
import fi.vrk.xroad.catalog.collector.wsimport.*;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadClientIdentifierType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadIdentifierType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadObjectType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadServiceIdentifierType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.ws.Holder;
import java.util.List;
import java.util.UUID;

/**
 * Created by sjk on 25.2.2016.
 */
@Slf4j
public class XRoadClient {


    @Value("${xroad-catalog.xroad-instance}")
    static private String xroadInstance;

    @Value("${xroad-catalog.group-code}")
    static private String groupCode;

    @Value("${xroad-catalog.member-code}")
    static private String memberCode;

    @Value("${xroad-catalog.member-class}")
    static private String memberClass;


    /**
     * Calls the service using JAX-WS endpoints that have been generated from wsdl
     * @param client
     * @return
     */
    public static List<XRoadServiceIdentifierType> getMethods(ClientType client) {

        // use dummy values for now
        XRoadClientIdentifierType clientIdentifierType = getDummyClientId();

        XRoadServiceIdentifierType serviceIdentifierType = new XRoadServiceIdentifierType();
        copyIdentifierType(serviceIdentifierType, client.getId());

        serviceIdentifierType.setServiceCode("listMethods");
        serviceIdentifierType.setServiceVersion("v1");

        serviceIdentifierType.setObjectType(XRoadObjectType.SERVICE);

        ProducerPortService service = new ProducerPortService();

        ListMethodsResponse response = service.getMetaServicesPortSoap11().listMethods(
                new ListMethods(),
                new Holder<>(clientIdentifierType),
                new Holder<>(serviceIdentifierType),
                new Holder<>("xroad-catalog-collector-"+ UUID.randomUUID()),
                new Holder<>("xroad-catalog-collector"),
                new Holder<>("4.x"));

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

    /**
     * Static values for testing my local vagrant ss1
     * @return
     */
    protected static XRoadClientIdentifierType getDummyClientId() {
        XRoadClientIdentifierType target = new XRoadClientIdentifierType();
        target.setGroupCode(groupCode);
        target.setObjectType(XRoadObjectType.MEMBER);
        target.setMemberCode(memberCode);
        target.setMemberClass(memberClass);
        target.setXRoadInstance(xroadInstance);
        return target;
    }

}
