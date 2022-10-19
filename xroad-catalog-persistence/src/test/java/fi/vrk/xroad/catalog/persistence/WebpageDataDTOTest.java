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

import fi.vrk.xroad.catalog.persistence.dto.WebpageData;
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
public class WebpageDataDTOTest {

    @Test
    public void testWebpageDataDTO() {
        String language = "FI";
        String url = "http://www.abc";
        String value = "value";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        WebpageData webpageData1 = new WebpageData();
        webpageData1.setLanguage(language);
        webpageData1.setUrl(url);
        webpageData1.setValue(value);
        webpageData1.setCreated(created);
        webpageData1.setChanged(changed);
        webpageData1.setFetched(fetched);
        webpageData1.setRemoved(null);
        WebpageData webpageData2 = new WebpageData(language, url, value, created, changed, fetched, null);
        WebpageData webpageData3 = WebpageData.builder().language(language).url(url).value(value)
                .created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(webpageData1, webpageData2);
        assertEquals(webpageData1, webpageData3);
        assertEquals(webpageData2, webpageData3);
        assertEquals(language, webpageData1.getLanguage());
        assertEquals(url, webpageData1.getUrl());
        assertEquals(value, webpageData1.getValue());
        assertEquals(created, webpageData1.getCreated());
        assertTrue(webpageData1.equals(webpageData2));
        assertTrue(webpageData1.hashCode() != 0);
        assertEquals(language, webpageData2.getLanguage());
        assertEquals(url, webpageData2.getUrl());
        assertEquals(value, webpageData2.getValue());
        assertEquals(changed, webpageData2.getChanged());
        assertTrue(webpageData2.equals(webpageData3));
        assertTrue(webpageData2.hashCode() != 0);
        assertEquals(language, webpageData3.getLanguage());
        assertEquals(url, webpageData3.getUrl());
        assertEquals(value, webpageData3.getValue());
        assertEquals(fetched, webpageData3.getFetched());
        assertTrue(webpageData3.equals(webpageData1));
        assertTrue(webpageData3.hashCode() != 0);
    }

}


