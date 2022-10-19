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

import fi.vrk.xroad.catalog.persistence.dto.LastCollectionData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class LastCollectionDataDTOTest {

    @Test
    public void testLastCollectionDataDTO() {
        LocalDateTime organizationsLastFetched = LocalDateTime.now();
        LocalDateTime companiesLastFetched = LocalDateTime.now();
        LocalDateTime membersLastFetched = LocalDateTime.now();
        LocalDateTime subsystemsLastFetched = LocalDateTime.now();
        LocalDateTime servicesLastFetched = LocalDateTime.now();
        LocalDateTime wsdlsLastFetched = LocalDateTime.now();
        LocalDateTime openapisLastFetched = LocalDateTime.now();
        LastCollectionData lastCollectionData1 = new LastCollectionData();
        lastCollectionData1.setOrganizationsLastFetched(organizationsLastFetched);
        lastCollectionData1.setCompaniesLastFetched(companiesLastFetched);
        lastCollectionData1.setMembersLastFetched(membersLastFetched);
        lastCollectionData1.setSubsystemsLastFetched(subsystemsLastFetched);
        lastCollectionData1.setServicesLastFetched(servicesLastFetched);
        lastCollectionData1.setWsdlsLastFetched(wsdlsLastFetched);
        lastCollectionData1.setOpenapisLastFetched(openapisLastFetched);
        LastCollectionData lastCollectionData2 = new LastCollectionData(organizationsLastFetched, companiesLastFetched, membersLastFetched,
                subsystemsLastFetched, servicesLastFetched, wsdlsLastFetched, openapisLastFetched);
        LastCollectionData lastCollectionData3 = LastCollectionData.builder().organizationsLastFetched(organizationsLastFetched)
                .companiesLastFetched(companiesLastFetched).membersLastFetched(membersLastFetched)
                .subsystemsLastFetched(subsystemsLastFetched).servicesLastFetched(servicesLastFetched)
                .wsdlsLastFetched(wsdlsLastFetched).openapisLastFetched(openapisLastFetched).build();
        assertEquals(lastCollectionData1, lastCollectionData2);
        assertEquals(lastCollectionData1, lastCollectionData3);
        assertEquals(lastCollectionData2, lastCollectionData3);
        assertEquals(organizationsLastFetched, lastCollectionData1.getOrganizationsLastFetched());
        assertEquals(companiesLastFetched, lastCollectionData1.getCompaniesLastFetched());
        assertEquals(membersLastFetched, lastCollectionData1.getMembersLastFetched());
        assertEquals(subsystemsLastFetched, lastCollectionData1.getSubsystemsLastFetched());
        assertEquals(servicesLastFetched, lastCollectionData1.getServicesLastFetched());
        assertEquals(wsdlsLastFetched, lastCollectionData1.getWsdlsLastFetched());
        assertEquals(openapisLastFetched, lastCollectionData1.getOpenapisLastFetched());
        assertEquals(organizationsLastFetched, lastCollectionData2.getOrganizationsLastFetched());
        assertEquals(companiesLastFetched, lastCollectionData2.getCompaniesLastFetched());
        assertEquals(membersLastFetched, lastCollectionData2.getMembersLastFetched());
        assertEquals(subsystemsLastFetched, lastCollectionData2.getSubsystemsLastFetched());
        assertEquals(servicesLastFetched, lastCollectionData2.getServicesLastFetched());
        assertEquals(wsdlsLastFetched, lastCollectionData2.getWsdlsLastFetched());
        assertEquals(openapisLastFetched, lastCollectionData2.getOpenapisLastFetched());
        assertEquals(organizationsLastFetched, lastCollectionData3.getOrganizationsLastFetched());
        assertEquals(companiesLastFetched, lastCollectionData3.getCompaniesLastFetched());
        assertEquals(membersLastFetched, lastCollectionData3.getMembersLastFetched());
        assertEquals(subsystemsLastFetched, lastCollectionData3.getSubsystemsLastFetched());
        assertEquals(servicesLastFetched, lastCollectionData3.getServicesLastFetched());
        assertEquals(wsdlsLastFetched, lastCollectionData3.getWsdlsLastFetched());
        assertEquals(openapisLastFetched, lastCollectionData3.getOpenapisLastFetched());
    }

}


