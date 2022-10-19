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

import fi.vrk.xroad.catalog.persistence.dto.LanguageData;
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
public class LanguageDataDTOTest {

    @Test
    public void testLanguageDataDTO() {
        long source = 1;
        long version = 1;
        String language = "FI";
        String name = "name";
        LocalDateTime registrationDate = LocalDateTime.now();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        LanguageData languageData1 = new LanguageData();
        languageData1.setSource(source);
        languageData1.setVersion(version);
        languageData1.setLanguage(language);
        languageData1.setName(name);
        languageData1.setRegistrationDate(registrationDate);
        languageData1.setEndDate(null);
        languageData1.setCreated(created);
        languageData1.setChanged(changed);
        languageData1.setFetched(fetched);
        languageData1.setRemoved(null);
        LanguageData languageData2 = new LanguageData(source, version, language, name, registrationDate, null,
                created, changed, fetched, null);
        LanguageData languageData3 = LanguageData.builder().source(source).version(version).language(language).name(name)
                .registrationDate(registrationDate).endDate(null).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(languageData1, languageData2);
        assertEquals(languageData1, languageData3);
        assertEquals(languageData2, languageData3);
        assertEquals(language, languageData1.getLanguage());
        assertEquals(name, languageData1.getName());
        assertNotEquals(0, languageData1.hashCode());
        assertEquals(true, languageData1.equals(languageData2));
        assertEquals(language, languageData2.getLanguage());
        assertEquals(name, languageData2.getName());
        assertNotEquals(0, languageData2.hashCode());
        assertEquals(true, languageData2.equals(languageData3));
        assertEquals(language, languageData3.getLanguage());
        assertEquals(name, languageData3.getName());
        assertNotEquals(0, languageData3.hashCode());
        assertEquals(true, languageData3.equals(languageData1));
    }

}


