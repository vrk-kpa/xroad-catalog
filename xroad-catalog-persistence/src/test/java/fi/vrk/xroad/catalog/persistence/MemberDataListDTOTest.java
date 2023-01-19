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

import fi.vrk.xroad.catalog.persistence.dto.MemberData;
import fi.vrk.xroad.catalog.persistence.dto.MemberDataList;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest
public class MemberDataListDTOTest {

    @Test
    public void testMemberDataListDTO() {
        LocalDateTime date = LocalDateTime.now();
        List<MemberData> memberDataList = new ArrayList<>();
        MemberDataList memberDataList1 = new MemberDataList();
        memberDataList1.setDate(date);
        memberDataList1.setMemberDataList(memberDataList);
        MemberDataList memberDataList2 = new MemberDataList(date, memberDataList);
        MemberDataList memberDataList3 = MemberDataList.builder().date(date).memberDataList(memberDataList).build();
        assertEquals(memberDataList1, memberDataList2);
        assertEquals(memberDataList1, memberDataList3);
        assertEquals(memberDataList2, memberDataList3);
        assertEquals(date, memberDataList1.getDate());
        assertEquals(memberDataList, memberDataList1.getMemberDataList());
        assertNotEquals(0, memberDataList1.hashCode());
        assertEquals(true, memberDataList1.equals(memberDataList2));
        assertEquals(date, memberDataList2.getDate());
        assertEquals(memberDataList, memberDataList2.getMemberDataList());
        assertNotEquals(0, memberDataList2.hashCode());
        assertEquals(true, memberDataList2.equals(memberDataList3));
        assertEquals(date, memberDataList3.getDate());
        assertEquals(memberDataList, memberDataList3.getMemberDataList());
        assertNotEquals(0, memberDataList3.hashCode());
        assertEquals(true, memberDataList3.equals(memberDataList1));
    }

}


