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

import fi.vrk.xroad.catalog.persistence.dto.ServiceData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class ServiceDataDTOTest {

    @Test
    public void testServiceDataDTO() {
        LocalDateTime created = LocalDateTime.now();
        String serviceCode = "aService";
        String serviceVersion = "v1";
        Boolean active = true;
        ServiceData serviceData1 = new ServiceData();
        serviceData1.setCreated(created);
        serviceData1.setServiceCode(serviceCode);
        serviceData1.setServiceVersion(serviceVersion);
        serviceData1.setActive(active);
        ServiceData serviceData2 = new ServiceData(created, serviceCode, serviceVersion, active);
        ServiceData serviceData3 = ServiceData.builder().created(created).serviceCode(serviceCode).serviceVersion(serviceVersion).active(active).build();
        assertEquals(serviceData1, serviceData2);
        assertEquals(serviceData1, serviceData3);
        assertEquals(serviceData2, serviceData3);
        assertEquals(created, serviceData1.getCreated());
        assertEquals(serviceCode, serviceData1.getServiceCode());
        assertEquals(serviceVersion, serviceData1.getServiceVersion());
        assertEquals(active, serviceData1.getActive());
        assertNotEquals(0, serviceData1.hashCode());
        assertEquals(true, serviceData1.equals(serviceData2));
        assertEquals(created, serviceData2.getCreated());
        assertEquals(serviceCode, serviceData2.getServiceCode());
        assertEquals(serviceVersion, serviceData2.getServiceVersion());
        assertEquals(active, serviceData2.getActive());
        assertNotEquals(0, serviceData2.hashCode());
        assertEquals(true, serviceData2.equals(serviceData3));
        assertEquals(created, serviceData3.getCreated());
        assertEquals(serviceCode, serviceData3.getServiceCode());
        assertEquals(serviceVersion, serviceData3.getServiceVersion());
        assertEquals(active, serviceData3.getActive());
        assertNotEquals(0, serviceData3.hashCode());
        assertEquals(true, serviceData3.equals(serviceData1));
    }

}


