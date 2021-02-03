/**
 * The MIT License
 * Copyright (c) 2021, Population Register Centre (VRK)
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

import fi.vrk.xroad.catalog.persistence.entity.RegisteredEntry;
import fi.vrk.xroad.catalog.persistence.repository.RegisteredEntryRepository;
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
public class RegisteredEntryRepositoryTest {

    @Autowired
    RegisteredEntryRepository registeredEntryRepository;

    @Test
    public void testFindAnyByCompanyId() {
        Optional<List<RegisteredEntry>> registeredEntries = registeredEntryRepository.findAnyByCompanyId(1L);
        assertEquals(true, registeredEntries.isPresent());
        assertEquals(1, registeredEntries.get().size());
        assertNotNull(registeredEntries.get().get(0).getStatusInfo());
        assertEquals("EN", registeredEntries.get().get(0).getLanguage());
        assertEquals("Unregistered", registeredEntries.get().get(0).getDescription());
        assertEquals(2, registeredEntries.get().get(0).getStatus());
        assertEquals(1, registeredEntries.get().get(0).getRegister());
        assertEquals(2, registeredEntries.get().get(0).getAuthority());
        assertEquals(LocalDate.of(2001, 6, 11), registeredEntries.get().get(0).getRegistrationDate().toLocalDate());
        assertEquals(LocalDate.of(2001, 6, 24), registeredEntries.get().get(0).getEndDate().toLocalDate());
    }



}

