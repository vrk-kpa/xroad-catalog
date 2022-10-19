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

import fi.vrk.xroad.catalog.persistence.dto.PostOfficeBoxAddressMunicipalityData;
import fi.vrk.xroad.catalog.persistence.dto.PostOfficeBoxAddressMunicipalityNameData;
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


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class PostOfficeBoxAddressMunicipalityDataDTOTest {

    @Test
    public void testPostOfficeBoxAddressMunicipalityDataDTO() {
        String code = "abc-123";
        List<PostOfficeBoxAddressMunicipalityNameData> postOfficeBoxAddressMunicipalityNames = new ArrayList<>();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        PostOfficeBoxAddressMunicipalityData postOfficeBoxAddressMunicipalityData1 = new PostOfficeBoxAddressMunicipalityData();
        postOfficeBoxAddressMunicipalityData1.setCode(code);
        postOfficeBoxAddressMunicipalityData1.setPostOfficeBoxAddressMunicipalityNames(postOfficeBoxAddressMunicipalityNames);
        postOfficeBoxAddressMunicipalityData1.setCreated(created);
        postOfficeBoxAddressMunicipalityData1.setChanged(changed);
        postOfficeBoxAddressMunicipalityData1.setFetched(fetched);
        postOfficeBoxAddressMunicipalityData1.setRemoved(null);
        PostOfficeBoxAddressMunicipalityData postOfficeBoxAddressMunicipalityData2 = new PostOfficeBoxAddressMunicipalityData(code, postOfficeBoxAddressMunicipalityNames,
                created, changed, fetched, null);
        PostOfficeBoxAddressMunicipalityData postOfficeBoxAddressMunicipalityData3 = PostOfficeBoxAddressMunicipalityData.builder().code(code)
                .postOfficeBoxAddressMunicipalityNames(postOfficeBoxAddressMunicipalityNames).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(postOfficeBoxAddressMunicipalityData1, postOfficeBoxAddressMunicipalityData2);
        assertEquals(postOfficeBoxAddressMunicipalityData1, postOfficeBoxAddressMunicipalityData3);
        assertEquals(postOfficeBoxAddressMunicipalityData2, postOfficeBoxAddressMunicipalityData3);
        assertEquals(code, postOfficeBoxAddressMunicipalityData1.getCode());
        assertEquals(postOfficeBoxAddressMunicipalityNames, postOfficeBoxAddressMunicipalityData1.getPostOfficeBoxAddressMunicipalityNames());
        assertEquals(created, postOfficeBoxAddressMunicipalityData1.getCreated());
        assertEquals(changed, postOfficeBoxAddressMunicipalityData1.getChanged());
        assertEquals(fetched, postOfficeBoxAddressMunicipalityData1.getFetched());
        assertEquals(code, postOfficeBoxAddressMunicipalityData2.getCode());
        assertEquals(postOfficeBoxAddressMunicipalityNames, postOfficeBoxAddressMunicipalityData2.getPostOfficeBoxAddressMunicipalityNames());
        assertEquals(created, postOfficeBoxAddressMunicipalityData2.getCreated());
        assertEquals(changed, postOfficeBoxAddressMunicipalityData2.getChanged());
        assertEquals(fetched, postOfficeBoxAddressMunicipalityData2.getFetched());
        assertEquals(code, postOfficeBoxAddressMunicipalityData3.getCode());
        assertEquals(postOfficeBoxAddressMunicipalityNames, postOfficeBoxAddressMunicipalityData3.getPostOfficeBoxAddressMunicipalityNames());
        assertEquals(created, postOfficeBoxAddressMunicipalityData3.getCreated());
        assertEquals(changed, postOfficeBoxAddressMunicipalityData3.getChanged());
        assertEquals(fetched, postOfficeBoxAddressMunicipalityData3.getFetched());

    }

}


