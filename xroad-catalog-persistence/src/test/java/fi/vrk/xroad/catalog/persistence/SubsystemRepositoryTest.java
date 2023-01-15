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

import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.repository.SubsystemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SubsystemRepositoryTest {

    @Autowired
    SubsystemRepository subsystemRepository;

    @Autowired
    TestUtil testUtil;

    @Test
    public void testFindByNaturalKey() {
        // 1->12 (subsystem_a3_removed) = removed, 1->1 (subsystem_a1) = ok, foo = not existing
        Subsystem subsystem = subsystemRepository.findActiveByNaturalKey("dev-cs", "PUB", "14151328",
                "subsystem_a1");
        assertNotNull(subsystem);
        subsystem = subsystemRepository.findActiveByNaturalKey("dev-cs", "PUB", "14151328",
                "subsystem_a3_removed");
        assertNull(subsystem);
        subsystem = subsystemRepository.findActiveByNaturalKey("dev-cs", "PUB", "14151328",
                "N/A code");
        assertNull(subsystem);
    }

    @Test
    public void testFindLatestFetched() {
        LocalDateTime latestFetched = subsystemRepository.findLatestFetched();
        assertEquals(2017, latestFetched.getYear());
        assertEquals(Month.JANUARY, latestFetched.getMonth());
        assertEquals(2, latestFetched.getDayOfMonth());
        assertEquals(0, latestFetched.getHour());
    }
}
