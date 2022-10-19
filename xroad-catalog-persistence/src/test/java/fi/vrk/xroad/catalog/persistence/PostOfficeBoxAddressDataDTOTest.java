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
import fi.vrk.xroad.catalog.persistence.dto.PostOfficeBoxAddressData;
import fi.vrk.xroad.catalog.persistence.dto.PostOfficeBoxAddressMunicipalityData;
import fi.vrk.xroad.catalog.persistence.dto.PostOfficeBoxData;
import fi.vrk.xroad.catalog.persistence.dto.PostOfficeData;
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
import static org.junit.Assert.assertNotEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class PostOfficeBoxAddressDataDTOTest {

    @Test
    public void testPostOfficeBoxAddressDataDTO() {
        String postalCode = "123";
        List<PostOfficeBoxAddressMunicipalityData> postOfficeBoxAddressMunicipalities = new ArrayList<>();
        List<PostOfficeBoxAddressAdditionalInformationData> additionalInformation = new ArrayList<>();
        List<PostOfficeData> postOffices = new ArrayList<>();
        List<PostOfficeBoxData> postOfficeBoxes = new ArrayList<>();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        PostOfficeBoxAddressData postOfficeBoxAddressData1 = new PostOfficeBoxAddressData();
        postOfficeBoxAddressData1.setPostalCode(postalCode);
        postOfficeBoxAddressData1.setPostOfficeBoxAddressMunicipalities(postOfficeBoxAddressMunicipalities);
        postOfficeBoxAddressData1.setAdditionalInformation(additionalInformation);
        postOfficeBoxAddressData1.setPostOffices(postOffices);
        postOfficeBoxAddressData1.setPostOfficeBoxes(postOfficeBoxes);
        postOfficeBoxAddressData1.setCreated(created);
        postOfficeBoxAddressData1.setChanged(changed);
        postOfficeBoxAddressData1.setFetched(fetched);
        postOfficeBoxAddressData1.setRemoved(null);
        PostOfficeBoxAddressData postOfficeBoxAddressData2 = new PostOfficeBoxAddressData(postalCode, postOfficeBoxAddressMunicipalities,
                additionalInformation, postOffices, postOfficeBoxes, created, changed, fetched, null);
        PostOfficeBoxAddressData postOfficeBoxAddressData3 = PostOfficeBoxAddressData.builder().postalCode(postalCode)
                .postOfficeBoxAddressMunicipalities(postOfficeBoxAddressMunicipalities).additionalInformation(additionalInformation)
                .postOffices(postOffices).postOfficeBoxes(postOfficeBoxes).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(postOfficeBoxAddressData1, postOfficeBoxAddressData2);
        assertEquals(postOfficeBoxAddressData1, postOfficeBoxAddressData3);
        assertEquals(postOfficeBoxAddressData2, postOfficeBoxAddressData3);
        assertEquals(postalCode, postOfficeBoxAddressData1.getPostalCode());
        assertEquals(postOfficeBoxAddressMunicipalities, postOfficeBoxAddressData1.getPostOfficeBoxAddressMunicipalities());
        assertEquals(additionalInformation, postOfficeBoxAddressData1.getAdditionalInformation());
        assertEquals(postOffices, postOfficeBoxAddressData1.getPostOffices());
        assertEquals(postOfficeBoxes, postOfficeBoxAddressData1.getPostOfficeBoxes());
        assertNotEquals(0, postOfficeBoxAddressData1.hashCode());
        assertEquals(true, postOfficeBoxAddressData1.equals(postOfficeBoxAddressData2));
        assertEquals(postalCode, postOfficeBoxAddressData2.getPostalCode());
        assertEquals(postOfficeBoxAddressMunicipalities, postOfficeBoxAddressData2.getPostOfficeBoxAddressMunicipalities());
        assertEquals(additionalInformation, postOfficeBoxAddressData2.getAdditionalInformation());
        assertEquals(postOffices, postOfficeBoxAddressData2.getPostOffices());
        assertEquals(postOfficeBoxes, postOfficeBoxAddressData2.getPostOfficeBoxes());
        assertNotEquals(0, postOfficeBoxAddressData2.hashCode());
        assertEquals(true, postOfficeBoxAddressData2.equals(postOfficeBoxAddressData3));
        assertEquals(postalCode, postOfficeBoxAddressData3.getPostalCode());
        assertEquals(postOfficeBoxAddressMunicipalities, postOfficeBoxAddressData3.getPostOfficeBoxAddressMunicipalities());
        assertEquals(additionalInformation, postOfficeBoxAddressData3.getAdditionalInformation());
        assertEquals(postOffices, postOfficeBoxAddressData3.getPostOffices());
        assertEquals(postOfficeBoxes, postOfficeBoxAddressData3.getPostOfficeBoxes());
        assertNotEquals(0, postOfficeBoxAddressData3.hashCode());
        assertEquals(true, postOfficeBoxAddressData3.equals(postOfficeBoxAddressData1));
    }

}


