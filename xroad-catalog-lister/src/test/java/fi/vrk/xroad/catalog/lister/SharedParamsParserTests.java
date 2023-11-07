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
package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.dto.SecurityServerDataList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SharedParamsParserTests {

    @Test
    public void testParseDetails() throws Exception {
        SharedParamsParser sharedParamsParser = new SharedParamsParser();
        SecurityServerDataList list = sharedParamsParser.parseDetails("src/test/resources/shared-params-2.xml");
        assertEquals(4, list.getSecurityServerDataList().size());

        // Security Server 1
        assertEquals("niisss01", list.getSecurityServerDataList().get(0).getServerCode());
        assertEquals("10.0.0.1", list.getSecurityServerDataList().get(0).getAddress());
        assertEquals("NIIS", list.getSecurityServerDataList().get(0).getOwner().getName());
        assertEquals("ORG", list.getSecurityServerDataList().get(0).getOwner().getMemberClass());
        assertEquals("2908758-4", list.getSecurityServerDataList().get(0).getOwner().getMemberCode());
        assertEquals(null, list.getSecurityServerDataList().get(0).getOwner().getSubsystemCode());

        assertEquals(2, list.getSecurityServerDataList().get(0).getClients().size());
        assertEquals("NIIS", list.getSecurityServerDataList().get(0).getClients().get(0).getName());
        assertEquals("ORG", list.getSecurityServerDataList().get(0).getClients().get(0).getMemberClass());
        assertEquals("2908758-4", list.getSecurityServerDataList().get(0).getClients().get(0).getMemberCode());
        assertEquals("Management", list.getSecurityServerDataList().get(0).getClients().get(0).getSubsystemCode());

        assertEquals("NIIS", list.getSecurityServerDataList().get(0).getClients().get(1).getName());
        assertEquals("ORG", list.getSecurityServerDataList().get(0).getClients().get(1).getMemberClass());
        assertEquals("2908758-4", list.getSecurityServerDataList().get(0).getClients().get(1).getMemberCode());
        assertEquals("MonitoringClient", list.getSecurityServerDataList().get(0).getClients().get(1).getSubsystemCode());

        // Security Server 2
        assertEquals("testcomss01", list.getSecurityServerDataList().get(1).getServerCode());
        assertEquals("10.0.0.2", list.getSecurityServerDataList().get(1).getAddress());
        assertEquals("Test Company", list.getSecurityServerDataList().get(1).getOwner().getName());
        assertEquals("COM", list.getSecurityServerDataList().get(1).getOwner().getMemberClass());
        assertEquals("1234567-8", list.getSecurityServerDataList().get(1).getOwner().getMemberCode());
        assertEquals(null, list.getSecurityServerDataList().get(1).getOwner().getSubsystemCode());

        assertEquals(3, list.getSecurityServerDataList().get(1).getClients().size());
        assertEquals("NIIS", list.getSecurityServerDataList().get(1).getClients().get(0).getName());
        assertEquals("ORG", list.getSecurityServerDataList().get(1).getClients().get(0).getMemberClass());
        assertEquals("2908758-4", list.getSecurityServerDataList().get(1).getClients().get(0).getMemberCode());
        assertEquals("MonitoringClient", list.getSecurityServerDataList().get(1).getClients().get(0).getSubsystemCode());

        assertEquals("Test Company", list.getSecurityServerDataList().get(1).getClients().get(1).getName());
        assertEquals("COM", list.getSecurityServerDataList().get(1).getClients().get(1).getMemberClass());
        assertEquals("1234567-8", list.getSecurityServerDataList().get(1).getClients().get(1).getMemberCode());
        assertEquals("TestClient", list.getSecurityServerDataList().get(1).getClients().get(1).getSubsystemCode());

        assertEquals("Test Agency", list.getSecurityServerDataList().get(1).getClients().get(2).getName());
        assertEquals("GOV", list.getSecurityServerDataList().get(1).getClients().get(2).getMemberClass());
        assertEquals("8765432-1", list.getSecurityServerDataList().get(1).getClients().get(2).getMemberCode());
        assertEquals("TestService", list.getSecurityServerDataList().get(1).getClients().get(2).getSubsystemCode());

        // Security Server 3
        assertEquals("testagess01", list.getSecurityServerDataList().get(2).getServerCode());
        assertEquals("10.0.0.3", list.getSecurityServerDataList().get(2).getAddress());
        assertEquals("Test Agency", list.getSecurityServerDataList().get(2).getOwner().getName());
        assertEquals("GOV", list.getSecurityServerDataList().get(2).getOwner().getMemberClass());
        assertEquals("8765432-1", list.getSecurityServerDataList().get(2).getOwner().getMemberCode());
        assertEquals(null, list.getSecurityServerDataList().get(2).getOwner().getSubsystemCode());

        assertEquals(1, list.getSecurityServerDataList().get(2).getClients().size());
        assertEquals("Test Company", list.getSecurityServerDataList().get(2).getClients().get(0).getName());
        assertEquals("COM", list.getSecurityServerDataList().get(2).getClients().get(0).getMemberClass());
        assertEquals("1234567-8", list.getSecurityServerDataList().get(2).getClients().get(0).getMemberCode());
        assertEquals(null, list.getSecurityServerDataList().get(2).getClients().get(0).getSubsystemCode());

        // Security Server 4
        assertEquals("niisss02", list.getSecurityServerDataList().get(3).getServerCode());
        assertEquals("10.0.0.4", list.getSecurityServerDataList().get(3).getAddress());
        assertEquals("NIIS", list.getSecurityServerDataList().get(3).getOwner().getName());
        assertEquals("ORG", list.getSecurityServerDataList().get(3).getOwner().getMemberClass());
        assertEquals("2908758-4", list.getSecurityServerDataList().get(3).getOwner().getMemberCode());
        assertEquals(null, list.getSecurityServerDataList().get(3).getOwner().getSubsystemCode());

        assertEquals(0, list.getSecurityServerDataList().get(3).getClients().size());
    }
}