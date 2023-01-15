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

import fi.vrk.xroad.catalog.persistence.dto.ErrorLogResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class ErrorLogResponseDTOTest {

    @Test
    public void testErrorLogResponseDTO() {
        Integer pageNumber = 1;
        Integer pageSize = 10;
        Integer numberOfPages = 2;
        ErrorLogResponse errorLogResponse1 = new ErrorLogResponse();
        errorLogResponse1.setPageNumber(pageNumber);
        errorLogResponse1.setPageSize(pageSize);
        errorLogResponse1.setNumberOfPages(numberOfPages);
        errorLogResponse1.setErrorLogList(new ArrayList<>());
        ErrorLogResponse errorLogResponse2 = new ErrorLogResponse(pageNumber, pageSize, numberOfPages, new ArrayList<>());
        ErrorLogResponse errorLogResponse3 = ErrorLogResponse.builder().pageNumber(pageNumber).pageSize(pageSize)
                .numberOfPages(numberOfPages).errorLogList(new ArrayList<>()).build();
        assertEquals(errorLogResponse1, errorLogResponse2);
        assertEquals(errorLogResponse1, errorLogResponse3);
        assertEquals(errorLogResponse2, errorLogResponse3);
        assertEquals(pageNumber, errorLogResponse1.getPageNumber());
        assertEquals(pageSize, errorLogResponse1.getPageSize());
        assertEquals(numberOfPages, errorLogResponse1.getNumberOfPages());
        assertEquals(0, errorLogResponse1.getErrorLogList().size());
        assertNotEquals(0, errorLogResponse1.hashCode());
        assertEquals(true, errorLogResponse1.equals(errorLogResponse2));
        assertEquals(pageNumber, errorLogResponse2.getPageNumber());
        assertEquals(pageSize, errorLogResponse2.getPageSize());
        assertEquals(numberOfPages, errorLogResponse2.getNumberOfPages());
        assertEquals(0, errorLogResponse2.getErrorLogList().size());
        assertNotEquals(0, errorLogResponse2.hashCode());
        assertEquals(true, errorLogResponse2.equals(errorLogResponse3));
        assertEquals(pageNumber, errorLogResponse3.getPageNumber());
        assertEquals(pageSize, errorLogResponse3.getPageSize());
        assertEquals(numberOfPages, errorLogResponse3.getNumberOfPages());
        assertEquals(0, errorLogResponse3.getErrorLogList().size());
        assertNotEquals(0, errorLogResponse3.hashCode());
        assertEquals(true, errorLogResponse3.equals(errorLogResponse1));
    }

}


