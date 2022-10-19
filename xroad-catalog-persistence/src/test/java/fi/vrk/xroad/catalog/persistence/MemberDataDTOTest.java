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

import fi.vrk.xroad.catalog.persistence.dto.MemberData;
import fi.vrk.xroad.catalog.persistence.dto.SubsystemData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class MemberDataDTOTest {

    @Test
    public void testMemberDataDTO() {
        LocalDateTime created = LocalDateTime.now();
        String xRoadInstance = "DEV";
        String memberClass = "GOV";
        String memberCode = "1234";
        String name = "MEMBER";
        Boolean provider = true;
        List<SubsystemData> subsystemList = new ArrayList<>();
        MemberData memberData1 = new MemberData();
        memberData1.setCreated(created);
        memberData1.setXRoadInstance(xRoadInstance);
        memberData1.setMemberClass(memberClass);
        memberData1.setMemberCode(memberCode);
        memberData1.setName(name);
        memberData1.setProvider(provider);
        memberData1.setSubsystemList(subsystemList);
        MemberData memberData2 = new MemberData(created, xRoadInstance, memberClass, memberCode, name, provider, subsystemList);
        MemberData memberData3 = MemberData.builder().created(created).xRoadInstance(xRoadInstance).memberClass(memberClass)
                .memberCode(memberCode).name(name).provider(provider).subsystemList(subsystemList).build();
        assertEquals(memberData1, memberData2);
        assertEquals(memberData1, memberData3);
        assertEquals(memberData2, memberData3);
        assertEquals(created, memberData1.getCreated());
        assertEquals(xRoadInstance, memberData1.getXRoadInstance());
        assertEquals(memberClass, memberData1.getMemberClass());
        assertEquals(memberCode, memberData1.getMemberCode());
        assertEquals(name, memberData1.getName());
        assertEquals(provider, memberData1.getProvider());
        assertEquals(subsystemList, memberData1.getSubsystemList());
        assertEquals(created, memberData2.getCreated());
        assertEquals(xRoadInstance, memberData2.getXRoadInstance());
        assertEquals(memberClass, memberData2.getMemberClass());
        assertEquals(memberCode, memberData2.getMemberCode());
        assertEquals(name, memberData2.getName());
        assertEquals(provider, memberData2.getProvider());
        assertEquals(subsystemList, memberData2.getSubsystemList());
        assertEquals(created, memberData3.getCreated());
        assertEquals(xRoadInstance, memberData3.getXRoadInstance());
        assertEquals(memberClass, memberData3.getMemberClass());
        assertEquals(memberCode, memberData3.getMemberCode());
        assertEquals(name, memberData3.getName());
        assertEquals(provider, memberData3.getProvider());
        assertEquals(subsystemList, memberData3.getSubsystemList());
    }

}


