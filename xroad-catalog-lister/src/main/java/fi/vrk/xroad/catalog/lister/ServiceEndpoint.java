/**
 * The MIT License
 * Copyright (c) 2016, Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
	public ListMembersResponse listMembers(@RequestPayload ListMembers request) {
		ListMembersResponse response = new ListMembersResponse();
		response.setMemberList(new MemberList());

        Iterable<Member> members = jaxbCatalogService.getAllMembers(request.getChangedAfter());
        response.getMemberList().getMembers().addAll(Lists.newArrayList(members));
		return response;
	}

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetWsdl")
	@ResponsePayload
	public GetWsdlResponse getWsdl(@RequestPayload GetWsdl request) {
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
