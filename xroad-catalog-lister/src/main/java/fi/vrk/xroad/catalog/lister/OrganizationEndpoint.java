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

import fi.vrk.xroad.xroad_catalog_lister.GetCompanies;
import fi.vrk.xroad.xroad_catalog_lister.GetCompaniesResponse;
import fi.vrk.xroad.xroad_catalog_lister.GetOrganizations;
import fi.vrk.xroad.xroad_catalog_lister.GetOrganizationsResponse;
import fi.vrk.xroad.xroad_catalog_lister.HasCompanyChanged;
import fi.vrk.xroad.xroad_catalog_lister.HasCompanyChangedResponse;
import fi.vrk.xroad.xroad_catalog_lister.HasOrganizationChanged;
import fi.vrk.xroad.xroad_catalog_lister.HasOrganizationChangedResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
@Profile("fi")
public interface OrganizationEndpoint {

    static final String NAMESPACE_URI = "http://xroad.vrk.fi/xroad-catalog-lister";

    static final String NOT_FOUND = " not found";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetOrganizations")
    @ResponsePayload
    GetOrganizationsResponse getOrganizations(@RequestPayload GetOrganizations request);

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "HasOrganizationChanged")
    @ResponsePayload
    HasOrganizationChangedResponse hasOrganizationChanged(@RequestPayload HasOrganizationChanged request);

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetCompanies")
    @ResponsePayload
    GetCompaniesResponse getCompanies(@RequestPayload GetCompanies request);

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "HasCompanyChanged")
    @ResponsePayload
    HasCompanyChangedResponse hasCompanyChanged(@RequestPayload HasCompanyChanged request);
}
