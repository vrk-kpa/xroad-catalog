package fi.vrk.xroad.catalog.lister;

import com.google.common.collect.Lists;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import fi.vrk.xroad.xroad_catalog_lister.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@Slf4j
public class ServiceEndpoint {
	private static final String NAMESPACE_URI = "http://xroad.vrk.fi/xroad-catalog-lister";

	@Autowired
	private CatalogService catalogService;

    @Autowired
    private JaxbCatalogService jaxbCatalogService;

    @Autowired
    private JaxbConverter jaxbConverter;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "ListMembers")
	@ResponsePayload
	public ListMembersResponse listMembers(@RequestPayload ListMembers request) throws
			Exception {
		ListMembersResponse response = new ListMembersResponse();
		response.setMemberList(new MemberList());

        Iterable<Member> members = jaxbCatalogService.getAllMembers(request.getChangedAfter());
        response.getMemberList().getMembers().addAll(Lists.newArrayList(members));
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
