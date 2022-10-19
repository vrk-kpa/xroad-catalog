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

import fi.vrk.xroad.catalog.persistence.dto.PostOfficeBoxAddressMunicipalityNameData;
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
public class PostOfficeBoxAddressMunicipalityNameDataDTOTest {

    @Test
    public void testPostOfficeBoxAddressMunicipalityNameDataDTO() {
        String language = "FI";
        String value = "value";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        PostOfficeBoxAddressMunicipalityNameData postOfficeBoxAddressMunicipalityNameData1 = new PostOfficeBoxAddressMunicipalityNameData();
        postOfficeBoxAddressMunicipalityNameData1.setLanguage(language);
        postOfficeBoxAddressMunicipalityNameData1.setValue(value);
        postOfficeBoxAddressMunicipalityNameData1.setCreated(created);
        postOfficeBoxAddressMunicipalityNameData1.setChanged(changed);
        postOfficeBoxAddressMunicipalityNameData1.setFetched(fetched);
        postOfficeBoxAddressMunicipalityNameData1.setRemoved(null);
        PostOfficeBoxAddressMunicipalityNameData postOfficeBoxAddressMunicipalityNameData2 = new PostOfficeBoxAddressMunicipalityNameData(language, value,
                created, changed, fetched, null);
        PostOfficeBoxAddressMunicipalityNameData postOfficeBoxAddressMunicipalityNameData3 = PostOfficeBoxAddressMunicipalityNameData.builder().language(language)
                .value(value).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(postOfficeBoxAddressMunicipalityNameData1, postOfficeBoxAddressMunicipalityNameData2);
        assertEquals(postOfficeBoxAddressMunicipalityNameData1, postOfficeBoxAddressMunicipalityNameData3);
        assertEquals(postOfficeBoxAddressMunicipalityNameData2, postOfficeBoxAddressMunicipalityNameData3);
        assertEquals(language, postOfficeBoxAddressMunicipalityNameData1.getLanguage());
        assertEquals(value, postOfficeBoxAddressMunicipalityNameData1.getValue());
        assertEquals(created, postOfficeBoxAddressMunicipalityNameData1.getCreated());
        assertEquals(changed, postOfficeBoxAddressMunicipalityNameData1.getChanged());
        assertEquals(fetched, postOfficeBoxAddressMunicipalityNameData1.getFetched());
        assertEquals(language, postOfficeBoxAddressMunicipalityNameData2.getLanguage());
        assertEquals(value, postOfficeBoxAddressMunicipalityNameData2.getValue());
        assertEquals(created, postOfficeBoxAddressMunicipalityNameData2.getCreated());
        assertEquals(changed, postOfficeBoxAddressMunicipalityNameData2.getChanged());
        assertEquals(fetched, postOfficeBoxAddressMunicipalityNameData2.getFetched());
        assertEquals(language, postOfficeBoxAddressMunicipalityNameData3.getLanguage());
        assertEquals(value, postOfficeBoxAddressMunicipalityNameData3.getValue());
        assertEquals(created, postOfficeBoxAddressMunicipalityNameData3.getCreated());
        assertEquals(changed, postOfficeBoxAddressMunicipalityNameData3.getChanged());
        assertEquals(fetched, postOfficeBoxAddressMunicipalityNameData3.getFetched());
    }

}


