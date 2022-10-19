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

import fi.vrk.xroad.catalog.persistence.dto.OrganizationDescriptionData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class OrganizationDescriptionDataDTOTest {

    @Test
    public void testOrganizationDescriptionDataDTO() {
        String language = "FI";
        String type = "type";
        String value = "value";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        OrganizationDescriptionData organizationDescriptionData1 = new OrganizationDescriptionData();
        organizationDescriptionData1.setLanguage(language);
        organizationDescriptionData1.setType(type);
        organizationDescriptionData1.setValue(value);
        organizationDescriptionData1.setCreated(created);
        organizationDescriptionData1.setChanged(changed);
        organizationDescriptionData1.setFetched(fetched);
        organizationDescriptionData1.setRemoved(null);
        OrganizationDescriptionData organizationDescriptionData2 = new OrganizationDescriptionData(language, type, value, created,
                changed, fetched, null);
        OrganizationDescriptionData organizationDescriptionData3 = OrganizationDescriptionData.builder()
                .language(language).type(type).value(value).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(organizationDescriptionData1, organizationDescriptionData2);
        assertEquals(organizationDescriptionData1, organizationDescriptionData3);
        assertEquals(organizationDescriptionData2, organizationDescriptionData3);
        assertEquals(language, organizationDescriptionData1.getLanguage());
        assertEquals(type, organizationDescriptionData1.getType());
        assertEquals(value, organizationDescriptionData1.getValue());
        assertEquals(created, organizationDescriptionData1.getCreated());
        assertEquals(changed, organizationDescriptionData1.getChanged());
        assertEquals(fetched, organizationDescriptionData1.getFetched());
        assertEquals(true, organizationDescriptionData1.equals(organizationDescriptionData2));
        assertNotEquals(0, organizationDescriptionData1.hashCode());
        assertEquals(language, organizationDescriptionData2.getLanguage());
        assertEquals(type, organizationDescriptionData2.getType());
        assertEquals(value, organizationDescriptionData2.getValue());
        assertEquals(created, organizationDescriptionData2.getCreated());
        assertEquals(changed, organizationDescriptionData2.getChanged());
        assertEquals(fetched, organizationDescriptionData2.getFetched());
        assertEquals(true, organizationDescriptionData2.equals(organizationDescriptionData3));
        assertNotEquals(0, organizationDescriptionData2.hashCode());
        assertEquals(language, organizationDescriptionData3.getLanguage());
        assertEquals(type, organizationDescriptionData3.getType());
        assertEquals(value, organizationDescriptionData3.getValue());
        assertEquals(created, organizationDescriptionData3.getCreated());
        assertEquals(changed, organizationDescriptionData3.getChanged());
        assertEquals(fetched, organizationDescriptionData3.getFetched());
    }

}


