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
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.dto.ServiceData;
import fi.vrk.xroad.catalog.persistence.dto.SubsystemData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class SubsystemDataDTOTest {

    @Test
    public void testSubsystemDataDTO() {
        LocalDateTime created = LocalDateTime.now();
        String subsystemCode = "TEST";
        Boolean active = true;
        List<ServiceData> serviceList = new ArrayList<>();
        SubsystemData subsystemData1 = new SubsystemData();
        subsystemData1.setCreated(created);
        subsystemData1.setSubsystemCode(subsystemCode);
        subsystemData1.setActive(active);
        subsystemData1.setServiceList(serviceList);
        SubsystemData subsystemData2 = new SubsystemData(created, subsystemCode, active, serviceList);
        SubsystemData subsystemData3 = SubsystemData.builder().created(created).subsystemCode(subsystemCode)
                .active(active).serviceList(serviceList).build();
        assertEquals(subsystemData1, subsystemData2);
        assertEquals(subsystemData1, subsystemData3);
        assertEquals(subsystemData2, subsystemData3);
        assertEquals(created, subsystemData1.getCreated());
        assertEquals(subsystemCode, subsystemData1.getSubsystemCode());
        assertEquals(active, subsystemData1.getActive());
        assertEquals(serviceList, subsystemData1.getServiceList());
        assertNotEquals(0, subsystemData1.hashCode());
        assertEquals(true, subsystemData1.equals(subsystemData2));
        assertEquals(created, subsystemData2.getCreated());
        assertEquals(subsystemCode, subsystemData2.getSubsystemCode());
        assertEquals(active, subsystemData2.getActive());
        assertEquals(serviceList, subsystemData2.getServiceList());
        assertNotEquals(0, subsystemData2.hashCode());
        assertEquals(true, subsystemData2.equals(subsystemData3));
        assertEquals(created, subsystemData3.getCreated());
        assertEquals(subsystemCode, subsystemData3.getSubsystemCode());
        assertEquals(active, subsystemData3.getActive());
        assertEquals(serviceList, subsystemData3.getServiceList());
        assertNotEquals(0, subsystemData3.hashCode());
        assertEquals(true, subsystemData3.equals(subsystemData1));
    }

}


