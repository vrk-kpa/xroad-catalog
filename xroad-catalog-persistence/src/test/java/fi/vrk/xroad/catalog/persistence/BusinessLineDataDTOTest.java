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

import fi.vrk.xroad.catalog.persistence.dto.BusinessLineData;
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
public class BusinessLineDataDTOTest {

    @Test
    public void testBusinessLineDataDTO() {
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        LocalDateTime registrationDate = LocalDateTime.now();
        String name = "Name";
        String language = "Finnish";
        long source = 1;
        long version = 1;
        long ordering = 1;
        BusinessLineData businessLineData1 = new BusinessLineData();
        businessLineData1.setName(name);
        businessLineData1.setLanguage(language);
        businessLineData1.setSource(source);
        businessLineData1.setVersion(version);
        businessLineData1.setOrdering(ordering);
        businessLineData1.setCreated(created);
        businessLineData1.setChanged(changed);
        businessLineData1.setFetched(fetched);
        businessLineData1.setRegistrationDate(registrationDate);
        businessLineData1.setEndDate(null);
        businessLineData1.setRemoved(null);
        BusinessLineData businessLineData2 = new BusinessLineData(source, ordering, version, name, language,
                registrationDate, null, created, changed, fetched, null);
        BusinessLineData businessLineData3 = BusinessLineData.builder().name(name).language(language)
                .source(source).version(version).ordering(ordering).created(created).changed(changed).fetched(fetched)
                .registrationDate(registrationDate).endDate(null).removed(null).build();
        assertEquals(businessLineData1, businessLineData2);
        assertEquals(businessLineData1, businessLineData3);
        assertEquals(businessLineData2, businessLineData3);
        assertEquals(name, businessLineData1.getName());
        assertEquals(language, businessLineData1.getLanguage());
        assertEquals(name, businessLineData2.getName());
        assertEquals(language, businessLineData2.getLanguage());
        assertEquals(name, businessLineData3.getName());
        assertEquals(language, businessLineData3.getLanguage());
    }

}


