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
                request.getServiceCode(),
                request.getSubsystemCode(),
                request.getServiceVersion());
        if (service == null) {
            throw new ServiceNotFoundException("Service with xRoadInstance \"" + request.getXRoadInstance()
                    + "\", memberClass \"" + request.getMemberClass()
                    + "\", memberCode \"" + request.getMemberCode()
                    + "\", serviceCode \"" + request.getServiceCode()
                    + "\", subsystemCode \"" + request.getSubsystemCode()
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
            throw new MemberNotFoundException("Member with xRoadInstance \"" + request.getXRoadInstance()
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
            throw new WsdlNotFoundException("wsdl with external id " + request.getExternalId()
                    + " not found");
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
            throw new OpenApiNotFoundException("OpenApi with external id " + request.getExternalId()
                    + " not found");
        }
        response.setOpenapi(openApi.getData());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetOrganizations")
    @ResponsePayload
    public GetOrganizationsResponse getOrganizations(@RequestPayload GetOrganizations request) {
        GetOrganizationsResponse response = new GetOrganizationsResponse();
        response.setOrganizationList(new OrganizationList());
        Iterable<Organization> organizations = jaxbCatalogService.getOrganizations(request.getBusinessCode());
        if (!organizations.iterator().hasNext()) {
            throw new OrganizationsNotFoundException("Organizations with businessCode " + request.getBusinessCode()
                    + " not found");
        }
        response.getOrganizationList().getOrganization().addAll(Lists.newArrayList(organizations));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "HasOrganizationChanged")
    @ResponsePayload
    public HasOrganizationChangedResponse hasOrganizationChanged(@RequestPayload HasOrganizationChanged request) {
        if ((request.getGuid() == null || request.getGuid().isEmpty()) || request.getChangedAfter() == null) {
            throw new ChangedValuesException("Guid and ChangedAfter are both required parameters");
        }
        HasOrganizationChangedResponse response = new HasOrganizationChangedResponse();
        response.setChangedValueList(new ChangedValueList());
        Iterable<ChangedValue> changedValues = jaxbCatalogService.getChangedOrganizationValues(request.getGuid(), request.getChangedAfter());
        response.getChangedValueList().getChangedValue().addAll(Lists.newArrayList(changedValues));
        response.setChanged(!response.getChangedValueList().getChangedValue().isEmpty());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetCompanies")
    @ResponsePayload
    public GetCompaniesResponse getCompanies(@RequestPayload GetCompanies request) {
        GetCompaniesResponse response = new GetCompaniesResponse();
        response.setCompanyList(new CompanyList());
        Iterable<Company> companies = jaxbCatalogService.getCompanies(request.getBusinessId());
        if (!companies.iterator().hasNext()) {
            throw new CompaniesNotFoundException("Companies with businessId " + request.getBusinessId()
                    + " not found");
        }
        response.getCompanyList().getCompany().addAll(Lists.newArrayList(companies));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "HasCompanyChanged")
    @ResponsePayload
    public HasCompanyChangedResponse hasCompanyChanged(@RequestPayload HasCompanyChanged request) {
        if ((request.getBusinessId() == null || request.getBusinessId().isEmpty()) || request.getChangedAfter() == null) {
            throw new ChangedValuesException("BusinessId and ChangedAfter are both required parameters");
        }
        HasCompanyChangedResponse response = new HasCompanyChangedResponse();
        response.setChangedValueList(new ChangedValueList());
        Iterable<ChangedValue> changedValues = jaxbCatalogService.getChangedCompanyValues(request.getBusinessId(), request.getChangedAfter());
        response.getChangedValueList().getChangedValue().addAll(Lists.newArrayList(changedValues));
        response.setChanged(!response.getChangedValueList().getChangedValue().isEmpty());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetErrors")
    @ResponsePayload
    public GetErrorsResponse getErrors(@RequestPayload GetErrors request) {
        if ((request.getSince() == null || request.getSince().toString().isEmpty())) {
            throw new ErrorLogNotFoundException("Since is a required parameter");
        }
        GetErrorsResponse response = new GetErrorsResponse();
        response.setErrorLogList(new ErrorLogList());
        Iterable<ErrorLog> errorLogEntries = jaxbCatalogService.getErrorLog(request.getSince());
        if (errorLogEntries != null && !errorLogEntries.iterator().hasNext()) {
            throw new ErrorLogNotFoundException("ErrorLog entries since " + request.getSince().toString() + " not found");
        }
        response.getErrorLogList().getErrorLog().addAll(Lists.newArrayList(errorLogEntries));
        return response;
    }
}
