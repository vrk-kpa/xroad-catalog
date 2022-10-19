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

import fi.vrk.xroad.catalog.persistence.dto.StreetAddressAdditionalInformationData;
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
public class StreetAddressAdditionalInformationDataDTOTest {

    @Test
    public void testStreetAddressAdditionalInformationDataDTO() {
        String language = "FI";
        String value = "value";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        StreetAddressAdditionalInformationData streetAddressAdditionalInformationData1 = new StreetAddressAdditionalInformationData();
        streetAddressAdditionalInformationData1.setLanguage(language);
        streetAddressAdditionalInformationData1.setValue(value);
        streetAddressAdditionalInformationData1.setCreated(created);
        streetAddressAdditionalInformationData1.setChanged(changed);
        streetAddressAdditionalInformationData1.setFetched(fetched);
        streetAddressAdditionalInformationData1.setRemoved(null);
        StreetAddressAdditionalInformationData streetAddressAdditionalInformationData2 = new StreetAddressAdditionalInformationData(language, value, created,
                changed, fetched, null);
        StreetAddressAdditionalInformationData streetAddressAdditionalInformationData3 = StreetAddressAdditionalInformationData.builder().language(language)
                .value(value).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(streetAddressAdditionalInformationData1, streetAddressAdditionalInformationData2);
        assertEquals(streetAddressAdditionalInformationData1, streetAddressAdditionalInformationData3);
        assertEquals(streetAddressAdditionalInformationData2, streetAddressAdditionalInformationData3);
        assertEquals(language, streetAddressAdditionalInformationData1.getLanguage());
        assertEquals(value, streetAddressAdditionalInformationData1.getValue());
        assertEquals(created, streetAddressAdditionalInformationData1.getCreated());
        assertEquals(changed, streetAddressAdditionalInformationData1.getChanged());
        assertEquals(fetched, streetAddressAdditionalInformationData1.getFetched());
        assertTrue(streetAddressAdditionalInformationData1.equals(streetAddressAdditionalInformationData2));
        assertTrue(streetAddressAdditionalInformationData1.hashCode() != 0);
        assertEquals(language, streetAddressAdditionalInformationData2.getLanguage());
        assertEquals(value, streetAddressAdditionalInformationData2.getValue());
        assertEquals(created, streetAddressAdditionalInformationData2.getCreated());
        assertEquals(changed, streetAddressAdditionalInformationData2.getChanged());
        assertEquals(fetched, streetAddressAdditionalInformationData2.getFetched());
        assertTrue(streetAddressAdditionalInformationData2.equals(streetAddressAdditionalInformationData3));
        assertTrue(streetAddressAdditionalInformationData2.hashCode() != 0);
        assertEquals(language, streetAddressAdditionalInformationData3.getLanguage());
        assertEquals(value, streetAddressAdditionalInformationData3.getValue());
        assertEquals(created, streetAddressAdditionalInformationData3.getCreated());
        assertEquals(changed, streetAddressAdditionalInformationData3.getChanged());
        assertEquals(fetched, streetAddressAdditionalInformationData3.getFetched());
        assertTrue(streetAddressAdditionalInformationData3.equals(streetAddressAdditionalInformationData2));
        assertTrue(streetAddressAdditionalInformationData3.hashCode() != 0);
    }

}


