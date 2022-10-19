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

import fi.vrk.xroad.catalog.persistence.dto.DistinctServiceStatisticsResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class DistinctServiceStatisticsResponseDTOTest {

    @Test
    public void testDistinctServiceStatisticsResponseDTO() {
        DistinctServiceStatisticsResponse distinctServiceStatisticsResponse1 = new DistinctServiceStatisticsResponse();
        distinctServiceStatisticsResponse1.setDistinctServiceStatisticsList(new ArrayList<>());
        DistinctServiceStatisticsResponse distinctServiceStatisticsResponse2 = new DistinctServiceStatisticsResponse(new ArrayList<>());
        DistinctServiceStatisticsResponse distinctServiceStatisticsResponse3 = DistinctServiceStatisticsResponse.builder()
                .distinctServiceStatisticsList(new ArrayList<>()).build();
        assertEquals(distinctServiceStatisticsResponse1, distinctServiceStatisticsResponse2);
        assertEquals(distinctServiceStatisticsResponse1, distinctServiceStatisticsResponse3);
        assertEquals(distinctServiceStatisticsResponse2, distinctServiceStatisticsResponse3);
        assertNotEquals(0, distinctServiceStatisticsResponse1.hashCode());
        assertEquals(true, distinctServiceStatisticsResponse1.equals(distinctServiceStatisticsResponse2));
        assertNotEquals(0, distinctServiceStatisticsResponse2.hashCode());
        assertEquals(true, distinctServiceStatisticsResponse2.equals(distinctServiceStatisticsResponse3));
        assertNotEquals(0, distinctServiceStatisticsResponse3.hashCode());
        assertEquals(true, distinctServiceStatisticsResponse3.equals(distinctServiceStatisticsResponse1));
    }

}


