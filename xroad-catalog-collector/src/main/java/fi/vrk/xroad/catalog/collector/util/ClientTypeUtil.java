package fi.vrk.xroad.catalog.collector.util;


import fi.vrk.xroad.catalog.collector.wsimport.ClientType;

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

    public static String toString(fi.vrk.xroad.catalog.collector.wsimport.XRoadIdentifierType c) {
        StringBuilder sb = new StringBuilder("");
        sb.append("ObjectType: ");
        sb.append(c.getObjectType());
        sb.append(" XRoadInstance: ");
        sb.append(c.getXRoadInstance());
        sb.append(" MemberClass: ");
        sb.append(c.getMemberClass());
        sb.append(" MemberCode: ");
        sb.append(c.getMemberCode());
        sb.append(" SubsystemCode: ");
        sb.append(c.getSubsystemCode());
        return sb.toString();
    }
}
