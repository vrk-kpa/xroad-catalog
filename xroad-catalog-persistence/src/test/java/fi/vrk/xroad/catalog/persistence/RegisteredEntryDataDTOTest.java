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

import fi.vrk.xroad.catalog.persistence.dto.RegisteredEntryData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class RegisteredEntryDataDTOTest {

    @Test
    public void testRegisteredEntryDataDTO() {
        String description = "abc";
        long status = 1;
        long register = 2;
        String language = "FI";
        long authority = 3;
        LocalDateTime registrationDate = LocalDateTime.now();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        RegisteredEntryData registeredEntryData1 = new RegisteredEntryData();
        registeredEntryData1.setDescription(description);
        registeredEntryData1.setStatus(status);
        registeredEntryData1.setRegister(register);
        registeredEntryData1.setLanguage(language);
        registeredEntryData1.setAuthority(authority);
        registeredEntryData1.setRegistrationDate(registrationDate);
        registeredEntryData1.setEndDate(null);
        registeredEntryData1.setCreated(created);
        registeredEntryData1.setChanged(changed);
        registeredEntryData1.setFetched(fetched);
        registeredEntryData1.setRemoved(null);
        RegisteredEntryData registeredEntryData2 = new RegisteredEntryData(description, status, register, language, authority, registrationDate,
                null, created, changed, fetched, null);
        RegisteredEntryData registeredEntryData3 = RegisteredEntryData.builder().description(description).status(status).register(register).language(language)
                .authority(authority).registrationDate(registrationDate).endDate(null).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(registeredEntryData1, registeredEntryData2);
        assertEquals(registeredEntryData1, registeredEntryData3);
        assertEquals(registeredEntryData2, registeredEntryData3);
        assertEquals(description, registeredEntryData1.getDescription());
        assertEquals(status, registeredEntryData1.getStatus());
        assertEquals(register, registeredEntryData1.getRegister());
        assertEquals(language, registeredEntryData1.getLanguage());
        assertEquals(authority, registeredEntryData1.getAuthority());
        assertNotEquals(0, registeredEntryData1.hashCode());
        assertEquals(true, registeredEntryData1.equals(registeredEntryData2));
        assertEquals(description, registeredEntryData2.getDescription());
        assertEquals(status, registeredEntryData2.getStatus());
        assertEquals(register, registeredEntryData2.getRegister());
        assertEquals(language, registeredEntryData2.getLanguage());
        assertEquals(authority, registeredEntryData2.getAuthority());
        assertNotEquals(0, registeredEntryData2.hashCode());
        assertEquals(true, registeredEntryData2.equals(registeredEntryData3));
        assertEquals(description, registeredEntryData3.getDescription());
        assertEquals(status, registeredEntryData3.getStatus());
        assertEquals(register, registeredEntryData3.getRegister());
        assertEquals(language, registeredEntryData3.getLanguage());
        assertEquals(authority, registeredEntryData3.getAuthority());
        assertNotEquals(0, registeredEntryData3.hashCode());
        assertEquals(true, registeredEntryData3.equals(registeredEntryData1));
    }

}


