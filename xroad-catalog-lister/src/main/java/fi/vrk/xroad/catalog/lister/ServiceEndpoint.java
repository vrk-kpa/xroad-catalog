/**
 * The MIT License
 * Copyright (c) 2022, Population Register Centre (VRK)
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
import fi.vrk.xroad.catalog.persistence.entity.OpenApi;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.xroad_catalog_lister.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import java.util.concurrent.atomic.AtomicReference;

@Endpoint
@Slf4j
public class ServiceEndpoint {
    private static final String NAMESPACE_URI = "http://xroad.vrk.fi/xroad-catalog-lister";

    private static final String NOT_FOUND = " not found";

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private JaxbCatalogService jaxbCatalogService;

    @Autowired
    private JaxbCompanyService jaxbCompanyService;

    @Autowired
    private JaxbOrganizationService jaxbOrganizationService;


    @Autowired
    private JaxbConverter jaxbConverter;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ListMembers")
    @ResponsePayload
    public ListMembersResponse listMembers(@RequestPayload ListMembers request) {
        ListMembersResponse response = new ListMembersResponse();
        response.setMemberList(new MemberList());

        Iterable<Member> members = jaxbCatalogService.getAllMembers(request.getStartDateTime(), request.getEndDateTime());
        response.getMemberList().getMember().addAll(Lists.newArrayList(members));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetServiceType")
    @ResponsePayload
    public GetServiceTypeResponse getServiceType(@RequestPayload GetServiceType request) {
        GetServiceTypeResponse response = new GetServiceTypeResponse();
        Service service = catalogService.getService(request.getXRoadInstance(),
                request.getMemberClass(),
                request.getMemberCode(),
                request.getSubsystemCode(),
                request.getServiceCode(),
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

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetOrganizations")
    @ResponsePayload
    public GetOrganizationsResponse getOrganizations(@RequestPayload GetOrganizations request) {
        GetOrganizationsResponse response = new GetOrganizationsResponse();
        response.setOrganizationList(new OrganizationList());
        Iterable<Organization> organizations = jaxbOrganizationService.getOrganizations(request.getBusinessCode());
        if (!organizations.iterator().hasNext()) {
            throw new CatalogListerRuntimeException("Organizations with businessCode " + request.getBusinessCode() + NOT_FOUND);
        }
        response.getOrganizationList().getOrganization().addAll(Lists.newArrayList(organizations));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "HasOrganizationChanged")
    @ResponsePayload
    public HasOrganizationChangedResponse hasOrganizationChanged(@RequestPayload HasOrganizationChanged request) {
        if ((request.getGuid() == null || request.getGuid().isEmpty())) {
            throw new CatalogListerRuntimeException("Guid is a required parameter");
        }
        HasOrganizationChangedResponse response = new HasOrganizationChangedResponse();
        response.setChangedValueList(new ChangedValueList());
        Iterable<ChangedValue> changedValues = jaxbOrganizationService.getChangedOrganizationValues(
                request.getGuid(),
                request.getStartDateTime(),
                request.getEndDateTime());
        response.getChangedValueList().getChangedValue().addAll(Lists.newArrayList(changedValues));
        response.setChanged(!response.getChangedValueList().getChangedValue().isEmpty());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetCompanies")
    @ResponsePayload
    public GetCompaniesResponse getCompanies(@RequestPayload GetCompanies request) {
        GetCompaniesResponse response = new GetCompaniesResponse();
        response.setCompanyList(new CompanyList());
        Iterable<Company> companies = jaxbCompanyService.getCompanies(request.getBusinessId());
        if (!companies.iterator().hasNext()) {
            throw new CatalogListerRuntimeException("Companies with businessId " + request.getBusinessId() + NOT_FOUND);
        }
        response.getCompanyList().getCompany().addAll(Lists.newArrayList(companies));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "HasCompanyChanged")
    @ResponsePayload
    public HasCompanyChangedResponse hasCompanyChanged(@RequestPayload HasCompanyChanged request) {
        if ((request.getBusinessId() == null || request.getBusinessId().isEmpty())) {
            throw new CatalogListerRuntimeException("BusinessId is a required parameter");
        }
        HasCompanyChangedResponse response = new HasCompanyChangedResponse();
        response.setChangedValueList(new ChangedValueList());
        Iterable<ChangedValue> changedValues = jaxbCompanyService.getChangedCompanyValues(
                request.getBusinessId(),
                request.getStartDateTime(),
                request.getEndDateTime());
        response.getChangedValueList().getChangedValue().addAll(Lists.newArrayList(changedValues));
        response.setChanged(!response.getChangedValueList().getChangedValue().isEmpty());
        return response;
    }

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
