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

import fi.vrk.xroad.catalog.persistence.entity.BusinessAuxiliaryName;
import fi.vrk.xroad.catalog.persistence.repository.BusinessAuxiliaryNameRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class BusinessAuxiliaryNameRepositoryTest {

    @Autowired
    BusinessAuxiliaryNameRepository businessAuxiliaryNameRepository;

    @Test
    public void testFindAnyByCompanyId() {
        Optional<List<BusinessAuxiliaryName>> businessAuxiliaryNames = businessAuxiliaryNameRepository.findAnyByCompanyId(1L);
        assertEquals(true, businessAuxiliaryNames.isPresent());
        assertEquals(1, businessAuxiliaryNames.get().size());
        assertNotNull(businessAuxiliaryNames.get().get(0).getStatusInfo());
        assertEquals("", businessAuxiliaryNames.get().get(0).getLanguage());
        assertEquals("Solinor", businessAuxiliaryNames.get().get(0).getName());
        assertEquals(1, businessAuxiliaryNames.get().get(0).getSource());
        assertEquals(5, businessAuxiliaryNames.get().get(0).getOrdering());
        assertEquals(1, businessAuxiliaryNames.get().get(0).getVersion());
        assertEquals(LocalDate.of(2019, 1, 31), businessAuxiliaryNames.get().get(0).getRegistrationDate().toLocalDate());
        assertNull(businessAuxiliaryNames.get().get(0).getEndDate());
    }



}

