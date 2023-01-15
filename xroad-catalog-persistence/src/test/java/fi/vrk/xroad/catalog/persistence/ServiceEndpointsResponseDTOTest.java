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
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.dto.EndpointData;
import fi.vrk.xroad.catalog.persistence.dto.ServiceEndpointsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class ServiceEndpointsResponseDTOTest {

    @Test
    public void testServiceEndpointsResponseDTO() {
        String xRoadInstance = "DEV";
        String memberClass = "GOV";
        String memberCode = "1234";
        String subsystemCode = "TEST";
        String serviceCode = "myService";
        String serviceVersion = "v1";
        List<EndpointData> endpointList = new ArrayList<>();
        ServiceEndpointsResponse serviceEndpointsResponse1 = new ServiceEndpointsResponse();
        serviceEndpointsResponse1.setXRoadInstance(xRoadInstance);
        serviceEndpointsResponse1.setMemberClass(memberClass);
        serviceEndpointsResponse1.setMemberCode(memberCode);
        serviceEndpointsResponse1.setSubsystemCode(subsystemCode);
        serviceEndpointsResponse1.setServiceCode(serviceCode);
        serviceEndpointsResponse1.setServiceVersion(serviceVersion);
        serviceEndpointsResponse1.setEndpointList(endpointList);
        ServiceEndpointsResponse serviceEndpointsResponse2 = new ServiceEndpointsResponse(xRoadInstance, memberClass, memberCode, subsystemCode,
                serviceCode, serviceVersion, endpointList);
        ServiceEndpointsResponse serviceEndpointsResponse3 = ServiceEndpointsResponse.builder().xRoadInstance(xRoadInstance).memberClass(memberClass)
                .memberCode(memberCode).subsystemCode(subsystemCode).serviceCode(serviceCode).serviceVersion(serviceVersion).endpointList(endpointList).build();
        assertEquals(serviceEndpointsResponse1, serviceEndpointsResponse2);
        assertEquals(serviceEndpointsResponse1, serviceEndpointsResponse3);
        assertEquals(serviceEndpointsResponse2, serviceEndpointsResponse3);
        assertEquals(xRoadInstance, serviceEndpointsResponse1.getXRoadInstance());
        assertEquals(memberClass, serviceEndpointsResponse1.getMemberClass());
        assertEquals(memberCode, serviceEndpointsResponse1.getMemberCode());
        assertEquals(subsystemCode, serviceEndpointsResponse1.getSubsystemCode());
        assertEquals(serviceCode, serviceEndpointsResponse1.getServiceCode());
        assertEquals(serviceVersion, serviceEndpointsResponse1.getServiceVersion());
        assertEquals(endpointList, serviceEndpointsResponse1.getEndpointList());
        assertEquals(xRoadInstance, serviceEndpointsResponse2.getXRoadInstance());
        assertEquals(memberClass, serviceEndpointsResponse2.getMemberClass());
        assertEquals(memberCode, serviceEndpointsResponse2.getMemberCode());
        assertEquals(subsystemCode, serviceEndpointsResponse2.getSubsystemCode());
        assertEquals(serviceCode, serviceEndpointsResponse2.getServiceCode());
        assertEquals(serviceVersion, serviceEndpointsResponse2.getServiceVersion());
        assertEquals(endpointList, serviceEndpointsResponse2.getEndpointList());
        assertEquals(xRoadInstance, serviceEndpointsResponse3.getXRoadInstance());
        assertEquals(memberClass, serviceEndpointsResponse3.getMemberClass());
        assertEquals(memberCode, serviceEndpointsResponse3.getMemberCode());
        assertEquals(subsystemCode, serviceEndpointsResponse3.getSubsystemCode());
        assertEquals(serviceCode, serviceEndpointsResponse3.getServiceCode());
        assertEquals(serviceVersion, serviceEndpointsResponse3.getServiceVersion());
        assertEquals(true, serviceEndpointsResponse3.equals(serviceEndpointsResponse1));
        assertNotEquals(0, serviceEndpointsResponse3.hashCode());
    }

}


