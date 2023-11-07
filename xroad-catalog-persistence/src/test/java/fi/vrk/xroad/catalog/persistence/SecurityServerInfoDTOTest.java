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

import fi.vrk.xroad.catalog.persistence.dto.SecurityServerInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest
public class SecurityServerInfoDTOTest {

    @Test
    public void testSecurityServerInfoDTO() {
        String xRoadInstance = "DEV";
        String serverCode = "SS1";
        String address = "https://ss1";
        String memberClass = "GOV";
        String memberCode = "1234";
        SecurityServerInfo securityServerInfo1 = new SecurityServerInfo(xRoadInstance, serverCode, address, memberClass, memberCode);
        SecurityServerInfo securityServerInfo2 = new SecurityServerInfo(xRoadInstance, serverCode, address, memberClass, memberCode);
        assertEquals(xRoadInstance, securityServerInfo1.getXRoadInstance());
        assertEquals(serverCode, securityServerInfo1.getServerCode());
        assertEquals(address, securityServerInfo1.getAddress());
        assertEquals(memberClass, securityServerInfo1.getMemberClass());
        assertEquals(memberCode, securityServerInfo1.getMemberCode());
        assertEquals("SecurityServerInfo(xRoadInstance=DEV, serverCode=SS1, address=https://ss1, memberClass=GOV, memberCode=1234)",
                securityServerInfo1.toString());
        assertNotEquals(0, securityServerInfo1.hashCode());
        assertEquals(true, securityServerInfo1.equals(securityServerInfo2));
        assertNotEquals(0, securityServerInfo2.hashCode());
    }

}


