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

import fi.vrk.xroad.catalog.persistence.dto.OrganizationNameData;
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
public class OrganizationNameDataDTOTest {

    @Test
    public void testOrganizationNameDataDTO() {
        String language = "FI";
        String type = "type";
        String value = "value";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        OrganizationNameData organizationNameData1 = new OrganizationNameData();
        organizationNameData1.setLanguage(language);
        organizationNameData1.setType(type);
        organizationNameData1.setValue(value);
        organizationNameData1.setCreated(created);
        organizationNameData1.setChanged(changed);
        organizationNameData1.setFetched(fetched);
        organizationNameData1.setRemoved(null);
        OrganizationNameData organizationNameData2 = new OrganizationNameData(language, type, value, created,
                changed, fetched, null);
        OrganizationNameData organizationNameData3 = OrganizationNameData.builder()
                .language(language).type(type).value(value).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(organizationNameData1, organizationNameData2);
        assertEquals(organizationNameData1, organizationNameData3);
        assertEquals(organizationNameData2, organizationNameData3);
        assertEquals(language, organizationNameData1.getLanguage());
        assertEquals(type, organizationNameData1.getType());
        assertEquals(value, organizationNameData1.getValue());
        assertEquals(created, organizationNameData1.getCreated());
        assertEquals(changed, organizationNameData1.getChanged());
        assertEquals(fetched, organizationNameData1.getFetched());
        assertEquals(language, organizationNameData1.getLanguage());
        assertEquals(type, organizationNameData2.getType());
        assertEquals(value, organizationNameData2.getValue());
        assertEquals(created, organizationNameData2.getCreated());
        assertEquals(changed, organizationNameData2.getChanged());
        assertEquals(fetched, organizationNameData2.getFetched());
        assertEquals(language, organizationNameData3.getLanguage());
        assertEquals(type, organizationNameData3.getType());
        assertEquals(value, organizationNameData3.getValue());
        assertEquals(created, organizationNameData3.getCreated());
        assertEquals(changed, organizationNameData3.getChanged());
        assertEquals(fetched, organizationNameData3.getFetched());
        assertEquals(true, organizationNameData1.equals(organizationNameData2));
        assertNotEquals(0, organizationNameData1.hashCode());
    }

}


