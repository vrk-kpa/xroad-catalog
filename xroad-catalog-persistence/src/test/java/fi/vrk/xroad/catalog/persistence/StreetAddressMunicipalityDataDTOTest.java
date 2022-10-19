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

import fi.vrk.xroad.catalog.persistence.dto.StreetAddressMunicipalityData;
import fi.vrk.xroad.catalog.persistence.dto.StreetAddressMunicipalityNameData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class StreetAddressMunicipalityDataDTOTest {

    @Test
    public void testStreetAddressMunicipalityDataDTO() {
        String code = "123abc";
        List<StreetAddressMunicipalityNameData> streetAddressMunicipalityNames = new ArrayList<>();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        StreetAddressMunicipalityData streetAddressMunicipalityData1 = new StreetAddressMunicipalityData();
        streetAddressMunicipalityData1.setCode(code);
        streetAddressMunicipalityData1.setStreetAddressMunicipalityNames(streetAddressMunicipalityNames);
        streetAddressMunicipalityData1.setCreated(created);
        streetAddressMunicipalityData1.setChanged(changed);
        streetAddressMunicipalityData1.setFetched(fetched);
        streetAddressMunicipalityData1.setRemoved(null);
        StreetAddressMunicipalityData streetAddressMunicipalityData2 = new StreetAddressMunicipalityData(code, streetAddressMunicipalityNames,
                created, changed, fetched, null);
        StreetAddressMunicipalityData streetAddressMunicipalityData3 = StreetAddressMunicipalityData.builder().code(code)
                .streetAddressMunicipalityNames(streetAddressMunicipalityNames).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(streetAddressMunicipalityData1, streetAddressMunicipalityData2);
        assertEquals(streetAddressMunicipalityData1, streetAddressMunicipalityData3);
        assertEquals(streetAddressMunicipalityData2, streetAddressMunicipalityData3);
        assertEquals(code, streetAddressMunicipalityData1.getCode());
        assertEquals(streetAddressMunicipalityNames, streetAddressMunicipalityData1.getStreetAddressMunicipalityNames());
        assertEquals(created, streetAddressMunicipalityData1.getCreated());
        assertEquals(changed, streetAddressMunicipalityData1.getChanged());
        assertEquals(fetched, streetAddressMunicipalityData1.getFetched());
        assertTrue(streetAddressMunicipalityData1.equals(streetAddressMunicipalityData2));
        assertNotNull(streetAddressMunicipalityData1.hashCode());
        assertEquals(code, streetAddressMunicipalityData2.getCode());
        assertEquals(streetAddressMunicipalityNames, streetAddressMunicipalityData2.getStreetAddressMunicipalityNames());
        assertEquals(created, streetAddressMunicipalityData2.getCreated());
        assertEquals(changed, streetAddressMunicipalityData2.getChanged());
        assertEquals(fetched, streetAddressMunicipalityData2.getFetched());
        assertTrue(streetAddressMunicipalityData2.equals(streetAddressMunicipalityData3));
        assertNotNull(streetAddressMunicipalityData2.hashCode());
        assertEquals(code, streetAddressMunicipalityData3.getCode());
        assertEquals(streetAddressMunicipalityNames, streetAddressMunicipalityData3.getStreetAddressMunicipalityNames());
        assertEquals(created, streetAddressMunicipalityData3.getCreated());
        assertEquals(changed, streetAddressMunicipalityData3.getChanged());
        assertEquals(fetched, streetAddressMunicipalityData3.getFetched());
        assertTrue(streetAddressMunicipalityData3.equals(streetAddressMunicipalityData2));
        assertNotNull(streetAddressMunicipalityData3.hashCode());
    }

}


