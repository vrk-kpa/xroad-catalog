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

import fi.vrk.xroad.catalog.persistence.dto.StreetAddressAdditionalInformationData;
import fi.vrk.xroad.catalog.persistence.dto.StreetAddressData;
import fi.vrk.xroad.catalog.persistence.dto.StreetAddressMunicipalityData;
import fi.vrk.xroad.catalog.persistence.dto.StreetAddressPostOfficeData;
import fi.vrk.xroad.catalog.persistence.dto.StreetData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class StreetAddressDataDTOTest {

    @Test
    public void testStreetAddressDataDTO() {
        String streetNumber = "1";
        String postalCode = "123";
        String latitude = "59";
        String longitude = "23";
        String coordinateState = "a1";
        List<StreetData> streets = new ArrayList<>();
        List<StreetAddressPostOfficeData> postOffices = new ArrayList<>();
        List<StreetAddressMunicipalityData> municipalities = new ArrayList<>();
        List<StreetAddressAdditionalInformationData> additionalInformation = new ArrayList<>();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime changed = LocalDateTime.now();
        LocalDateTime fetched = LocalDateTime.now();
        StreetAddressData streetAddressData1 = new StreetAddressData();
        streetAddressData1.setStreetNumber(streetNumber);
        streetAddressData1.setPostalCode(postalCode);
        streetAddressData1.setLatitude(latitude);
        streetAddressData1.setLongitude(longitude);
        streetAddressData1.setCoordinateState(coordinateState);
        streetAddressData1.setStreets(streets);
        streetAddressData1.setPostOffices(postOffices);
        streetAddressData1.setMunicipalities(municipalities);
        streetAddressData1.setAdditionalInformation(additionalInformation);
        streetAddressData1.setCreated(created);
        streetAddressData1.setChanged(changed);
        streetAddressData1.setFetched(fetched);
        streetAddressData1.setRemoved(null);
        StreetAddressData streetAddressData2 = new StreetAddressData(streetNumber, postalCode, latitude, longitude, coordinateState, streets,
                postOffices, municipalities, additionalInformation, created, changed, fetched, null);
        StreetAddressData streetAddressData3 = StreetAddressData.builder().streetNumber(streetNumber).postalCode(postalCode)
                .latitude(latitude).longitude(longitude).coordinateState(coordinateState).streets(streets)
                .postOffices(postOffices).municipalities(municipalities).additionalInformation(additionalInformation)
                .created(created).changed(changed).fetched(fetched).removed(null).build();
        assertEquals(streetAddressData1, streetAddressData2);
        assertEquals(streetAddressData1, streetAddressData3);
        assertEquals(streetAddressData2, streetAddressData3);
        assertEquals(streetNumber, streetAddressData1.getStreetNumber());
        assertEquals(postalCode, streetAddressData1.getPostalCode());
        assertEquals(latitude, streetAddressData1.getLatitude());
        assertEquals(longitude, streetAddressData1.getLongitude());
        assertEquals(coordinateState, streetAddressData1.getCoordinateState());
        assertNotEquals(0, streetAddressData1.hashCode());
        assertEquals(true, streetAddressData1.equals(streetAddressData2));
        assertEquals(streetNumber, streetAddressData2.getStreetNumber());
        assertEquals(postalCode, streetAddressData2.getPostalCode());
        assertEquals(latitude, streetAddressData2.getLatitude());
        assertEquals(longitude, streetAddressData2.getLongitude());
        assertEquals(coordinateState, streetAddressData2.getCoordinateState());
        assertNotEquals(0, streetAddressData2.hashCode());
        assertEquals(true, streetAddressData2.equals(streetAddressData3));
        assertEquals(streetNumber, streetAddressData3.getStreetNumber());
        assertEquals(postalCode, streetAddressData3.getPostalCode());
        assertEquals(latitude, streetAddressData3.getLatitude());
        assertEquals(longitude, streetAddressData3.getLongitude());
        assertEquals(coordinateState, streetAddressData3.getCoordinateState());
        assertNotEquals(0, streetAddressData3.hashCode());
        assertEquals(true, streetAddressData3.equals(streetAddressData1));
    }

}


