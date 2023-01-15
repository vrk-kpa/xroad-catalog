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

import fi.vrk.xroad.catalog.persistence.dto.CompanyFormData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class CompanyFormDataDTOTest {

    @Test
    public void testCompanyFormDataDTO() {
        long source = 1;
        long version = 1;
        String name = "name";
        String language = "FI";
        long type = 1;
        LocalDateTime registrationDate = LocalDateTime.now();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        CompanyFormData companyFormData1 = new CompanyFormData();
        companyFormData1.setSource(source);
        companyFormData1.setVersion(version);
        companyFormData1.setName(name);
        companyFormData1.setLanguage(language);
        companyFormData1.setType(type);
        companyFormData1.setRegistrationDate(registrationDate);
        companyFormData1.setEndDate(null);
        companyFormData1.setCreated(created);
        companyFormData1.setChanged(changed);
        companyFormData1.setFetched(fetched);
        companyFormData1.setRemoved(null);
        CompanyFormData companyFormData2 = new CompanyFormData(source, version, name, language, type, registrationDate, null,
                created, changed, fetched, null);
        CompanyFormData companyFormData3 = CompanyFormData.builder().source(source).version(version).name(name).language(language)
                .type(type).registrationDate(registrationDate).endDate(null).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(companyFormData1, companyFormData2);
        assertEquals(companyFormData1, companyFormData3);
        assertEquals(companyFormData2, companyFormData3);
        assertEquals(name, companyFormData1.getName());
        assertEquals(language, companyFormData1.getLanguage());
        assertNotEquals(0, companyFormData1.hashCode());
        assertEquals(true, companyFormData1.equals(companyFormData2));
        assertEquals(name, companyFormData2.getName());
        assertEquals(language, companyFormData2.getLanguage());
        assertNotEquals(0, companyFormData2.hashCode());
        assertEquals(true, companyFormData2.equals(companyFormData3));
        assertEquals(name, companyFormData3.getName());
        assertEquals(language, companyFormData3.getLanguage());
        assertNotEquals(0, companyFormData3.hashCode());
        assertEquals(true, companyFormData3.equals(companyFormData1));
    }

}


