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

import fi.vrk.xroad.catalog.persistence.entity.StreetAddress;
import fi.vrk.xroad.catalog.persistence.repository.StreetAddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class StreetAddressRepositoryTest {

    @Autowired
    StreetAddressRepository streetAddressRepository;

    @Test
    public void testFindByAddressId() {
        StreetAddress streetAddress = streetAddressRepository.findByAddressId(1L);
        assertNotNull(streetAddress);
        assertNotNull(streetAddress.getStatusInfo());
        assertEquals("2", streetAddress.getStreetNumber());
        assertEquals("Ok", streetAddress.getCoordinateState());
        assertEquals("64200", streetAddress.getPostalCode());
        assertEquals("6939589.246", streetAddress.getLatitude());
        assertEquals("208229.722", streetAddress.getLongitude());
        assertEquals(1, streetAddress.getAllStreets().size());
        assertEquals(1, streetAddress.getAllPostOffices().size());
        assertEquals(1, streetAddress.getAllAdditionalInformation().size());
        assertEquals(1, streetAddress.getAllMunicipalities().size());
    }

}


