/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS) Copyright (c) 2016-2022 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.dto.BusinessAddressData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class BusinessAddressDataDTOTest {

    @Test
    public void testBusinessAddressDataDTO() {
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        LocalDateTime registrationDate = LocalDateTime.now();
        String country = "FI";
        String careOf = "1";
        String street = "street";
        String postCode = "123";
        String city = "Helsinki";
        String language = "Finnish";
        long source = 1;
        long version = 1;
        long type = 1;
        BusinessAddressData businessAddressData1 = new BusinessAddressData();
        businessAddressData1.setCountry(country);
        businessAddressData1.setCareOf(careOf);
        businessAddressData1.setStreet(street);
        businessAddressData1.setPostCode(postCode);
        businessAddressData1.setCity(city);
        businessAddressData1.setLanguage(language);
        businessAddressData1.setSource(source);
        businessAddressData1.setVersion(version);
        businessAddressData1.setType(type);
        businessAddressData1.setCreated(created);
        businessAddressData1.setChanged(changed);
        businessAddressData1.setFetched(fetched);
        businessAddressData1.setRegistrationDate(registrationDate);
        businessAddressData1.setRemoved(null);
        BusinessAddressData businessAddressData2 = new BusinessAddressData(source, version, careOf, street, postCode, city,
                language, type, country, registrationDate, created, changed, fetched, null);
        BusinessAddressData businessAddressData3 = BusinessAddressData.builder().country(country).careOf(careOf).street(street)
                .postCode(postCode).city(city).language(language).source(source).version(version).type(type)
                .created(created).changed(changed).fetched(fetched).registrationDate(registrationDate).removed(null).build();
        assertEquals(businessAddressData1, businessAddressData2);
        assertEquals(businessAddressData1, businessAddressData3);
        assertEquals(businessAddressData2, businessAddressData3);
        assertEquals(country, businessAddressData1.getCountry());
        assertEquals(careOf, businessAddressData1.getCareOf());
        assertEquals(street, businessAddressData1.getStreet());
        assertEquals(postCode, businessAddressData1.getPostCode());
        assertEquals(city, businessAddressData1.getCity());
        assertEquals(language, businessAddressData1.getLanguage());
        assertNotEquals(0, businessAddressData1.hashCode());
        assertEquals(true, businessAddressData1.equals(businessAddressData2));
        assertEquals(country, businessAddressData2.getCountry());
        assertEquals(careOf, businessAddressData2.getCareOf());
        assertEquals(street, businessAddressData2.getStreet());
        assertEquals(postCode, businessAddressData2.getPostCode());
        assertEquals(city, businessAddressData2.getCity());
        assertEquals(language, businessAddressData2.getLanguage());
        assertEquals(country, businessAddressData3.getCountry());
        assertEquals(careOf, businessAddressData3.getCareOf());
        assertEquals(street, businessAddressData3.getStreet());
        assertEquals(postCode, businessAddressData3.getPostCode());
        assertEquals(city, businessAddressData3.getCity());
        assertEquals(language, businessAddressData3.getLanguage());
    }

}


