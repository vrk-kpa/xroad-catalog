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

import fi.vrk.xroad.catalog.persistence.dto.XRoadData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class XRoadDataDTOTest {

    @Test
    public void testXRoadDataDTO() {
        String xRoadInstance = "DEV";
        String memberClass = "GOV";
        String memberCode = "1234";
        String subsystemCode = "TEST";
        XRoadData xRoadData1 = new XRoadData();
        xRoadData1.setXRoadInstance(xRoadInstance);
        xRoadData1.setMemberClass(memberClass);
        xRoadData1.setMemberCode(memberCode);
        xRoadData1.setSubsystemCode(subsystemCode);
        XRoadData xRoadData2 = new XRoadData(xRoadInstance, memberClass, memberCode, subsystemCode);
        XRoadData xRoadData3 = XRoadData.builder().xRoadInstance(xRoadInstance).memberClass(memberClass)
                .memberCode(memberCode).subsystemCode(subsystemCode).build();
        assertEquals(xRoadData1, xRoadData2);
        assertEquals(xRoadData1, xRoadData3);
        assertEquals(xRoadData2, xRoadData3);
        assertEquals(xRoadInstance, xRoadData1.getXRoadInstance());
        assertEquals(memberClass, xRoadData1.getMemberClass());
        assertEquals(memberCode, xRoadData1.getMemberCode());
        assertEquals(subsystemCode, xRoadData1.getSubsystemCode());
        assertTrue(xRoadData1.equals(xRoadData2));
        assertTrue(xRoadData1.hashCode() != 0);
        assertEquals(xRoadInstance, xRoadData2.getXRoadInstance());
        assertEquals(memberClass, xRoadData2.getMemberClass());
        assertEquals(memberCode, xRoadData2.getMemberCode());
        assertEquals(subsystemCode, xRoadData2.getSubsystemCode());
        assertTrue(xRoadData2.equals(xRoadData3));
        assertTrue(xRoadData2.hashCode() != 0);
        assertEquals(xRoadInstance, xRoadData3.getXRoadInstance());
        assertEquals(memberClass, xRoadData3.getMemberClass());
        assertEquals(memberCode, xRoadData3.getMemberCode());
        assertEquals(subsystemCode, xRoadData3.getSubsystemCode());
        assertTrue(xRoadData3.equals(xRoadData1));
        assertTrue(xRoadData3.hashCode() != 0);
    }

}


