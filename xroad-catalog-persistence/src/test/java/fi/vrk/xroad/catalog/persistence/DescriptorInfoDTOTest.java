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

import fi.vrk.xroad.catalog.persistence.dto.DescriptorInfo;
import fi.vrk.xroad.catalog.persistence.dto.SubsystemName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class DescriptorInfoDTOTest {

    @Test
    public void testDescriptorInfoDTO() {
        String xRoadInstance = "DEV";
        SubsystemName subsystemName = SubsystemName.builder().en("Subsystem in EN").et("Subsystem in ET").build();
        String memberClass = "GOV";
        String memberCode = "1234";
        String memberName = "MEMBER";
        String subsystemCode = "TEST";
        DescriptorInfo descriptorInfo1 = new DescriptorInfo();
        descriptorInfo1.setXRoadInstance(xRoadInstance);
        descriptorInfo1.setSubsystemName(subsystemName);
        descriptorInfo1.setEmail(new ArrayList<>());
        descriptorInfo1.setMemberClass(memberClass);
        descriptorInfo1.setMemberCode(memberCode);
        descriptorInfo1.setMemberName(memberName);
        descriptorInfo1.setSubsystemCode(subsystemCode);
        DescriptorInfo descriptorInfo2 = new DescriptorInfo(xRoadInstance, subsystemName, new ArrayList<>(), memberClass,
                memberCode, memberName, subsystemCode);
        DescriptorInfo descriptorInfo3 = DescriptorInfo.builder().xRoadInstance(xRoadInstance).subsystemName(subsystemName)
                .email(new ArrayList<>()).memberClass(memberClass).memberCode(memberCode).memberName(memberName).subsystemCode(subsystemCode).build();
        assertEquals(descriptorInfo1, descriptorInfo2);
        assertEquals(descriptorInfo1, descriptorInfo3);
        assertEquals(descriptorInfo2, descriptorInfo3);
        assertEquals(xRoadInstance, descriptorInfo1.getXRoadInstance());
        assertEquals(memberClass, descriptorInfo1.getMemberClass());
        assertEquals(memberCode, descriptorInfo1.getMemberCode());
        assertEquals(memberName, descriptorInfo1.getMemberName());
        assertEquals(subsystemCode, descriptorInfo1.getSubsystemCode());
        assertNotEquals(0, descriptorInfo1.hashCode());
        assertEquals(true, descriptorInfo1.equals(descriptorInfo2));
        assertEquals(xRoadInstance, descriptorInfo2.getXRoadInstance());
        assertEquals(memberClass, descriptorInfo2.getMemberClass());
        assertEquals(memberCode, descriptorInfo2.getMemberCode());
        assertEquals(memberName, descriptorInfo2.getMemberName());
        assertNotEquals(0, descriptorInfo2.hashCode());
        assertEquals(true, descriptorInfo2.equals(descriptorInfo3));
        assertEquals(subsystemCode, descriptorInfo2.getSubsystemCode());
        assertEquals(xRoadInstance, descriptorInfo3.getXRoadInstance());
        assertEquals(memberClass, descriptorInfo3.getMemberClass());
        assertEquals(memberCode, descriptorInfo3.getMemberCode());
        assertEquals(memberName, descriptorInfo3.getMemberName());
        assertEquals(subsystemCode, descriptorInfo3.getSubsystemCode());
        assertNotEquals(0, descriptorInfo3.hashCode());
        assertEquals(true, descriptorInfo3.equals(descriptorInfo1));
    }

}


