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

import fi.vrk.xroad.catalog.persistence.dto.Email;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest
public class EmailDTOTest {

    @Test
    public void testEmailDTO() {
        String name = "name";
        String email = "email";
        Email email1 = new Email();
        email1.setName(name);
        email1.setEmail(email);
        Email email2 = new Email(name, email);
        Email email3 = Email.builder().name(name).email(email).build();
        assertEquals(email1, email2);
        assertEquals(email1, email3);
        assertEquals(email2, email3);
        assertEquals(name, email1.getName());
        assertEquals(email, email1.getEmail());
        assertNotEquals(0, email1.hashCode());
        assertEquals(true, email1.equals(email2));
        assertEquals(name, email2.getName());
        assertEquals(email, email2.getEmail());
        assertNotEquals(0, email2.hashCode());
        assertEquals(true, email2.equals(email3));
        assertEquals(name, email3.getName());
        assertEquals(email, email3.getEmail());
        assertNotEquals(0, email3.hashCode());
        assertEquals(true, email3.equals(email1));
    }

}


