package fi.vrk.xroad.catalog.collector.util;

import eu.x_road.xsd.xroad.ClientType;

/**
 * Created by sjk on 26.2.2016.
 */
public class ClientTypeUtil {

    public static String toString(ClientType c) {
        StringBuilder sb = new StringBuilder("");
        sb.append("ObjectType: ");
        sb.append(c.getId().getObjectType());
        sb.append(" Name: ");
        sb.append(c.getName());
        sb.append(" XRoadInstance: ");
        sb.append(c.getId().getXRoadInstance());
        sb.append(" MemberClass: ");
        sb.append(c.getId().getMemberClass());
        sb.append(" MemberCode: ");
        sb.append(c.getId().getMemberCode());
        sb.append(" SubsystemCode: ");
        sb.append(c.getId().getSubsystemCode());
        return sb.toString();
    }
}
