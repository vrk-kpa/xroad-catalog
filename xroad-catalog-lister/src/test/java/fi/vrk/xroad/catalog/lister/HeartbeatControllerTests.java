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
package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.CatalogService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ListerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"xroad-catalog.app-name=X-Road Catalog Lister","xroad-catalog.app-version=1.0.3"})
public class HeartbeatControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    CatalogService catalogService;

    @Test
    public void testGetHeartbeat() throws JSONException {
        given(catalogService.checkDatabaseConnection()).willReturn(Boolean.TRUE);
        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/heartbeat", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        assertTrue(json.getBoolean("appWorking"));
        assertTrue(json.getBoolean("dbWorking"));
        assertEquals("X-Road Catalog Lister", json.getString("appName"));
        assertEquals("1.0.3", json.getString("appVersion"));
    }

    @Test
    public void testGetHeartbeatDbDown() throws JSONException {
        given(catalogService.checkDatabaseConnection()).willReturn(Boolean.FALSE);

        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/heartbeat", String.class);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        JSONObject json = new JSONObject(response.getBody());
        assertTrue(json.getBoolean("appWorking"));
        assertFalse(json.getBoolean("dbWorking"));
        assertEquals("X-Road Catalog Lister", json.getString("appName"));
        assertEquals("1.0.3", json.getString("appVersion"));
    }

}

