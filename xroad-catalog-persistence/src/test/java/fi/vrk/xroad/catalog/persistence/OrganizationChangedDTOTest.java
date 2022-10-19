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

import fi.vrk.xroad.catalog.persistence.dto.ChangedValue;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationChanged;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class OrganizationChangedDTOTest {

    @Test
    public void testOrganizationChangedDTO() {
        boolean changed = true;
        List<ChangedValue> changedValueList = new ArrayList<>();
        OrganizationChanged organizationChanged1 = new OrganizationChanged();
        organizationChanged1.setChanged(changed);
        organizationChanged1.setChangedValueList(changedValueList);
        OrganizationChanged organizationChanged2 = new OrganizationChanged(changed, changedValueList);
        OrganizationChanged organizationChanged3 = OrganizationChanged.builder().changed(changed).changedValueList(changedValueList).build();
        assertEquals(organizationChanged1, organizationChanged2);
        assertEquals(organizationChanged1, organizationChanged3);
        assertEquals(organizationChanged2, organizationChanged3);
        assertEquals(changed, organizationChanged1.isChanged());
        assertEquals(changedValueList, organizationChanged1.getChangedValueList());
        assertEquals(changed, organizationChanged2.isChanged());
        assertEquals(changedValueList, organizationChanged2.getChangedValueList());
        assertEquals(changed, organizationChanged3.isChanged());
        assertEquals(changedValueList, organizationChanged3.getChangedValueList());
    }

}


