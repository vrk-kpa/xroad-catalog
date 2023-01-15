/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS) Copyright (c) 2016-2022 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.lister;

import com.google.common.collect.Lists;
import fi.vrk.xroad.xroad_catalog_lister.ChangedValue;
import fi.vrk.xroad.xroad_catalog_lister.ChangedValueList;
import fi.vrk.xroad.xroad_catalog_lister.Company;
import fi.vrk.xroad.xroad_catalog_lister.CompanyList;
import fi.vrk.xroad.xroad_catalog_lister.GetCompanies;
import fi.vrk.xroad.xroad_catalog_lister.GetCompaniesResponse;
import fi.vrk.xroad.xroad_catalog_lister.GetOrganizations;
import fi.vrk.xroad.xroad_catalog_lister.GetOrganizationsResponse;
import fi.vrk.xroad.xroad_catalog_lister.HasCompanyChanged;
import fi.vrk.xroad.xroad_catalog_lister.HasCompanyChangedResponse;
import fi.vrk.xroad.xroad_catalog_lister.HasOrganizationChanged;
import fi.vrk.xroad.xroad_catalog_lister.HasOrganizationChangedResponse;
import fi.vrk.xroad.xroad_catalog_lister.Organization;
import fi.vrk.xroad.xroad_catalog_lister.OrganizationList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
@Slf4j
@Profile("fi")
public class OrganizationEndpointImpl implements OrganizationEndpoint {

    @Autowired
    private JaxbCompanyService jaxbCompanyService;

    @Autowired
    private JaxbOrganizationService jaxbOrganizationService;


    @Override
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

    @Override
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

    @Override
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

    @Override
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

}
