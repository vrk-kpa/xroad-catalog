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

import fi.vrk.xroad.xroad_catalog_lister.GetErrors;
import fi.vrk.xroad.xroad_catalog_lister.GetErrorsResponse;
import fi.vrk.xroad.xroad_catalog_lister.GetOpenAPI;
import fi.vrk.xroad.xroad_catalog_lister.GetOpenAPIResponse;
import fi.vrk.xroad.xroad_catalog_lister.GetServiceType;
import fi.vrk.xroad.xroad_catalog_lister.GetServiceTypeResponse;
import fi.vrk.xroad.xroad_catalog_lister.GetWsdl;
import fi.vrk.xroad.xroad_catalog_lister.GetWsdlResponse;
import fi.vrk.xroad.xroad_catalog_lister.IsProvider;
import fi.vrk.xroad.xroad_catalog_lister.IsProviderResponse;
import fi.vrk.xroad.xroad_catalog_lister.ListMembers;
import fi.vrk.xroad.xroad_catalog_lister.ListMembersResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
@Profile({"default", "fi"})
public interface ServiceEndpoint {

    static final String NAMESPACE_URI = "http://xroad.vrk.fi/xroad-catalog-lister";

    static final String NOT_FOUND = " not found";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ListMembers")
    @ResponsePayload
    ListMembersResponse listMembers(@RequestPayload ListMembers request);

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetServiceType")
    @ResponsePayload
    GetServiceTypeResponse getServiceType(@RequestPayload GetServiceType request);

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "IsProvider")
    @ResponsePayload
    IsProviderResponse isProvider(@RequestPayload IsProvider request);

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetWsdl")
    @ResponsePayload
    GetWsdlResponse getWsdl(@RequestPayload GetWsdl request);

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetOpenAPI")
    @ResponsePayload
    GetOpenAPIResponse getOpenApi(@RequestPayload GetOpenAPI request);

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetErrors")
    @ResponsePayload
    GetErrorsResponse getErrors(@RequestPayload GetErrors request);
}
