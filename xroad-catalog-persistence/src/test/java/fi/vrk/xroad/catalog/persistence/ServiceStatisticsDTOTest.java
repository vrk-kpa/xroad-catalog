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
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.dto.ServiceStatistics;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class ServiceStatisticsDTOTest {

    @Test
    public void testServiceStatisticsDTO() {
        LocalDateTime created = LocalDateTime.now();
        Long numberOfSoapServices = 1L;
        Long numberOfRestServices = 2L;
        Long numberOfOpenApiServices = 3L;
        ServiceStatistics serviceStatistics1 = new ServiceStatistics();
        serviceStatistics1.setCreated(created);
        serviceStatistics1.setNumberOfSoapServices(numberOfSoapServices);
        serviceStatistics1.setNumberOfRestServices(numberOfRestServices);
        serviceStatistics1.setNumberOfOpenApiServices(numberOfOpenApiServices);
        ServiceStatistics serviceStatistics2 = new ServiceStatistics(created, numberOfSoapServices, numberOfRestServices, numberOfOpenApiServices);
        ServiceStatistics serviceStatistics3 = ServiceStatistics.builder().created(created).numberOfSoapServices(numberOfSoapServices)
                .numberOfRestServices(numberOfRestServices).numberOfOpenApiServices(numberOfOpenApiServices).build();
        assertEquals(serviceStatistics1, serviceStatistics2);
        assertEquals(serviceStatistics1, serviceStatistics3);
        assertEquals(serviceStatistics2, serviceStatistics3);
        assertEquals(created, serviceStatistics1.getCreated());
        assertEquals(numberOfSoapServices, serviceStatistics1.getNumberOfSoapServices());
        assertEquals(numberOfRestServices, serviceStatistics1.getNumberOfRestServices());
        assertEquals(numberOfOpenApiServices, serviceStatistics1.getNumberOfOpenApiServices());
        assertTrue(serviceStatistics1.equals(serviceStatistics2));
        assertTrue(serviceStatistics1.hashCode() != 0);
        assertTrue(serviceStatistics1.toString().contains("numberOfRestServices"));
        assertEquals(created, serviceStatistics2.getCreated());
        assertEquals(numberOfSoapServices, serviceStatistics2.getNumberOfSoapServices());
        assertEquals(numberOfRestServices, serviceStatistics2.getNumberOfRestServices());
        assertEquals(numberOfOpenApiServices, serviceStatistics2.getNumberOfOpenApiServices());
        assertTrue(serviceStatistics2.equals(serviceStatistics3));
        assertTrue(serviceStatistics2.toString().contains("numberOfSoapServices"));
        assertTrue(serviceStatistics2.hashCode() != 0);
        assertEquals(created, serviceStatistics3.getCreated());
        assertEquals(numberOfSoapServices, serviceStatistics3.getNumberOfSoapServices());
        assertEquals(numberOfRestServices, serviceStatistics3.getNumberOfRestServices());
        assertEquals(numberOfOpenApiServices, serviceStatistics3.getNumberOfOpenApiServices());
        assertTrue(serviceStatistics3.equals(serviceStatistics1));
        assertTrue(serviceStatistics3.toString().contains("numberOfOpenApiServices"));
        assertTrue(serviceStatistics3.hashCode() != 0);
    }

}


