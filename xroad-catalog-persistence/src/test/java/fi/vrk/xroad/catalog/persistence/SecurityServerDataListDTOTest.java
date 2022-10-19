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

import fi.vrk.xroad.catalog.persistence.dto.SecurityServerData;
import fi.vrk.xroad.catalog.persistence.dto.SecurityServerDataList;
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
public class SecurityServerDataListDTOTest {

    @Test
    public void testSecurityServerDataListDTO() {
        List<SecurityServerData> securityServerDataList = new ArrayList<>();
        SecurityServerDataList securityServerDataList1 = new SecurityServerDataList();
        securityServerDataList1.setSecurityServerDataList(securityServerDataList);
        SecurityServerDataList securityServerDataList2 = new SecurityServerDataList(securityServerDataList);
        SecurityServerDataList securityServerDataList3 = SecurityServerDataList.builder().securityServerDataList(securityServerDataList).build();
        assertEquals(securityServerDataList1, securityServerDataList2);
        assertEquals(securityServerDataList1, securityServerDataList3);
        assertEquals(securityServerDataList2, securityServerDataList3);
        assertEquals(securityServerDataList, securityServerDataList1.getSecurityServerDataList());
        assertNotEquals(0, securityServerDataList1.hashCode());
        assertEquals(true, securityServerDataList1.equals(securityServerDataList2));
        assertEquals(securityServerDataList, securityServerDataList2.getSecurityServerDataList());
        assertNotEquals(0, securityServerDataList2.hashCode());
        assertEquals(true, securityServerDataList2.equals(securityServerDataList3));
        assertEquals(securityServerDataList, securityServerDataList3.getSecurityServerDataList());
        assertNotEquals(0, securityServerDataList3.hashCode());
        assertEquals(true, securityServerDataList3.equals(securityServerDataList1));
    }

}


