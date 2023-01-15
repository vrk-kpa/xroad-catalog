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

import fi.vrk.xroad.catalog.persistence.entity.Address;
import fi.vrk.xroad.catalog.persistence.repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AddressRepositoryTest {

    @Autowired
    AddressRepository addressRepository;

    @Test
    public void testFindAnyByOrganizationId() {

        Optional<List<Address>> addresses = addressRepository.findAnyByOrganizationId(1L);
        assertEquals(true, addresses.isPresent());
        assertEquals(1, addresses.get().size());
        assertNotNull(addresses.get().get(0).getStatusInfo());
        assertEquals("FI", addresses.get().get(0).getCountry());
        assertEquals("Street", addresses.get().get(0).getSubType());
        assertEquals("Postal", addresses.get().get(0).getType());
        assertEquals(1, addresses.get().get(0).getAllStreetAddresses().size());
        assertEquals(1, addresses.get().get(0).getAllPostOfficeBoxAddresses().size());
    }

}


