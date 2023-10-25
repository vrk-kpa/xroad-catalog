/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2016-2023 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.lister;

import com.google.common.collect.Lists;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.OpenApi;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.xroad_catalog_lister.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import java.util.concurrent.atomic.AtomicReference;

@Endpoint
@Slf4j
@Profile({"default", "fi"})
public class ServiceEndpointImpl implements ServiceEndpoint {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private JaxbCatalogService jaxbCatalogService;

    @Override
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ListMembers")
    @ResponsePayload
    public ListMembersResponse listMembers(@RequestPayload ListMembers request) {
        if (request.getStartDateTime() == null || request.getEndDateTime() == null) {
            throw new CatalogListerRuntimeException("startDateTime and endDateTIme parameters are missing");
        }
        ListMembersResponse response = new ListMembersResponse();
        response.setMemberList(new MemberList());
        Iterable<Member> members = jaxbCatalogService.getAllMembers(request.getStartDateTime(), request.getEndDateTime());
        response.getMemberList().getMember().addAll(Lists.newArrayList(members));
        return response;
    }

    @Override
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetServiceType")
    @ResponsePayload
    public GetServiceTypeResponse getServiceType(@RequestPayload GetServiceType request) {
        GetServiceTypeResponse response = new GetServiceTypeResponse();
        Service service = catalogService.getService(request.getXRoadInstance(),
                request.getMemberClass(),
                request.getMemberCode(),
                request.getServiceCode(),
                request.getSubsystemCode(),
                request.getServiceVersion());
        if (service == null) {
            throw new CatalogListerRuntimeException("Service with xRoadInstance \"" + request.getXRoadInstance()
                    + "\", memberClass \"" + request.getMemberClass()
                    + "\", memberCode \"" + request.getMemberCode()
                    + "\", subsystemCode \"" + request.getSubsystemCode()
                    + "\", serviceCode \"" + request.getServiceCode()
                    + "\" and serviceVersion \"" + request.getServiceVersion() + "\" not found");
        }
        if (service.hasWsdl()) {
            response.setType("SOAP");
        } else if (service.hasOpenApi()) {
            response.setType("OPENAPI");
        } else {
            response.setType("REST");
        }
        return response;
    }

    @Override
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "IsProvider")
    @ResponsePayload
    public IsProviderResponse isProvider(@RequestPayload IsProvider request) {
        AtomicReference<Boolean> isProvider = new AtomicReference<>();
        isProvider.set(Boolean.FALSE);
        fi.vrk.xroad.catalog.persistence.entity.Member member = catalogService.getMember(request.getXRoadInstance(),
                request.getMemberClass(), request.getMemberCode());

        if (member == null) {
            throw new CatalogListerRuntimeException("Member with xRoadInstance \"" + request.getXRoadInstance()
                    + "\", memberClass \"" + request.getMemberClass()
                    + "\" and memberCode \"" + request.getMemberCode() + "\" not found");
        }

        member.getAllSubsystems().forEach(subsystem -> {
            subsystem.getAllServices().forEach(service ->  {
                if (service.hasWsdl() || service.hasOpenApi()) {
                    isProvider.set(Boolean.TRUE);
                }
            });
        });

        IsProviderResponse response = new IsProviderResponse();
        response.setProvider(isProvider.get());
        return response;
    }

    @Override
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetWsdl")
    @ResponsePayload
    public GetWsdlResponse getWsdl(@RequestPayload GetWsdl request) {
        GetWsdlResponse response = new GetWsdlResponse();
        Wsdl wsdl = catalogService.getWsdl(request.getExternalId());
        if (wsdl == null) {
            throw new CatalogListerRuntimeException("wsdl with external id " + request.getExternalId() + NOT_FOUND);
        }
        response.setWsdl(wsdl.getData());
        return response;
    }

    @Override
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetOpenAPI")
    @ResponsePayload
    public GetOpenAPIResponse getOpenApi(@RequestPayload GetOpenAPI request) {
        GetOpenAPIResponse response = new GetOpenAPIResponse();
        OpenApi openApi = catalogService.getOpenApi(request.getExternalId());
        if (openApi == null) {
            throw new CatalogListerRuntimeException("OpenApi with external id " + request.getExternalId() + NOT_FOUND);
        }
        response.setOpenapi(openApi.getData());
        return response;
    }

    @Override
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetErrors")
    @ResponsePayload
    public GetErrorsResponse getErrors(@RequestPayload GetErrors request) {
        GetErrorsResponse response = new GetErrorsResponse();
        response.setErrorLogList(new ErrorLogList());
        Iterable<ErrorLog> errorLogEntries = jaxbCatalogService.getErrorLog(request.getStartDateTime(), request.getEndDateTime());
        if (errorLogEntries != null && !errorLogEntries.iterator().hasNext()) {
            throw new CatalogListerRuntimeException("ErrorLog entries since "
                    + request.getStartDateTime().toString() + " until " + request.getEndDateTime().toString() + NOT_FOUND);
        }
        response.getErrorLogList().getErrorLog().addAll(errorLogEntries != null
                ? Lists.newArrayList(errorLogEntries)
                : Lists.newArrayList());
        return response;
    }
}
