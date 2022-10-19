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

import fi.vrk.xroad.catalog.persistence.dto.PostOfficeBoxData;
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
public class PostOfficeBoxDataDTOTest {

    @Test
    public void testPostOfficeBoxDataDTO() {
        String language = "FI";
        String value = "value";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        PostOfficeBoxData postOfficeBoxData1 = new PostOfficeBoxData();
        postOfficeBoxData1.setLanguage(language);
        postOfficeBoxData1.setValue(value);
        postOfficeBoxData1.setCreated(created);
        postOfficeBoxData1.setChanged(changed);
        postOfficeBoxData1.setFetched(fetched);
        postOfficeBoxData1.setRemoved(null);
        PostOfficeBoxData postOfficeBoxData2 = new PostOfficeBoxData(language, value, created, changed, fetched, null);
        PostOfficeBoxData postOfficeBoxData3 = PostOfficeBoxData.builder().language(language).value(value).created(created)
                .changed(changed).fetched(fetched).removed(null).build();
        assertEquals(postOfficeBoxData1, postOfficeBoxData2);
        assertEquals(postOfficeBoxData1, postOfficeBoxData3);
        assertEquals(postOfficeBoxData2, postOfficeBoxData3);
        assertEquals(language, postOfficeBoxData1.getLanguage());
        assertEquals(value, postOfficeBoxData1.getValue());
        assertEquals(created, postOfficeBoxData1.getCreated());
        assertEquals(changed, postOfficeBoxData1.getChanged());
        assertEquals(fetched, postOfficeBoxData1.getFetched());
        assertNotEquals(0, postOfficeBoxData1.hashCode());
        assertEquals(true, postOfficeBoxData1.equals(postOfficeBoxData2));
        assertEquals(language, postOfficeBoxData2.getLanguage());
        assertEquals(value, postOfficeBoxData2.getValue());
        assertEquals(created, postOfficeBoxData2.getCreated());
        assertEquals(changed, postOfficeBoxData2.getChanged());
        assertEquals(fetched, postOfficeBoxData2.getFetched());
        assertNotEquals(0, postOfficeBoxData2.hashCode());
        assertEquals(true, postOfficeBoxData2.equals(postOfficeBoxData3));
        assertEquals(language, postOfficeBoxData3.getLanguage());
        assertEquals(value, postOfficeBoxData3.getValue());
        assertEquals(created, postOfficeBoxData3.getCreated());
        assertEquals(changed, postOfficeBoxData3.getChanged());
        assertEquals(fetched, postOfficeBoxData3.getFetched());
        assertNotEquals(0, postOfficeBoxData3.hashCode());
        assertEquals(true, postOfficeBoxData3.equals(postOfficeBoxData1));
    }

}


