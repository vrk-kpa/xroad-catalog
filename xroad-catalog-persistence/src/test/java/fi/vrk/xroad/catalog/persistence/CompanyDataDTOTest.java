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

import fi.vrk.xroad.catalog.persistence.dto.CompanyData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class CompanyDataDTOTest {

    @Test
    public void testCompanyDataDTO() {
        String businessCode = "12345-0";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        String companyForm = "OY";
        String detailsUri = "abcdef123";
        String name = "name";
        LocalDateTime registrationDate = LocalDateTime.now();
        CompanyData companyData1 = new CompanyData();
        companyData1.setBusinessCode(businessCode);
        companyData1.setCreated(created);
        companyData1.setChanged(changed);
        companyData1.setFetched(fetched);
        companyData1.setRemoved(null);
        companyData1.setCompanyForm(companyForm);
        companyData1.setDetailsUri(detailsUri);
        companyData1.setName(name);
        companyData1.setRegistrationDate(registrationDate);
        companyData1.setBusinessAddresses(new ArrayList<>());
        companyData1.setBusinessAuxiliaryNames(new ArrayList<>());
        companyData1.setBusinessIdChanges(new ArrayList<>());
        companyData1.setBusinessLines(new ArrayList<>());
        companyData1.setBusinessNames(new ArrayList<>());
        companyData1.setCompanyForms(new ArrayList<>());
        companyData1.setContactDetails(new ArrayList<>());
        companyData1.setLanguages(new ArrayList<>());
        companyData1.setLiquidations(new ArrayList<>());
        companyData1.setRegisteredEntries(new ArrayList<>());
        companyData1.setRegisteredOffices(new ArrayList<>());
        CompanyData companyData2 = new CompanyData(businessCode, created, changed, fetched, null, companyForm, detailsUri, name, registrationDate,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        CompanyData companyData3 = CompanyData.builder().businessCode(businessCode).created(created).changed(changed).fetched(fetched)
                .removed(null).companyForm(companyForm).detailsUri(detailsUri).name(name).registrationDate(registrationDate)
                .businessAddresses(new ArrayList<>()).businessAuxiliaryNames(new ArrayList<>()).businessIdChanges(new ArrayList<>())
                .businessLines(new ArrayList<>()).businessNames(new ArrayList<>()).companyForms(new ArrayList<>())
                .contactDetails(new ArrayList<>()).languages(new ArrayList<>()).liquidations(new ArrayList<>())
                .registeredEntries(new ArrayList<>()).registeredOffices(new ArrayList<>()).build();
        assertEquals(companyData1, companyData2);
        assertEquals(companyData1, companyData3);
        assertEquals(companyData2, companyData3);
        assertEquals(businessCode, companyData1.getBusinessCode());
        assertEquals(companyForm, companyData1.getCompanyForm());
        assertEquals(detailsUri, companyData1.getDetailsUri());
        assertEquals(name, companyData1.getName());
        assertEquals(businessCode, companyData2.getBusinessCode());
        assertEquals(companyForm, companyData2.getCompanyForm());
        assertEquals(detailsUri, companyData2.getDetailsUri());
        assertEquals(name, companyData2.getName());
        assertEquals(businessCode, companyData3.getBusinessCode());
        assertEquals(companyForm, companyData3.getCompanyForm());
        assertEquals(detailsUri, companyData3.getDetailsUri());
        assertEquals(name, companyData3.getName());
    }

}


