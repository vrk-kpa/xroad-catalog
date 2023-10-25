/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2016-2023 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.dto.AddressData;
import fi.vrk.xroad.catalog.persistence.dto.EmailData;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationData;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationDescriptionData;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationNameData;
import fi.vrk.xroad.catalog.persistence.dto.PhoneNumberData;
import fi.vrk.xroad.catalog.persistence.dto.WebpageData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class OrganizationDataDTOTest {

    @Test
    public void testOrganizationDataDTO() {
        String businessCode = "12345-0";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        String organizationType = "company";
        String publishingStatus = "status";
        String guid = "123456789";
        List<OrganizationNameData> organizationNames = new ArrayList<>();
        List<OrganizationDescriptionData> organizationDescriptions = new ArrayList<>();
        List<EmailData> emails = new ArrayList<>();
        List<PhoneNumberData> phoneNumbers = new ArrayList<>();
        List<WebpageData> webPages = new ArrayList<>();
        List<AddressData> addresses = new ArrayList<>();
        OrganizationData organizationData1 = new OrganizationData();
        organizationData1.setBusinessCode(businessCode);
        organizationData1.setCreated(created);
        organizationData1.setChanged(changed);
        organizationData1.setFetched(fetched);
        organizationData1.setRemoved(null);
        organizationData1.setOrganizationType(organizationType);
        organizationData1.setPublishingStatus(publishingStatus);
        organizationData1.setGuid(guid);
        organizationData1.setOrganizationNames(organizationNames);
        organizationData1.setOrganizationDescriptions(organizationDescriptions);
        organizationData1.setEmails(emails);
        organizationData1.setPhoneNumbers(phoneNumbers);
        organizationData1.setWebPages(webPages);
        organizationData1.setAddresses(addresses);
        OrganizationData organizationData2 = new OrganizationData(businessCode, created, changed, fetched, null, organizationType, publishingStatus,
                guid, organizationNames, organizationDescriptions, emails, phoneNumbers, webPages, addresses);
        OrganizationData organizationData3 = OrganizationData.builder().businessCode(businessCode).created(created).changed(changed)
                .fetched(fetched).removed(null).organizationType(organizationType).publishingStatus(publishingStatus).guid(guid)
                .organizationNames(organizationNames).organizationDescriptions(organizationDescriptions)
                .emails(emails).phoneNumbers(phoneNumbers).webPages(webPages).addresses(addresses).build();
        assertEquals(organizationData1, organizationData2);
        assertEquals(organizationData1, organizationData3);
        assertEquals(organizationData2, organizationData3);
        assertEquals(businessCode, organizationData1.getBusinessCode());
        assertEquals(created, organizationData1.getCreated());
        assertEquals(changed, organizationData1.getChanged());
        assertEquals(fetched, organizationData1.getFetched());
        assertEquals(organizationType, organizationData1.getOrganizationType());
        assertEquals(publishingStatus, organizationData1.getPublishingStatus());
        assertEquals(guid, organizationData1.getGuid());
        assertEquals(businessCode, organizationData2.getBusinessCode());
        assertEquals(created, organizationData2.getCreated());
        assertEquals(changed, organizationData2.getChanged());
        assertEquals(fetched, organizationData2.getFetched());
        assertEquals(organizationType, organizationData2.getOrganizationType());
        assertEquals(publishingStatus, organizationData2.getPublishingStatus());
        assertEquals(guid, organizationData2.getGuid());
        assertEquals(businessCode, organizationData3.getBusinessCode());
        assertEquals(created, organizationData3.getCreated());
        assertEquals(changed, organizationData3.getChanged());
        assertEquals(fetched, organizationData3.getFetched());
        assertEquals(organizationType, organizationData3.getOrganizationType());
        assertEquals(publishingStatus, organizationData3.getPublishingStatus());
        assertEquals(true, organizationData3.equals(organizationData1));
        assertNotEquals(0, organizationData3.hashCode());
    }

}


