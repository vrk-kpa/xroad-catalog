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

import fi.vrk.xroad.catalog.persistence.dto.StreetAddressPostOfficeData;
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
public class StreetAddressPostOfficeDataDTOTest {

    @Test
    public void testStreetAddressPostOfficeDataDTO() {
        String language = "FI";
        String value = "value";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        StreetAddressPostOfficeData streetAddressPostOfficeData1 = new StreetAddressPostOfficeData();
        streetAddressPostOfficeData1.setLanguage(language);
        streetAddressPostOfficeData1.setValue(value);
        streetAddressPostOfficeData1.setCreated(created);
        streetAddressPostOfficeData1.setChanged(changed);
        streetAddressPostOfficeData1.setFetched(fetched);
        streetAddressPostOfficeData1.setRemoved(null);
        StreetAddressPostOfficeData streetAddressPostOfficeData2 = new StreetAddressPostOfficeData(language, value,
                created, changed, fetched, null);
        StreetAddressPostOfficeData streetAddressPostOfficeData3 = StreetAddressPostOfficeData.builder().language(language)
                .value(value).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(streetAddressPostOfficeData1, streetAddressPostOfficeData2);
        assertEquals(streetAddressPostOfficeData1, streetAddressPostOfficeData3);
        assertEquals(streetAddressPostOfficeData2, streetAddressPostOfficeData3);
        assertEquals(language, streetAddressPostOfficeData1.getLanguage());
        assertEquals(value, streetAddressPostOfficeData1.getValue());
        assertEquals(created, streetAddressPostOfficeData1.getCreated());
        assertEquals(changed, streetAddressPostOfficeData1.getChanged());
        assertEquals(fetched, streetAddressPostOfficeData1.getFetched());
        assertNotEquals(0, streetAddressPostOfficeData1.hashCode());
        assertEquals(true, streetAddressPostOfficeData1.equals(streetAddressPostOfficeData2));
        assertEquals(value, streetAddressPostOfficeData2.getValue());
        assertEquals(created, streetAddressPostOfficeData2.getCreated());
        assertEquals(changed, streetAddressPostOfficeData2.getChanged());
        assertEquals(fetched, streetAddressPostOfficeData2.getFetched());
        assertNotEquals(0, streetAddressPostOfficeData2.hashCode());
        assertEquals(true, streetAddressPostOfficeData2.equals(streetAddressPostOfficeData3));
        assertEquals(value, streetAddressPostOfficeData3.getValue());
        assertEquals(created, streetAddressPostOfficeData3.getCreated());
        assertEquals(changed, streetAddressPostOfficeData3.getChanged());
        assertEquals(fetched, streetAddressPostOfficeData3.getFetched());
        assertNotEquals(0, streetAddressPostOfficeData3.hashCode());
        assertEquals(true, streetAddressPostOfficeData3.equals(streetAddressPostOfficeData1));
    }

}


