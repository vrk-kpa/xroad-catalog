package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.xroad_catalog_lister.ListMembers;
import fi.vrk.xroad.xroad_catalog_lister.ListMembersResponse;
import fi.vrk.xroad.xroad_catalog_lister.MemberList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.time.LocalTime;

@Endpoint
public class ServiceEndpoint {
	private static final String NAMESPACE_URI = "http://xroad.vrk.fi/xroad-catalog-lister";

	@Autowired
	private ServiceRepository serviceRepository;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "ListMembers")
	@ResponsePayload
	public ListMembersResponse listMembers(@RequestPayload ListMembers request) throws
			Exception {
		ListMembersResponse response = new ListMembersResponse();
		response.setMemberList(new MemberList());
		response.getMemberList().getMembers().addAll(serviceRepository.listServices(LocalTime.now
				()));


		return response;
	}

}
