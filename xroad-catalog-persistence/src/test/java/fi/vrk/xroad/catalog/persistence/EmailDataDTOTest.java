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

import fi.vrk.xroad.catalog.persistence.dto.EmailData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class EmailDataDTOTest {

    @Test
    public void testEmailDataDTO() {
        String language = "FI";
        String description = "description";
        String value = "value";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        EmailData emailData1 = new EmailData();
        emailData1.setLanguage(language);
        emailData1.setDescription(description);
        emailData1.setValue(value);
        emailData1.setCreated(created);
        emailData1.setChanged(changed);
        emailData1.setFetched(fetched);
        emailData1.setRemoved(null);
        EmailData emailData2 = new EmailData(language, description, value, created, changed, fetched, null);
        EmailData emailData3 = EmailData.builder().language(language).description(description).value(value)
                .created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(emailData1, emailData2);
        assertEquals(emailData1, emailData3);
        assertEquals(emailData2, emailData3);
        assertEquals(language, emailData1.getLanguage());
        assertEquals(description, emailData1.getDescription());
        assertEquals(value, emailData1.getValue());
        assertEquals(language, emailData2.getLanguage());
        assertEquals(description, emailData2.getDescription());
        assertEquals(value, emailData2.getValue());
        assertEquals(language, emailData3.getLanguage());
        assertEquals(description, emailData3.getDescription());
        assertEquals(value, emailData3.getValue());
    }

}


