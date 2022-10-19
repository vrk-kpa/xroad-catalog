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

import fi.vrk.xroad.catalog.persistence.dto.StreetAddressMunicipalityNameData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class StreetAddressMunicipalityNameDataDTOTest {

    @Test
    public void testStreetAddressMunicipalityNameDataDTO() {
        String language = "FI";
        String value = "value";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        StreetAddressMunicipalityNameData streetAddressMunicipalityNameData1 = new StreetAddressMunicipalityNameData();
        streetAddressMunicipalityNameData1.setLanguage(language);
        streetAddressMunicipalityNameData1.setValue(value);
        streetAddressMunicipalityNameData1.setCreated(created);
        streetAddressMunicipalityNameData1.setChanged(changed);
        streetAddressMunicipalityNameData1.setFetched(fetched);
        streetAddressMunicipalityNameData1.setRemoved(null);
        StreetAddressMunicipalityNameData streetAddressMunicipalityNameData2 = new StreetAddressMunicipalityNameData(language, value,
                created, changed, fetched, null);
        StreetAddressMunicipalityNameData streetAddressMunicipalityNameData3 = StreetAddressMunicipalityNameData.builder().language(language)
                .value(value).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(streetAddressMunicipalityNameData1, streetAddressMunicipalityNameData2);
        assertEquals(streetAddressMunicipalityNameData1, streetAddressMunicipalityNameData3);
        assertEquals(streetAddressMunicipalityNameData2, streetAddressMunicipalityNameData3);
        assertEquals(language, streetAddressMunicipalityNameData1.getLanguage());
        assertEquals(value, streetAddressMunicipalityNameData1.getValue());
        assertEquals(created, streetAddressMunicipalityNameData1.getCreated());
        assertEquals(changed, streetAddressMunicipalityNameData1.getChanged());
        assertEquals(fetched, streetAddressMunicipalityNameData1.getFetched());
        assertTrue(streetAddressMunicipalityNameData1.equals(streetAddressMunicipalityNameData2));
        assertTrue(streetAddressMunicipalityNameData1.hashCode() != 0);
        assertEquals(value, streetAddressMunicipalityNameData2.getValue());
        assertEquals(created, streetAddressMunicipalityNameData2.getCreated());
        assertEquals(changed, streetAddressMunicipalityNameData2.getChanged());
        assertEquals(fetched, streetAddressMunicipalityNameData2.getFetched());
        assertTrue(streetAddressMunicipalityNameData2.equals(streetAddressMunicipalityNameData3));
        assertTrue(streetAddressMunicipalityNameData2.hashCode() != 0);
        assertEquals(value, streetAddressMunicipalityNameData3.getValue());
        assertEquals(created, streetAddressMunicipalityNameData3.getCreated());
        assertEquals(changed, streetAddressMunicipalityNameData3.getChanged());
        assertEquals(fetched, streetAddressMunicipalityNameData3.getFetched());
        assertTrue(streetAddressMunicipalityNameData3.equals(streetAddressMunicipalityNameData1));
        assertTrue(streetAddressMunicipalityNameData3.hashCode() != 0);
    }

}


