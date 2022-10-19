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

import fi.vrk.xroad.catalog.persistence.dto.CompanyFormData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
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
        assertEquals(name, companyFormData2.getName());
        assertEquals(language, companyFormData2.getLanguage());
        assertEquals(name, companyFormData3.getName());
        assertEquals(language, companyFormData3.getLanguage());
    }

}


