package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import fi.vrk.xroad.xroad_catalog_lister.Member;
import fi.vrk.xroad.xroad_catalog_lister.ServiceList;
import fi.vrk.xroad.xroad_catalog_lister.SubsystemList;
import fi.vrk.xroad.xroad_catalog_lister.WSDL;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Utility for converting JAXB classes & entities
 */
@Component
public class JaxbConverter {

    public Collection<Member> convertMembers(Iterable<fi.vrk.xroad.catalog.persistence.entity.Member> members,
                                             boolean onlyActiveChildren) throws DatatypeConfigurationException {
        List<Member> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Member member: members) {
            Member cm = new Member();
            cm.setChanged(toXmlGregorianCalendar(member.getStatusInfo().getChanged()));
            cm.setCreated(toXmlGregorianCalendar(member.getStatusInfo().getCreated()));
            cm.setFetched(toXmlGregorianCalendar(member.getStatusInfo().getFetched()));
            cm.setRemoved(toXmlGregorianCalendar(member.getStatusInfo().getRemoved()));
            cm.setMemberCode(member.getMemberCode());
            cm.setMemberClass(member.getMemberClass());
            cm.setName(member.getName());
            cm.setXRoadInstance(member.getXRoadInstance());
            cm.setSubsystems(new SubsystemList());
            Iterable<Subsystem> subsystems = null;
            if (onlyActiveChildren) {
                subsystems = member.getActiveSubsystems();
            } else {
                subsystems = member.getAllSubsystems();
            }
            cm.getSubsystems().getSubsystem().addAll(convertSubsystems(subsystems, onlyActiveChildren));
            converted.add(cm);
        }
        return converted;
    }

    public Collection<fi.vrk.xroad.xroad_catalog_lister.Subsystem> convertSubsystems(Iterable<Subsystem> subsystems,
                                                                                     boolean onlyActiveChildren) throws DatatypeConfigurationException {
        List<fi.vrk.xroad.xroad_catalog_lister.Subsystem> converted = new ArrayList<>();
        for (Subsystem subsystem: subsystems) {
            fi.vrk.xroad.xroad_catalog_lister.Subsystem cs = new fi.vrk.xroad.xroad_catalog_lister.Subsystem();
            cs.setChanged(toXmlGregorianCalendar(subsystem.getStatusInfo().getChanged()));
            cs.setCreated(toXmlGregorianCalendar(subsystem.getStatusInfo().getCreated()));
            cs.setFetched(toXmlGregorianCalendar(subsystem.getStatusInfo().getFetched()));
            cs.setRemoved(toXmlGregorianCalendar(subsystem.getStatusInfo().getRemoved()));
            cs.setSubsystemCode(subsystem.getSubsystemCode());
            cs.setServices(new ServiceList());
            Iterable<Service> services = null;
            if (onlyActiveChildren) {
                services = subsystem.getActiveServices();
            } else {
                services = subsystem.getAllServices();
            }
            cs.getServices().getService().addAll(convertServices(services, onlyActiveChildren));
            converted.add(cs);
        }
        return converted;
    }

    public Collection<fi.vrk.xroad.xroad_catalog_lister.Service> convertServices(Iterable<Service> services,
                                                                                 boolean onlyActiveChildren) throws DatatypeConfigurationException {
        List<fi.vrk.xroad.xroad_catalog_lister.Service> converted = new ArrayList<>();
        for (Service service: services) {
            fi.vrk.xroad.xroad_catalog_lister.Service cs = new fi.vrk.xroad.xroad_catalog_lister.Service();
            cs.setChanged(toXmlGregorianCalendar(service.getStatusInfo().getChanged()));
            cs.setCreated(toXmlGregorianCalendar(service.getStatusInfo().getCreated()));
            cs.setFetched(toXmlGregorianCalendar(service.getStatusInfo().getFetched()));
            cs.setRemoved(toXmlGregorianCalendar(service.getStatusInfo().getRemoved()));
            cs.setServiceCode(service.getServiceCode());
            cs.setServiceVersion(service.getServiceVersion());
            Wsdl wsdl = null;
            if (onlyActiveChildren) {
                if (service.getWsdl() != null && !service.getWsdl().getStatusInfo().isRemoved()) {
                    wsdl = service.getWsdl();
                }
            } else {
                wsdl = service.getWsdl();
            }
            if (wsdl != null) {
                cs.setWsdl(convertWsdl(service.getWsdl()));
            }
            converted.add(cs);
        }
        return converted;
    }

    private WSDL convertWsdl(Wsdl wsdl) throws DatatypeConfigurationException {
        WSDL cw = new WSDL();
        cw.setChanged(toXmlGregorianCalendar(wsdl.getStatusInfo().getChanged()));
        cw.setCreated(toXmlGregorianCalendar(wsdl.getStatusInfo().getCreated()));
        cw.setFetched(toXmlGregorianCalendar(wsdl.getStatusInfo().getFetched()));
        cw.setRemoved(toXmlGregorianCalendar(wsdl.getStatusInfo().getRemoved()));
        cw.setExternalId(wsdl.getExternalId());
        cw.setData(wsdl.getData());
        return cw;
    }

    public LocalDateTime toLocalDateTime(XMLGregorianCalendar calendar) {
        return calendar.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
    }

    public XMLGregorianCalendar toXmlGregorianCalendar(LocalDateTime localDateTime) throws DatatypeConfigurationException {
        if (localDateTime == null) {
            return null;
        } else {
            GregorianCalendar cal = GregorianCalendar.from(localDateTime.atZone(ZoneId.systemDefault()));
            XMLGregorianCalendar xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            return xc;
        }
    }


}
