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

import fi.vrk.xroad.catalog.persistence.dto.AddressData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class AddressDataDTOTest {

    @Test
    public void testAddressDataDTO() {
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        String type = "StreetAddress";
        String country = "FI";
        String subType = "street";
        AddressData addressData1 = new AddressData();
        addressData1.setType(type);
        addressData1.setCountry(country);
        addressData1.setSubType(subType);
        addressData1.setCreated(created);
        addressData1.setChanged(changed);
        addressData1.setFetched(fetched);
        addressData1.setRemoved(null);
        addressData1.setStreetAddressData(new ArrayList<>());
        addressData1.setPostOfficeBoxAddressData(new ArrayList<>());
        AddressData addressData2 = new AddressData(country, type, subType, new ArrayList<>(), new ArrayList<>(),
                created, changed, fetched, null);
        AddressData addressData3 = AddressData.builder().country(country).type(type).subType(subType)
                .streetAddressData(new ArrayList<>()).postOfficeBoxAddressData(new ArrayList<>())
                .created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(addressData1, addressData2);
        assertEquals(addressData1, addressData3);
        assertEquals(addressData2, addressData3);
        assertEquals(type, addressData1.getType());
        assertEquals(country, addressData1.getCountry());
        assertEquals(subType, addressData1.getSubType());
        assertNotEquals(0, addressData1.hashCode());
        assertEquals(true, addressData1.equals(addressData2));
        assertEquals(type, addressData2.getType());
        assertEquals(country, addressData2.getCountry());
        assertEquals(subType, addressData2.getSubType());
        assertNotEquals(0, addressData2.hashCode());
        assertEquals(true, addressData2.equals(addressData3));
        assertEquals(type, addressData3.getType());
        assertEquals(country, addressData3.getCountry());
        assertEquals(subType, addressData3.getSubType());
        assertNotEquals(0, addressData3.hashCode());
        assertEquals(true, addressData3.equals(addressData1));
    }

}


