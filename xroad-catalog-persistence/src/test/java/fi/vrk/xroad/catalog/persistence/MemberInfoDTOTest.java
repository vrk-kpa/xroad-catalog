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

import fi.vrk.xroad.catalog.persistence.dto.MemberInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class MemberInfoDTOTest {

    @Test
    public void testMemberInfoDTO() {
        String memberClass = "GOV";
        String memberCode = "1234";
        String name = "MEMBER";
        String subsystemCode = "TEST";
        MemberInfo memberInfo1 = new MemberInfo();
        memberInfo1.setMemberClass(memberClass);
        memberInfo1.setMemberCode(memberCode);
        memberInfo1.setName(name);
        memberInfo1.setSubsystemCode(subsystemCode);
        MemberInfo memberInfo2 = new MemberInfo(memberClass, memberCode, name, subsystemCode);
        MemberInfo memberInfo3 = MemberInfo.builder().memberClass(memberClass).memberCode(memberCode).name(name).subsystemCode(subsystemCode).build();
        assertEquals(memberInfo1, memberInfo2);
        assertEquals(memberInfo1, memberInfo3);
        assertEquals(memberInfo2, memberInfo3);
        assertEquals(memberClass, memberInfo1.getMemberClass());
        assertEquals(memberCode, memberInfo1.getMemberCode());
        assertEquals(name, memberInfo1.getName());
        assertEquals(subsystemCode, memberInfo1.getSubsystemCode());
        assertNotEquals(0, memberInfo1.hashCode());
        assertEquals(true, memberInfo1.equals(memberInfo2));
        assertEquals(memberClass, memberInfo2.getMemberClass());
        assertEquals(memberCode, memberInfo2.getMemberCode());
        assertEquals(name, memberInfo2.getName());
        assertEquals(subsystemCode, memberInfo2.getSubsystemCode());
        assertNotEquals(0, memberInfo2.hashCode());
        assertEquals(true, memberInfo2.equals(memberInfo3));
        assertEquals(memberClass, memberInfo3.getMemberClass());
        assertEquals(memberCode, memberInfo3.getMemberCode());
        assertEquals(name, memberInfo3.getName());
        assertEquals(subsystemCode, memberInfo3.getSubsystemCode());
        assertNotEquals(0, memberInfo3.hashCode());
        assertEquals(true, memberInfo3.equals(memberInfo1));
    }

}


