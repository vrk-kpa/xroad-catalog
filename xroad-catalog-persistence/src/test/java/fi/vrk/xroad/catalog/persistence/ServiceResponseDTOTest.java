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

import fi.vrk.xroad.catalog.persistence.dto.ServiceEndpointsResponse;
import fi.vrk.xroad.catalog.persistence.dto.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class ServiceResponseDTOTest {

    @Test
    public void testServiceResponseDTO() {
        List<ServiceEndpointsResponse> listOfServices = new ArrayList<>();
        ServiceResponse serviceResponse1 = new ServiceResponse();
        serviceResponse1.setListOfServices(listOfServices);
        ServiceResponse serviceResponse2 = new ServiceResponse(listOfServices);
        ServiceResponse serviceResponse3 = ServiceResponse.builder().listOfServices(listOfServices).build();
        assertEquals(serviceResponse1, serviceResponse2);
        assertEquals(serviceResponse1, serviceResponse3);
        assertEquals(serviceResponse2, serviceResponse3);
        assertEquals(listOfServices, serviceResponse1.getListOfServices());
        assertNotEquals(0, serviceResponse1.hashCode());
        assertEquals(true, serviceResponse1.equals(serviceResponse2));
        assertEquals(listOfServices, serviceResponse2.getListOfServices());
        assertNotEquals(0, serviceResponse2.hashCode());
        assertEquals(true, serviceResponse2.equals(serviceResponse3));
        assertEquals(listOfServices, serviceResponse3.getListOfServices());
        assertNotEquals(0, serviceResponse3.hashCode());
        assertEquals(true, serviceResponse3.equals(serviceResponse1));
    }

}


