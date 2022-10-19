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

import fi.vrk.xroad.catalog.persistence.dto.LiquidationData;
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
public class LiquidationDataDTOTest {

    @Test
    public void testLiquidationDataDTO() {
        long source = 1;
        long version = 1;
        String language = "FI";
        String name = "name";
        LocalDateTime registrationDate = LocalDateTime.now();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        LiquidationData liquidationData1 = new LiquidationData();
        liquidationData1.setSource(source);
        liquidationData1.setVersion(version);
        liquidationData1.setLanguage(language);
        liquidationData1.setName(name);
        liquidationData1.setRegistrationDate(registrationDate);
        liquidationData1.setEndDate(null);
        liquidationData1.setCreated(created);
        liquidationData1.setChanged(changed);
        liquidationData1.setFetched(fetched);
        liquidationData1.setRemoved(null);
        LiquidationData liquidationData2 = new LiquidationData(source, version, language, name, registrationDate, null,
                created, changed, fetched, null);
        LiquidationData liquidationData3 = LiquidationData.builder().source(source).version(version).language(language).name(name)
                .registrationDate(registrationDate).endDate(null).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(liquidationData1, liquidationData2);
        assertEquals(liquidationData1, liquidationData3);
        assertEquals(liquidationData2, liquidationData3);
        assertEquals(source, liquidationData1.getSource());
        assertEquals(version, liquidationData1.getVersion());
        assertEquals(language, liquidationData1.getLanguage());
        assertEquals(name, liquidationData1.getName());
        assertNotEquals(0, liquidationData1.hashCode());
        assertEquals(true, liquidationData1.equals(liquidationData2));
        assertEquals(source, liquidationData2.getSource());
        assertEquals(version, liquidationData2.getVersion());
        assertEquals(language, liquidationData2.getLanguage());
        assertEquals(name, liquidationData2.getName());
        assertNotEquals(0, liquidationData2.hashCode());
        assertEquals(true, liquidationData2.equals(liquidationData3));
        assertEquals(source, liquidationData3.getSource());
        assertEquals(version, liquidationData3.getVersion());
        assertEquals(language, liquidationData3.getLanguage());
        assertEquals(name, liquidationData3.getName());
        assertNotEquals(0, liquidationData3.hashCode());
        assertEquals(true, liquidationData3.equals(liquidationData1));
    }

}


