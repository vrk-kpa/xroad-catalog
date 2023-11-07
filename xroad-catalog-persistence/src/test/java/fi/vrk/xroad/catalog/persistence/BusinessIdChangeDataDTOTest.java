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

import fi.vrk.xroad.catalog.persistence.dto.BusinessIdChangeData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class BusinessIdChangeDataDTOTest {

    @Test
    public void testBusinessIdChangeDataDTO() {
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        long source = 1;
        String description = "description";
        String reason = "reason";
        String changeDate = LocalDateTime.now().toString();
        String change = "change";
        String oldBusinessId = "oldBusinessId";
        String newBusinessId = "newBusinessId";
        String language = "language";
        BusinessIdChangeData businessIdChangeData1 = new BusinessIdChangeData();
        businessIdChangeData1.setSource(source);
        businessIdChangeData1.setDescription(description);
        businessIdChangeData1.setReason(reason);
        businessIdChangeData1.setChangeDate(changeDate);
        businessIdChangeData1.setChange(change);
        businessIdChangeData1.setOldBusinessId(oldBusinessId);
        businessIdChangeData1.setNewBusinessId(newBusinessId);
        businessIdChangeData1.setLanguage(language);
        businessIdChangeData1.setCreated(created);
        businessIdChangeData1.setChanged(changed);
        businessIdChangeData1.setFetched(fetched);
        businessIdChangeData1.setRemoved(null);
        BusinessIdChangeData businessIdChangeData2 = new BusinessIdChangeData(source, description, reason, changeDate, change, oldBusinessId,
                newBusinessId, language, created, changed, fetched, null);
        BusinessIdChangeData businessIdChangeData3 = BusinessIdChangeData.builder().source(source).description(description).reason(reason)
                .changeDate(changeDate).change(change).oldBusinessId(oldBusinessId).newBusinessId(newBusinessId).language(language)
                .created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(businessIdChangeData1, businessIdChangeData2);
        assertEquals(businessIdChangeData1, businessIdChangeData3);
        assertEquals(businessIdChangeData2, businessIdChangeData3);
        assertEquals(description, businessIdChangeData1.getDescription());
        assertEquals(reason, businessIdChangeData1.getReason());
        assertEquals(changeDate, businessIdChangeData1.getChangeDate());
        assertEquals(change, businessIdChangeData1.getChange());
        assertEquals(oldBusinessId, businessIdChangeData1.getOldBusinessId());
        assertEquals(newBusinessId, businessIdChangeData1.getNewBusinessId());
        assertEquals(language, businessIdChangeData1.getLanguage());
        assertEquals(description, businessIdChangeData2.getDescription());
        assertEquals(reason, businessIdChangeData2.getReason());
        assertEquals(changeDate, businessIdChangeData2.getChangeDate());
        assertEquals(change, businessIdChangeData2.getChange());
        assertEquals(oldBusinessId, businessIdChangeData2.getOldBusinessId());
        assertEquals(newBusinessId, businessIdChangeData2.getNewBusinessId());
        assertEquals(language, businessIdChangeData2.getLanguage());
        assertEquals(description, businessIdChangeData3.getDescription());
        assertEquals(reason, businessIdChangeData3.getReason());
        assertEquals(changeDate, businessIdChangeData3.getChangeDate());
        assertEquals(change, businessIdChangeData3.getChange());
        assertEquals(language, businessIdChangeData3.getLanguage());
        assertEquals(true, businessIdChangeData3.equals(businessIdChangeData2));
        assertNotEquals(0, businessIdChangeData3.hashCode());
    }

}


