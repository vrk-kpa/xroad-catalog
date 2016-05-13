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
package fi.vrk.xroad.catalog.collector.mock;

import fi.vrk.xroad.catalog.collector.util.ClientTypeUtil;
import fi.vrk.xroad.catalog.collector.wsimport.*;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadServiceIdentifierType;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceContext;
import java.util.Collections;
import java.util.List;

/**
 * Mock metaservices -service which answers something valid and semi-reasonable
 * dummy data when queried for listMethods
 */
@WebService
@Slf4j
public class MockMetaServicesImpl implements MetaServicesPort {

    @Resource
    private WebServiceContext ctx;

    @Override
    public List<XRoadServiceIdentifierType> allowedMethods() {
        return Collections.emptyList();
    }

    @Override
    public ListMethodsResponse listMethods(ListMethods listMethods,
                                           Holder<XRoadClientIdentifierType> client,
                                           Holder<XRoadServiceIdentifierType> service,
                                           Holder<String> userId,
                                           Holder<String> id,
                                           Holder<String> protocolVersion) {
        log.info("mock listMethods");
        log.info("client= {}", ClientTypeUtil.toString(client.value));

        ListMethodsResponse response = new ListMethodsResponse();
        response.getService().add(generateService("testServiceFoo", "v1", service.value));
        response.getService().add(generateService("testServiceBar", "v1", service.value));
        response.getService().add(generateService("testServiceBaz", "v1", service.value));
        return response;
    }


    private XRoadServiceIdentifierType generateService(String serviceCode,
                                                       String serviceVersion,
                                                       XRoadServiceIdentifierType serviceHeader) {
        XRoadServiceIdentifierType service = new XRoadServiceIdentifierType();
        service.setXRoadInstance(serviceHeader.getXRoadInstance());
        service.setMemberClass(serviceHeader.getMemberClass());
        service.setMemberCode(serviceHeader.getMemberCode());
        service.setSubsystemCode(serviceHeader.getSubsystemCode());
        service.setServiceCode(serviceCode);
        service.setServiceVersion(serviceVersion);
        service.setObjectType(XRoadObjectType.SERVICE);
        return service;
    }
}
