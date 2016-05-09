/**
 * The MIT License
 * Copyright (c) 2016, Population Register Centre (VRK)
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

import fi.vrk.xroad.catalog.persistence.entity.Service;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@Transactional
@Slf4j
public class ServiceRepositoryTest {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    TestUtil testUtil;

    @Test
    public void testFindByNaturalKey() {
        // member(7) -> ss (8) -> service(6) = ok [dummy-service_7-1-2/v1]
        // member(7) -> ss (8) -> service(8) = removed [removed-service_7-1-3/v1]
        // member(7) -> ss (8) -> service(10) = ok, null service code [service-with-null-version/null]
        // member(7) -> ss (8) -> service(???) = non-existent
        Service service = serviceRepository.findActiveByNaturalKey("dev-cs", "PUB", "15",
                "subsystem_7-1", "dummy-service_7-1-2", "v1");
        assertNotNull(service);
        service = serviceRepository.findActiveByNaturalKey("dev-cs", "PUB", "15",
                "subsystem_7-1", "removed-service_7-1-3", "v1");
        assertNull(service);
        service = serviceRepository.findActiveByNaturalKey("dev-cs", "PUB", "15",
                "subsystem_7-1", "N/A-service", "v1");
        assertNull(service);
        service = serviceRepository.findActiveNullVersionByNaturalKey("dev-cs", "PUB", "15",
                "subsystem_7-1", "service-with-null-version");
        assertNotNull(service);
    }
}