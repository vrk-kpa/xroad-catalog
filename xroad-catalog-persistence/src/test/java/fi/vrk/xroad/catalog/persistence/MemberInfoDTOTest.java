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

import fi.vrk.xroad.catalog.persistence.dto.MemberInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
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
        assertEquals(memberClass, memberInfo2.getMemberClass());
        assertEquals(memberCode, memberInfo2.getMemberCode());
        assertEquals(name, memberInfo2.getName());
        assertEquals(subsystemCode, memberInfo2.getSubsystemCode());
        assertEquals(memberClass, memberInfo3.getMemberClass());
        assertEquals(memberCode, memberInfo3.getMemberCode());
        assertEquals(name, memberInfo3.getName());
        assertEquals(subsystemCode, memberInfo3.getSubsystemCode());
    }

}


