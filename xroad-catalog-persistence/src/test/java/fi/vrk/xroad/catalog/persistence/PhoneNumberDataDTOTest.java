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

import fi.vrk.xroad.catalog.persistence.dto.PhoneNumberData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class PhoneNumberDataDTOTest {

    @Test
    public void testPhoneNumberDataDTO() {
        String language = "FI";
        String additionalInformation = "additionalInfo";
        String serviceChargeType = "fixed";
        String chargeDescription = "charge";
        String prefixNumber = "1";
        String number = "123456";
        boolean isFinnishServiceNumber = true;
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        PhoneNumberData phoneNumberData1 = new PhoneNumberData();
        phoneNumberData1.setLanguage(language);
        phoneNumberData1.setAdditionalInformation(additionalInformation);
        phoneNumberData1.setServiceChargeType(serviceChargeType);
        phoneNumberData1.setChargeDescription(chargeDescription);
        phoneNumberData1.setPrefixNumber(prefixNumber);
        phoneNumberData1.setNumber(number);
        phoneNumberData1.setFinnishServiceNumber(isFinnishServiceNumber);
        phoneNumberData1.setCreated(created);
        phoneNumberData1.setChanged(changed);
        phoneNumberData1.setFetched(fetched);
        phoneNumberData1.setRemoved(null);
        PhoneNumberData phoneNumberData2 = new PhoneNumberData(language, additionalInformation, serviceChargeType, chargeDescription,
                prefixNumber, number, isFinnishServiceNumber, created, changed, fetched, null);
        PhoneNumberData phoneNumberData3 = PhoneNumberData.builder().language(language).additionalInformation(additionalInformation)
                .serviceChargeType(serviceChargeType).chargeDescription(chargeDescription).prefixNumber(prefixNumber).number(number)
                .isFinnishServiceNumber(isFinnishServiceNumber).created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(phoneNumberData1, phoneNumberData2);
        assertEquals(phoneNumberData1, phoneNumberData3);
        assertEquals(phoneNumberData2, phoneNumberData3);
        assertEquals(language, phoneNumberData1.getLanguage());
        assertEquals(additionalInformation, phoneNumberData1.getAdditionalInformation());
        assertEquals(serviceChargeType, phoneNumberData1.getServiceChargeType());
        assertEquals(chargeDescription, phoneNumberData1.getChargeDescription());
        assertEquals(prefixNumber, phoneNumberData1.getPrefixNumber());
        assertEquals(number, phoneNumberData1.getNumber());
        assertEquals(isFinnishServiceNumber, phoneNumberData1.isFinnishServiceNumber());
        assertEquals(language, phoneNumberData2.getLanguage());
        assertEquals(additionalInformation, phoneNumberData2.getAdditionalInformation());
        assertEquals(serviceChargeType, phoneNumberData2.getServiceChargeType());
        assertEquals(chargeDescription, phoneNumberData2.getChargeDescription());
        assertEquals(prefixNumber, phoneNumberData2.getPrefixNumber());
        assertEquals(number, phoneNumberData2.getNumber());
        assertEquals(isFinnishServiceNumber, phoneNumberData2.isFinnishServiceNumber());
        assertEquals(language, phoneNumberData3.getLanguage());
        assertEquals(additionalInformation, phoneNumberData3.getAdditionalInformation());
        assertEquals(true, phoneNumberData1.equals(phoneNumberData2));
        assertNotEquals(0, phoneNumberData1.hashCode());
        assertEquals(true, phoneNumberData2.equals(phoneNumberData3));
        assertNotEquals(0, phoneNumberData2.hashCode());
        assertEquals(true, phoneNumberData3.equals(phoneNumberData1));
        assertNotEquals(0, phoneNumberData3.hashCode());
    }

}


