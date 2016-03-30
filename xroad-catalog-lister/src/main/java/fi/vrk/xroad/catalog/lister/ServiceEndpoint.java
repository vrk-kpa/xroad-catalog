package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import fi.vrk.xroad.xroad_catalog_lister.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.time.LocalDateTime;
import java.util.Collection;

@Endpoint
@Slf4j
public class ServiceEndpoint {
	private static final String NAMESPACE_URI = "http://xroad.vrk.fi/xroad-catalog-lister";

	@Autowired
	private CatalogService catalogService;

    @Autowired
    private JaxbConverter jaxbConverter;

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
        Collection<fi.vrk.xroad.xroad_catalog_lister.Member> jaxbMembers = jaxbConverter.convertMembers(members, false);
        response.getMemberList().getMembers().addAll(jaxbMembers);
		return response;
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
