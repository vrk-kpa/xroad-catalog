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

import fi.vrk.xroad.catalog.persistence.entity.ErrorLog;
import fi.vrk.xroad.catalog.persistence.repository.ErrorLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class ErrorLogRepositoryTest {

    @Autowired
    ErrorLogRepository errorLogRepository;

    @Test
    public void testFindAnySince() {
        LocalDateTime changedAfter = LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0);
        Set<ErrorLog> errorLogEntries = errorLogRepository.findAny(changedAfter);
        assertEquals(2, errorLogEntries.size());
        assertEquals("Service not found", errorLogEntries.iterator().next().getMessage());
    }

    @Test
    public void testFindAnyByClientParameters() {
        LocalDateTime changedAfter = LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0);
        Page<ErrorLog> errorLogEntries = errorLogRepository.findAnyByClientParameters(changedAfter,
                "DEV",
                "GOV",
                "1234",
                "TestSubsystem",
                new PageRequest(0, 100));
        assertEquals(1, errorLogEntries.getTotalPages());
        assertEquals("Service not found", errorLogEntries.getContent().get(0).getMessage());
    }

    @Test
    public void testFindAnyByOrganizationParameters() {
        LocalDateTime changedAfter = LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0);
        Page<ErrorLog> errorLogEntries = errorLogRepository.findAnyByOrganization(changedAfter,
                "DEV",
                "GOV",
                "1234",
                new PageRequest(0, 100));
        assertEquals(1, errorLogEntries.getTotalPages());
        assertEquals("Service not found", errorLogEntries.getContent().get(0).getMessage());
    }


}

