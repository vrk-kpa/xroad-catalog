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

import fi.vrk.xroad.catalog.persistence.dto.HeartbeatResponse;
import fi.vrk.xroad.catalog.persistence.dto.LastCollectionData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class HeartbeatResponseDTOTest {

    @Test
    public void testHeartbeatResponseDTO() {
        Boolean appWorking = true;
        Boolean dbWorking = true;
        String appName = "X-Road Catalog";
        String appVersion = "1.0";
        LocalDateTime systemTime = LocalDateTime.now();
        LastCollectionData lastCollectionData = LastCollectionData.builder().build();
        HeartbeatResponse heartbeatResponse1 = new HeartbeatResponse();
        heartbeatResponse1.setAppWorking(appWorking);
        heartbeatResponse1.setDbWorking(dbWorking);
        heartbeatResponse1.setAppName(appName);
        heartbeatResponse1.setAppVersion(appVersion);
        heartbeatResponse1.setSystemTime(systemTime);
        heartbeatResponse1.setLastCollectionData(lastCollectionData);
        HeartbeatResponse heartbeatResponse2 = new HeartbeatResponse(appWorking, dbWorking, appName, appVersion, systemTime, lastCollectionData);
        HeartbeatResponse heartbeatResponse3 = HeartbeatResponse.builder().appWorking(appWorking).dbWorking(dbWorking)
                .appName(appName).appVersion(appVersion).systemTime(systemTime).lastCollectionData(lastCollectionData).build();
        assertEquals(heartbeatResponse1, heartbeatResponse2);
        assertEquals(heartbeatResponse1, heartbeatResponse3);
        assertEquals(heartbeatResponse2, heartbeatResponse3);
        assertEquals(appWorking, heartbeatResponse1.getAppWorking());
        assertEquals(dbWorking, heartbeatResponse1.getDbWorking());
        assertEquals(appName, heartbeatResponse1.getAppName());
        assertEquals(appVersion, heartbeatResponse1.getAppVersion());
        assertEquals(systemTime, heartbeatResponse1.getSystemTime());
        assertNotEquals(0, heartbeatResponse1.hashCode());
        assertEquals(true, heartbeatResponse1.equals(heartbeatResponse2));
        assertEquals(appWorking, heartbeatResponse2.getAppWorking());
        assertEquals(dbWorking, heartbeatResponse2.getDbWorking());
        assertEquals(appName, heartbeatResponse2.getAppName());
        assertEquals(appVersion, heartbeatResponse2.getAppVersion());
        assertEquals(systemTime, heartbeatResponse2.getSystemTime());
        assertNotEquals(0, heartbeatResponse2.hashCode());
        assertEquals(true, heartbeatResponse2.equals(heartbeatResponse3));
        assertEquals(appWorking, heartbeatResponse3.getAppWorking());
        assertEquals(dbWorking, heartbeatResponse3.getDbWorking());
        assertEquals(appName, heartbeatResponse3.getAppName());
        assertEquals(appVersion, heartbeatResponse3.getAppVersion());
        assertEquals(systemTime, heartbeatResponse3.getSystemTime());
        assertNotEquals(0, heartbeatResponse3.hashCode());
        assertEquals(true, heartbeatResponse3.equals(heartbeatResponse1));
    }

}


