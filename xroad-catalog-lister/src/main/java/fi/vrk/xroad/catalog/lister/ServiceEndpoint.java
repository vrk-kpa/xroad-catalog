package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.*;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.xroad_catalog_lister.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

@Endpoint
@Slf4j
public class ServiceEndpoint {
	private static final String NAMESPACE_URI = "http://xroad.vrk.fi/xroad-catalog-lister";

	@Autowired
	private CatalogService catalogService;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "ListMembers")
	@ResponsePayload
	public ListMembersResponse listMembers(@RequestPayload ListMembers request) throws
			Exception {
		ListMembersResponse response = new ListMembersResponse();
		response.setMemberList(new MemberList());

        Iterable<Member> members;
        if (request.getChangedAfter() == null) {
            log.info("fetching all active members");
            members = catalogService.getActiveMembers();
        } else {
            log.info("fetching active members since xmlGregorianDate: " + request.getChangedAfter());
            LocalDateTime changedAfter = request.getChangedAfter().toGregorianCalendar()
                    .toZonedDateTime().toLocalDateTime();
            members = catalogService.getActiveMembers(changedAfter);
        }
        Collection<fi.vrk.xroad.xroad_catalog_lister.Member> jaxbMembers = convertMembers(members);
        response.getMemberList().getMembers().addAll(jaxbMembers);
		return response;
	}

    // TODO: move to utility class
    private Collection<fi.vrk.xroad.xroad_catalog_lister.Member> convertMembers(Iterable<Member> members) throws DatatypeConfigurationException {
        List<fi.vrk.xroad.xroad_catalog_lister.Member> converted = new ArrayList<>();
        for (Member member: members) {
            fi.vrk.xroad.xroad_catalog_lister.Member cm = new fi.vrk.xroad.xroad_catalog_lister.Member();
            cm.setChanged(toXmlGregorianCalendar(member.getStatusInfo().getChanged()));
            cm.setCreated(toXmlGregorianCalendar(member.getStatusInfo().getCreated()));
            cm.setFetched(toXmlGregorianCalendar(member.getStatusInfo().getFetched()));
            cm.setRemoved(toXmlGregorianCalendar(member.getStatusInfo().getRemoved()));
            cm.setMemberCode(member.getMemberCode());
            cm.setMemberClass(member.getMemberClass());
            cm.setName(member.getName());
            cm.setXRoadInstance(member.getXRoadInstance());
            cm.setSubsystems(new SubsystemList());
            cm.getSubsystems().getSubsystem().addAll(convertSubsystems(member.getActiveSubsystems()));
            converted.add(cm);
        }
        return converted;
    }

    private Collection<fi.vrk.xroad.xroad_catalog_lister.Subsystem> convertSubsystems(Iterable<Subsystem> subsystems) throws DatatypeConfigurationException {
        List<fi.vrk.xroad.xroad_catalog_lister.Subsystem> converted = new ArrayList<>();
        for (Subsystem subsystem: subsystems) {
            fi.vrk.xroad.xroad_catalog_lister.Subsystem cs = new fi.vrk.xroad.xroad_catalog_lister.Subsystem();
            cs.setChanged(toXmlGregorianCalendar(subsystem.getStatusInfo().getChanged()));
            cs.setCreated(toXmlGregorianCalendar(subsystem.getStatusInfo().getCreated()));
            cs.setFetched(toXmlGregorianCalendar(subsystem.getStatusInfo().getFetched()));
            cs.setRemoved(toXmlGregorianCalendar(subsystem.getStatusInfo().getRemoved()));
            cs.setSubsystemCode(subsystem.getSubsystemCode());
            cs.setServices(new ServiceList());
            cs.getServices().getService().addAll(convertServices(subsystem.getActiveServices()));
            converted.add(cs);
        }
        return converted;
    }

    private Collection<fi.vrk.xroad.xroad_catalog_lister.Service> convertServices(Iterable<Service> services) throws DatatypeConfigurationException {
        List<fi.vrk.xroad.xroad_catalog_lister.Service> converted = new ArrayList<>();
        for (Service service: services) {
            fi.vrk.xroad.xroad_catalog_lister.Service cs = new fi.vrk.xroad.xroad_catalog_lister.Service();
            cs.setChanged(toXmlGregorianCalendar(service.getStatusInfo().getChanged()));
            cs.setCreated(toXmlGregorianCalendar(service.getStatusInfo().getCreated()));
            cs.setFetched(toXmlGregorianCalendar(service.getStatusInfo().getFetched()));
            cs.setRemoved(toXmlGregorianCalendar(service.getStatusInfo().getRemoved()));
            cs.setServiceCode(service.getServiceCode());
            cs.setServiceVersion(service.getServiceVersion());
            converted.add(cs);
        }
        return converted;
    }

    private XMLGregorianCalendar toXmlGregorianCalendar(LocalDateTime localDateTime) throws DatatypeConfigurationException {
        if (localDateTime == null) {
            return null;
        } else {
            GregorianCalendar cal = GregorianCalendar.from(localDateTime.atZone(ZoneId.systemDefault()));
            XMLGregorianCalendar xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            return xc;
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetWsdl")
	@ResponsePayload
	public GetWsdlResponse getWsdl(@RequestPayload GetWsdl request) throws
			Exception {
        GetWsdlResponse response = new GetWsdlResponse();
        Wsdl wsdl = catalogService.getWsdl(request.getExternalId());
        if (wsdl == null) {
            throw new WsdlNotFoundException("wsdl with external id " + request.getExternalId()
            + " not found");
        }
        response.setWsdl(wsdl.getData());
        return response;
	}

}
