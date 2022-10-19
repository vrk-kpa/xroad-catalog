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

import fi.vrk.xroad.catalog.persistence.dto.ChangedValue;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class ChangedValueDTOTest {

    @Test
    public void testChangedValueDTO() {
        String name = "name";
        ChangedValue changedValue1 = new ChangedValue();
        changedValue1.setName(name);
        ChangedValue changedValue2 = new ChangedValue(name);
        ChangedValue changedValue3 = ChangedValue.builder().name(name).build();
        assertEquals(changedValue1, changedValue2);
        assertEquals(changedValue1, changedValue3);
        assertEquals(changedValue2, changedValue3);
        assertEquals(name, changedValue1.getName());
        assertNotEquals(0, changedValue1.hashCode());
        assertEquals(true, changedValue1.equals(changedValue2));
        assertEquals(name, changedValue2.getName());
        assertNotEquals(0, changedValue2.hashCode());
        assertEquals(true, changedValue2.equals(changedValue3));
        assertEquals(name, changedValue3.getName());
        assertNotEquals(0, changedValue3.hashCode());
        assertEquals(true, changedValue3.equals(changedValue1));
    }

}


