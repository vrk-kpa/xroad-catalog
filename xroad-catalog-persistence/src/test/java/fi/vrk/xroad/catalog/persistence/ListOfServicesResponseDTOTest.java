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

import fi.vrk.xroad.catalog.persistence.dto.ListOfServicesResponse;
import fi.vrk.xroad.catalog.persistence.dto.MemberDataList;
import fi.vrk.xroad.catalog.persistence.dto.SecurityServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class ListOfServicesResponseDTOTest {

    @Test
    public void testListOfServicesResponseDTO() {
        List<MemberDataList> memberData = new ArrayList<>();
        List<SecurityServerInfo> securityServerData = new ArrayList<>();
        ListOfServicesResponse listOfServicesResponse1 = new ListOfServicesResponse();
        listOfServicesResponse1.setMemberData(memberData);
        listOfServicesResponse1.setSecurityServerData(securityServerData);
        ListOfServicesResponse listOfServicesResponse2 = new ListOfServicesResponse(memberData, securityServerData);
        ListOfServicesResponse listOfServicesResponse3 = ListOfServicesResponse.builder().memberData(memberData)
                .securityServerData(securityServerData).build();
        assertEquals(listOfServicesResponse1, listOfServicesResponse2);
        assertEquals(listOfServicesResponse1, listOfServicesResponse3);
        assertEquals(listOfServicesResponse2, listOfServicesResponse3);
        assertEquals(memberData, listOfServicesResponse1.getMemberData());
        assertEquals(securityServerData, listOfServicesResponse1.getSecurityServerData());
        assertNotEquals(0, listOfServicesResponse1.hashCode());
        assertEquals(true, listOfServicesResponse1.equals(listOfServicesResponse2));
        assertEquals(memberData, listOfServicesResponse2.getMemberData());
        assertEquals(securityServerData, listOfServicesResponse2.getSecurityServerData());
        assertNotEquals(0, listOfServicesResponse2.hashCode());
        assertEquals(true, listOfServicesResponse2.equals(listOfServicesResponse3));
        assertEquals(memberData, listOfServicesResponse3.getMemberData());
        assertEquals(securityServerData, listOfServicesResponse3.getSecurityServerData());
        assertNotEquals(0, listOfServicesResponse3.hashCode());
        assertEquals(true, listOfServicesResponse3.equals(listOfServicesResponse1));
    }

}


