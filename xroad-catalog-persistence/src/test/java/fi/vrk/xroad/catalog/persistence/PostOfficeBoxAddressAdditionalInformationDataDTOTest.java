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

import fi.vrk.xroad.catalog.persistence.dto.PostOfficeBoxAddressAdditionalInformationData;
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
public class PostOfficeBoxAddressAdditionalInformationDataDTOTest {

    @Test
    public void testPostOfficeBoxAddressAdditionalInformationDataDTO() {
        String language = "FI";
        String value = "value";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        PostOfficeBoxAddressAdditionalInformationData postOfficeBoxAddressAdditionalInformationData1 = new PostOfficeBoxAddressAdditionalInformationData();
        postOfficeBoxAddressAdditionalInformationData1.setLanguage(language);
        postOfficeBoxAddressAdditionalInformationData1.setValue(value);
        postOfficeBoxAddressAdditionalInformationData1.setCreated(created);
        postOfficeBoxAddressAdditionalInformationData1.setChanged(changed);
        postOfficeBoxAddressAdditionalInformationData1.setFetched(fetched);
        postOfficeBoxAddressAdditionalInformationData1.setRemoved(null);
        PostOfficeBoxAddressAdditionalInformationData postOfficeBoxAddressAdditionalInformationData2
                = new PostOfficeBoxAddressAdditionalInformationData(language, value, created, changed, fetched, null);
        PostOfficeBoxAddressAdditionalInformationData postOfficeBoxAddressAdditionalInformationData3 = PostOfficeBoxAddressAdditionalInformationData.builder()
                .language(language).value(value).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(postOfficeBoxAddressAdditionalInformationData1, postOfficeBoxAddressAdditionalInformationData2);
        assertEquals(postOfficeBoxAddressAdditionalInformationData1, postOfficeBoxAddressAdditionalInformationData3);
        assertEquals(postOfficeBoxAddressAdditionalInformationData2, postOfficeBoxAddressAdditionalInformationData3);
        assertEquals(language, postOfficeBoxAddressAdditionalInformationData1.getLanguage());
        assertEquals(value, postOfficeBoxAddressAdditionalInformationData1.getValue());
        assertEquals(created, postOfficeBoxAddressAdditionalInformationData1.getCreated());
        assertEquals(changed, postOfficeBoxAddressAdditionalInformationData1.getChanged());
        assertEquals(fetched, postOfficeBoxAddressAdditionalInformationData1.getFetched());
        assertNotEquals(0, postOfficeBoxAddressAdditionalInformationData1.hashCode());
        assertEquals(true, postOfficeBoxAddressAdditionalInformationData1.equals(postOfficeBoxAddressAdditionalInformationData2));
        assertEquals(language, postOfficeBoxAddressAdditionalInformationData2.getLanguage());
        assertEquals(value, postOfficeBoxAddressAdditionalInformationData2.getValue());
        assertEquals(created, postOfficeBoxAddressAdditionalInformationData2.getCreated());
        assertEquals(changed, postOfficeBoxAddressAdditionalInformationData2.getChanged());
        assertEquals(fetched, postOfficeBoxAddressAdditionalInformationData2.getFetched());
        assertNotEquals(0, postOfficeBoxAddressAdditionalInformationData2.hashCode());
        assertEquals(true, postOfficeBoxAddressAdditionalInformationData2.equals(postOfficeBoxAddressAdditionalInformationData3));
        assertEquals(language, postOfficeBoxAddressAdditionalInformationData3.getLanguage());
        assertEquals(value, postOfficeBoxAddressAdditionalInformationData3.getValue());
        assertEquals(created, postOfficeBoxAddressAdditionalInformationData3.getCreated());
        assertEquals(changed, postOfficeBoxAddressAdditionalInformationData3.getChanged());
        assertEquals(fetched, postOfficeBoxAddressAdditionalInformationData3.getFetched());
        assertNotEquals(0, postOfficeBoxAddressAdditionalInformationData3.hashCode());
        assertEquals(true, postOfficeBoxAddressAdditionalInformationData3.equals(postOfficeBoxAddressAdditionalInformationData1));
    }

}


