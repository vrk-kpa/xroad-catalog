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

import fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipality;
import fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipalityName;
import fi.vrk.xroad.catalog.persistence.repository.StreetAddressMunicipalityNameRepository;
import fi.vrk.xroad.catalog.persistence.repository.StreetAddressMunicipalityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class StreetAddressMunicipalityNameRepositoryTest {

    @Autowired
    StreetAddressMunicipalityNameRepository streetAddressMunicipalityNameRepository;

    @Autowired
    StreetAddressMunicipalityRepository streetAddressMunicipalityRepository;

    @Test
    public void testFindAnyByStreetAddressMunicipalityId() {
        Optional<List<StreetAddressMunicipalityName>> streetAddressMunicipalities =
                streetAddressMunicipalityNameRepository.findAnyByStreetAddressMunicipalityId(1L);
        assertEquals(true, streetAddressMunicipalities.isPresent());
        assertEquals(1, streetAddressMunicipalities.get().size());
        assertNotNull(streetAddressMunicipalities.get().get(0).getStatusInfo());
        assertEquals("fi", streetAddressMunicipalities.get().get(0).getLanguage());
        assertEquals("Nivala", streetAddressMunicipalities.get().get(0).getValue());
    }

    @Test
    public void testGetAllMunicipalityNames() {
        Optional<List<StreetAddressMunicipalityName>> streetAddressMunicipalities =
                streetAddressMunicipalityNameRepository.findAnyByStreetAddressMunicipalityId(1L);
        assertEquals(true, streetAddressMunicipalities.isPresent());
        assertEquals(1, streetAddressMunicipalities.get().size());
        assertNotNull(streetAddressMunicipalities.get().get(0).getStatusInfo());
        assertEquals("fi", streetAddressMunicipalities.get().get(0).getLanguage());
        assertEquals("Nivala", streetAddressMunicipalities.get().get(0).getValue());
        StreetAddressMunicipality streetAddressMunicipality = streetAddressMunicipalityRepository.findById(1L).get();
        Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipalityName> streetAddressMunicipalityNames
                = streetAddressMunicipality.getAllMunicipalityNames();
        assertEquals(true, streetAddressMunicipalityNames.iterator().hasNext());
    }

}


