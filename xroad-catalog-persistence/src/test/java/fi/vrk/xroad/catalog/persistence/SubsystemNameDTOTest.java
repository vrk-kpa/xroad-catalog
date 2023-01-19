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

import fi.vrk.xroad.catalog.persistence.dto.SubsystemName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class SubsystemNameDTOTest {

    @Test
    public void testSubsystemNameDTO() {
        String et = "ET";
        String en = "EN";
        SubsystemName subsystemName1 = new SubsystemName();
        subsystemName1.setEt(et);
        subsystemName1.setEn(en);
        SubsystemName subsystemName2 = new SubsystemName(et, en);
        SubsystemName subsystemName3 = SubsystemName.builder().et(et).en(en).build();
        assertEquals(subsystemName1, subsystemName2);
        assertEquals(subsystemName1, subsystemName3);
        assertEquals(subsystemName2, subsystemName3);
        assertEquals(et, subsystemName1.getEt());
        assertEquals(en, subsystemName1.getEn());
        assertNotEquals(0, subsystemName1.hashCode());
        assertEquals(true, subsystemName1.equals(subsystemName2));
        assertEquals(et, subsystemName2.getEt());
        assertEquals(en, subsystemName2.getEn());
        assertNotEquals(0, subsystemName2.hashCode());
        assertEquals(true, subsystemName2.equals(subsystemName3));
        assertEquals(et, subsystemName3.getEt());
        assertEquals(en, subsystemName3.getEn());
        assertNotEquals(0, subsystemName3.hashCode());
        assertEquals(true, subsystemName3.equals(subsystemName1));
    }

}


