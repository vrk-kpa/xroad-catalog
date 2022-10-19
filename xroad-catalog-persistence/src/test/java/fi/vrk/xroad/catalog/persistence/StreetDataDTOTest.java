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

import fi.vrk.xroad.catalog.persistence.dto.StreetData;
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
public class StreetDataDTOTest {

    @Test
    public void testStreetDataDTO() {
        String language = "FI";
        String value = "value";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        StreetData streetData1 = new StreetData();
        streetData1.setLanguage(language);
        streetData1.setValue(value);
        streetData1.setCreated(created);
        streetData1.setChanged(changed);
        streetData1.setFetched(fetched);
        streetData1.setRemoved(null);
        StreetData streetData2 = new StreetData(language, value,
                created, changed, fetched, null);
        StreetData streetData3 = StreetData.builder().language(language)
                .value(value).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(streetData1, streetData2);
        assertEquals(streetData1, streetData3);
        assertEquals(streetData2, streetData3);
        assertEquals(language, streetData1.getLanguage());
        assertEquals(value, streetData1.getValue());
        assertEquals(created, streetData1.getCreated());
        assertEquals(changed, streetData1.getChanged());
        assertEquals(fetched, streetData1.getFetched());
        assertNotEquals(0, streetData1.hashCode());
        assertEquals(true, streetData1.equals(streetData2));
        assertEquals(value, streetData2.getValue());
        assertEquals(created, streetData2.getCreated());
        assertEquals(changed, streetData2.getChanged());
        assertEquals(fetched, streetData2.getFetched());
        assertNotEquals(0, streetData2.hashCode());
        assertEquals(true, streetData2.equals(streetData3));
        assertEquals(value, streetData3.getValue());
        assertEquals(created, streetData3.getCreated());
        assertEquals(changed, streetData3.getChanged());
        assertEquals(fetched, streetData3.getFetched());
        assertNotEquals(0, streetData3.hashCode());
        assertEquals(true, streetData3.equals(streetData1));
    }

}


