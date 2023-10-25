/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2016-2023 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.dto.RegisteredOfficeData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest
public class RegisteredOfficeDataDTOTest {

    @Test
    public void testRegisteredOfficeDataDTO() {
        long source = 1;
        long ordering = 1;
        long version = 1;
        String name = "name";
        String language = "FI";
        LocalDateTime registrationDate = LocalDateTime.now();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        RegisteredOfficeData registeredOfficeData1 = new RegisteredOfficeData();
        registeredOfficeData1.setSource(source);
        registeredOfficeData1.setOrdering(ordering);
        registeredOfficeData1.setVersion(version);
        registeredOfficeData1.setName(name);
        registeredOfficeData1.setLanguage(language);
        registeredOfficeData1.setRegistrationDate(registrationDate);
        registeredOfficeData1.setEndDate(null);
        registeredOfficeData1.setCreated(created);
        registeredOfficeData1.setChanged(changed);
        registeredOfficeData1.setFetched(fetched);
        registeredOfficeData1.setRemoved(null);
        RegisteredOfficeData registeredOfficeData2 = new RegisteredOfficeData(source, ordering, version, name, language, registrationDate,
                null, created, changed, fetched, null);
        RegisteredOfficeData registeredOfficeData3 = RegisteredOfficeData.builder().source(source).ordering(ordering).version(version)
                .name(name).language(language).registrationDate(registrationDate).endDate(null)
                .created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(registeredOfficeData1, registeredOfficeData2);
        assertEquals(registeredOfficeData1, registeredOfficeData3);
        assertEquals(registeredOfficeData2, registeredOfficeData3);
        assertEquals(source, registeredOfficeData1.getSource());
        assertEquals(ordering, registeredOfficeData1.getOrdering());
        assertEquals(version, registeredOfficeData1.getVersion());
        assertEquals(name, registeredOfficeData1.getName());
        assertEquals(language, registeredOfficeData1.getLanguage());
        assertNotEquals(0, registeredOfficeData1.hashCode());
        assertEquals(true, registeredOfficeData1.equals(registeredOfficeData2));
        assertEquals(source, registeredOfficeData2.getSource());
        assertEquals(ordering, registeredOfficeData2.getOrdering());
        assertEquals(version, registeredOfficeData2.getVersion());
        assertEquals(name, registeredOfficeData2.getName());
        assertEquals(language, registeredOfficeData2.getLanguage());
        assertNotEquals(0, registeredOfficeData2.hashCode());
        assertEquals(true, registeredOfficeData2.equals(registeredOfficeData3));
        assertEquals(source, registeredOfficeData3.getSource());
        assertEquals(ordering, registeredOfficeData3.getOrdering());
        assertEquals(version, registeredOfficeData3.getVersion());
        assertEquals(name, registeredOfficeData3.getName());
        assertEquals(language, registeredOfficeData3.getLanguage());
        assertNotEquals(0, registeredOfficeData3.hashCode());
        assertEquals(true, registeredOfficeData3.equals(registeredOfficeData1));
    }

}


