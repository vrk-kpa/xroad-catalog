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

import fi.vrk.xroad.catalog.persistence.entity.OrganizationDescription;
import fi.vrk.xroad.catalog.persistence.repository.OrganizationDescriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class OrganizationDescriptionRepositoryTest {

    @Autowired
    OrganizationDescriptionRepository organizationDescriptionRepository;

    @Test
    public void testFindAnyByOrganizationId() {
        Optional<List<OrganizationDescription>> organizationDescriptions = organizationDescriptionRepository.findAnyByOrganizationId(1L);
        assertEquals(true, organizationDescriptions.isPresent());
        assertEquals(1, organizationDescriptions.get().size());
        assertNotNull(organizationDescriptions.get().get(0).getStatusInfo());
        assertEquals("fi", organizationDescriptions.get().get(0).getLanguage());
        assertEquals("Description", organizationDescriptions.get().get(0).getType());
        assertEquals("Vaasa on yli 67 000 asukkaan voimakkaasti kasvava kaupunki", organizationDescriptions.get().get(0).getValue());
    }

}

