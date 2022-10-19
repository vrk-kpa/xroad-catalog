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

import fi.vrk.xroad.catalog.persistence.dto.ContactDetailData;
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
public class ContactDetailDataDTOTest {

    @Test
    public void testContactDetailDataDTO() {
        long source = 1;
        long version = 1;
        String language = "FI";
        String value = "value";
        String type = "type";
        LocalDateTime registrationDate = LocalDateTime.now();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        ContactDetailData contactDetailData1 = new ContactDetailData();
        contactDetailData1.setSource(source);
        contactDetailData1.setVersion(version);
        contactDetailData1.setLanguage(language);
        contactDetailData1.setValue(value);
        contactDetailData1.setType(type);
        contactDetailData1.setRegistrationDate(registrationDate);
        contactDetailData1.setEndDate(null);
        contactDetailData1.setCreated(created);
        contactDetailData1.setChanged(changed);
        contactDetailData1.setFetched(fetched);
        contactDetailData1.setRemoved(null);
        ContactDetailData contactDetailData2 = new ContactDetailData(source, version, language, value, type, registrationDate, null,
                created, changed, fetched, null);
        ContactDetailData contactDetailData3 = ContactDetailData.builder().source(source).version(version).language(language).value(value)
                .type(type).registrationDate(registrationDate).endDate(null).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(contactDetailData1, contactDetailData2);
        assertEquals(contactDetailData1, contactDetailData3);
        assertEquals(contactDetailData2, contactDetailData3);
        assertEquals(value, contactDetailData1.getValue());
        assertEquals(language, contactDetailData1.getLanguage());
        assertEquals(type, contactDetailData1.getType());
        assertNotEquals(0, contactDetailData1.hashCode());
        assertEquals(true, contactDetailData1.equals(contactDetailData2));
        assertEquals(value, contactDetailData2.getValue());
        assertEquals(language, contactDetailData2.getLanguage());
        assertEquals(type, contactDetailData2.getType());
        assertNotEquals(0, contactDetailData2.hashCode());
        assertEquals(true, contactDetailData2.equals(contactDetailData3));
        assertEquals(value, contactDetailData3.getValue());
        assertEquals(language, contactDetailData3.getLanguage());
        assertEquals(type, contactDetailData3.getType());
        assertNotEquals(0, contactDetailData3.hashCode());
        assertEquals(true, contactDetailData3.equals(contactDetailData1));
    }

}


