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

import fi.vrk.xroad.catalog.persistence.entity.BusinessIdChange;
import fi.vrk.xroad.catalog.persistence.repository.BusinessIdChangeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BusinessIdChangeRepositoryTest {

    @Autowired
    BusinessIdChangeRepository businessIdChangeRepository;

    @Test
    public void testFindAnyByCompanyId() {
        Optional<List<BusinessIdChange>> businessIdChanges = businessIdChangeRepository.findAnyByCompanyId(1L);
        assertEquals(true, businessIdChanges.isPresent());
        assertEquals(1, businessIdChanges.get().size());
        assertNotNull(businessIdChanges.get().get(0).getStatusInfo());
        assertEquals("", businessIdChanges.get().get(0).getLanguage());
        assertEquals("", businessIdChanges.get().get(0).getDescription());
        assertEquals(2, businessIdChanges.get().get(0).getSource());
        assertEquals("", businessIdChanges.get().get(0).getReason());
        assertEquals("44", businessIdChanges.get().get(0).getChange());
        assertEquals("1796717-0", businessIdChanges.get().get(0).getOldBusinessId());
        assertEquals("1710128-9", businessIdChanges.get().get(0).getNewBusinessId());
        assertEquals("2019-02-01", businessIdChanges.get().get(0).getChangeDate());
    }



}

