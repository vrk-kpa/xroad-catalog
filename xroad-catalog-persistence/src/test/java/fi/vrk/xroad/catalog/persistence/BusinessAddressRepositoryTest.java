/**
 * The MIT License
 * Copyright (c) 2020, Population Register Centre (VRK)
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

import fi.vrk.xroad.catalog.persistence.entity.BusinessAddress;
import fi.vrk.xroad.catalog.persistence.repository.BusinessAddressRepository;
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
public class BusinessAddressRepositoryTest {

    @Autowired
    BusinessAddressRepository businessAddressRepository;

    @Test
    public void testFindAnyByCompanyId() {
        Optional<List<BusinessAddress>> businessAddresses = businessAddressRepository.findAnyByCompanyId(1L);
        assertEquals(true, businessAddresses.isPresent());
        assertEquals(1, businessAddresses.get().size());
        assertNotNull(businessAddresses.get().get(0).getStatusInfo());
        assertEquals("FI", businessAddresses.get().get(0).getLanguage());
        assertEquals("", businessAddresses.get().get(0).getCareOf());
        assertEquals("TAMPERE", businessAddresses.get().get(0).getCity());
        assertEquals("", businessAddresses.get().get(0).getCountry());
        assertEquals("33100", businessAddresses.get().get(0).getPostCode());
        assertEquals("Kalevantie 2", businessAddresses.get().get(0).getStreet());
        assertEquals(0, businessAddresses.get().get(0).getSource());
        assertEquals(1, businessAddresses.get().get(0).getVersion());
        assertEquals(2, businessAddresses.get().get(0).getType());
        assertEquals(LocalDate.of(2016, 7, 12), businessAddresses.get().get(0).getRegistrationDate().toLocalDate());
        assertNull(businessAddresses.get().get(0).getEndDate());
    }



}

