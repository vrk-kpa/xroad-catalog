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

import fi.vrk.xroad.catalog.persistence.entity.Organization;
import fi.vrk.xroad.catalog.persistence.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class OrganizationRepositoryTest {

    @Autowired
    OrganizationRepository organizationRepository;

    @Test
    public void testFindAllByBusinessCode() {
        Set<Organization> organizations = organizationRepository.findAllByBusinessCode("0123456-9");
        assertEquals(1, organizations.size());
        assertNotNull(organizations.iterator().next().getStatusInfo());
        assertEquals("0123456-9", organizations.iterator().next().getBusinessCode());
        assertEquals("abcdef123456", organizations.iterator().next().getGuid());
        assertEquals("Municipality", organizations.iterator().next().getOrganizationType());
        assertEquals("Published", organizations.iterator().next().getPublishingStatus());
        assertEquals(1, organizations.iterator().next().getAllOrganizationNames().size());
        assertEquals(1, organizations.iterator().next().getAllOrganizationDescriptions().size());
        assertEquals(1, organizations.iterator().next().getAllEmails().size());
        assertEquals(1, organizations.iterator().next().getAllPhoneNumbers().size());
        assertEquals(1, organizations.iterator().next().getAllWebPages().size());
        assertEquals(1, organizations.iterator().next().getAllAddresses().size());
        assertEquals(1, organizations.iterator().next().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        assertEquals(1, organizations.iterator().next().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().size());
    }

    @Test
    public void findAnyByOrganizationGuid() {
        Optional<Organization> organization = organizationRepository.findAnyByOrganizationGuid("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertNotNull(organization.get().getStatusInfo());
        assertEquals("0123456-9", organization.get().getBusinessCode());
        assertEquals("abcdef123456", organization.get().getGuid());
        assertEquals("Municipality", organization.get().getOrganizationType());
        assertEquals("Published", organization.get().getPublishingStatus());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        assertEquals(1, organization.get().getAllOrganizationDescriptions().size());
        assertEquals(1, organization.get().getAllEmails().size());
        assertEquals(1, organization.get().getAllPhoneNumbers().size());
        assertEquals(1, organization.get().getAllWebPages().size());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().size());
    }

    @Test
    public void testFindLatestFetched() {
        LocalDateTime latestFetched = organizationRepository.findLatestFetched();
        assertEquals(2016, latestFetched.getYear());
        assertEquals(Month.JANUARY, latestFetched.getMonth());
        assertEquals(1, latestFetched.getDayOfMonth());
        assertEquals(0, latestFetched.getHour());
    }

}
