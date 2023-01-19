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

import fi.vrk.xroad.catalog.persistence.dto.ServiceStatistics;
import fi.vrk.xroad.catalog.persistence.dto.ServiceStatisticsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class ServiceStatisticsResponseDTOTest {

    @Test
    public void testServiceStatisticsResponseDTO() {
        List<ServiceStatistics> serviceStatisticsList = new ArrayList<>();
        ServiceStatisticsResponse serviceStatisticsResponse1 = new ServiceStatisticsResponse();
        serviceStatisticsResponse1.setServiceStatisticsList(serviceStatisticsList);
        ServiceStatisticsResponse serviceStatisticsResponse2 = new ServiceStatisticsResponse(serviceStatisticsList);
        ServiceStatisticsResponse serviceStatisticsResponse3 = ServiceStatisticsResponse.builder().serviceStatisticsList(serviceStatisticsList).build();
        assertEquals(serviceStatisticsResponse1, serviceStatisticsResponse2);
        assertEquals(serviceStatisticsResponse1, serviceStatisticsResponse3);
        assertEquals(serviceStatisticsResponse2, serviceStatisticsResponse3);
        assertEquals(serviceStatisticsList, serviceStatisticsResponse1.getServiceStatisticsList());
        assertNotEquals(0, serviceStatisticsResponse1.hashCode());
        assertEquals(true, serviceStatisticsResponse1.equals(serviceStatisticsResponse2));
        assertEquals(60, serviceStatisticsResponse1.hashCode());
        assertEquals(serviceStatisticsList, serviceStatisticsResponse2.getServiceStatisticsList());
        assertNotEquals(0, serviceStatisticsResponse2.hashCode());
        assertEquals(true, serviceStatisticsResponse2.equals(serviceStatisticsResponse3));
        assertEquals(60, serviceStatisticsResponse2.hashCode());
        assertEquals(serviceStatisticsList, serviceStatisticsResponse3.getServiceStatisticsList());
        assertNotEquals(0, serviceStatisticsResponse3.hashCode());
        assertEquals(true, serviceStatisticsResponse3.equals(serviceStatisticsResponse1));
    }

}


