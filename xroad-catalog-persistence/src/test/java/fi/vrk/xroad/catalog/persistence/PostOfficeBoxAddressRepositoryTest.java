/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS) Copyright (c) 2016-2022 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddress;
import fi.vrk.xroad.catalog.persistence.repository.PostOfficeBoxAddressRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PostOfficeBoxAddressRepositoryTest {

    @Autowired
    PostOfficeBoxAddressRepository postOfficeBoxAddressRepository;

    @Test
    public void testFindAnyByAddressId() {
        PostOfficeBoxAddress postOfficeBoxAddress = postOfficeBoxAddressRepository.findByAddressId(1L);
        assertNotNull(postOfficeBoxAddress);
        assertNotNull(postOfficeBoxAddress.getStatusInfo());
        assertEquals("64200", postOfficeBoxAddress.getPostalCode());
        assertEquals(1, postOfficeBoxAddress.getAllPostOffices().size());
        assertEquals(1, postOfficeBoxAddress.getAllPostOfficeBoxes().size());
        assertEquals(1, postOfficeBoxAddress.getAllAdditionalInformation().size());
        assertEquals(1, postOfficeBoxAddress.getAllMunicipalities().size());
    }

}


