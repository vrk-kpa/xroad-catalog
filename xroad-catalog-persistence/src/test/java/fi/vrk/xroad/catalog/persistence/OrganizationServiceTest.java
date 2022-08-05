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

import fi.vrk.xroad.catalog.persistence.entity.*;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class OrganizationServiceTest {

    @Autowired
    OrganizationService organizationService;

    @Test
    public void testGetOrganizations() {
        Iterable<Organization> organizations = organizationService.getOrganizations("0123456-9");
        assertEquals(1, Iterables.size(organizations));
        assertEquals(1, organizations.iterator().next().getAllOrganizationNames().size());
        assertEquals(1, organizations.iterator().next().getAllOrganizationDescriptions().size());
        assertEquals(1, organizations.iterator().next().getAllEmails().size());
        assertEquals(1, organizations.iterator().next().getAllPhoneNumbers().size());
        assertEquals(1, organizations.iterator().next().getAllWebPages().size());
        assertEquals(1, organizations.iterator().next().getAllAddresses().size());
        assertEquals(1, organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllStreetAddresses().size());
        assertEquals(1, organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().size());
        assertEquals("0123456-9", organizations.iterator().next().getBusinessCode());
        assertEquals("abcdef123456", organizations.iterator().next().getGuid());
        assertEquals("Published", organizations.iterator().next().getPublishingStatus());
        assertEquals("Municipality", organizations.iterator().next().getOrganizationType());
        assertEquals("Vaasan kaupunki", organizations.iterator().next().getAllOrganizationNames().iterator().next().getValue());
        assertEquals("Vaasa on yli 67 000 asukkaan voimakkaasti kasvava kaupunki",
                organizations.iterator().next().getAllOrganizationDescriptions().iterator().next().getValue());
        assertEquals("vaasa@vaasa.fi", organizations.iterator().next().getAllEmails().iterator().next().getValue());
        assertEquals("62249111", organizations.iterator().next().getAllPhoneNumbers().iterator().next().getNumber());
        assertEquals("https://www.vaasa.fi/", organizations.iterator().next().getAllWebPages().iterator().next().getUrl());
        assertEquals("Street", organizations.iterator().next().getAllAddresses().iterator().next().getSubType());
        assertEquals("64200", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllStreetAddresses().iterator().next().getPostalCode());
        assertEquals("Motellikuja", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllStreetAddresses().iterator().next().getAllStreets().iterator().next().getValue());
        assertEquals("64200", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getPostalCode());
        assertEquals("NIVALA", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllPostOffices().iterator().next().getValue());
        assertEquals("NIVALA", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllPostOfficeBoxes().iterator().next().getValue());
        assertEquals("Kaupungintalo/kaupunginjohtaja", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllAdditionalInformation().iterator().next().getValue());
        assertEquals("545", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllMunicipalities().iterator().next().getCode());
        assertEquals("Nivala", organizations.iterator().next().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllMunicipalities().iterator().next()
                .getAllMunicipalityNames().iterator().next().getValue());
    }

    @Test
    public void testGetOrganization() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        assertEquals(1, organization.get().getAllOrganizationDescriptions().size());
        assertEquals(1, organization.get().getAllEmails().size());
        assertEquals(1, organization.get().getAllPhoneNumbers().size());
        assertEquals(1, organization.get().getAllWebPages().size());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next()
                .getAllStreetAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().size());
        assertEquals("0123456-9", organization.get().getBusinessCode());
        assertEquals("abcdef123456", organization.get().getGuid());
        assertEquals("Published", organization.get().getPublishingStatus());
        assertEquals("Municipality", organization.get().getOrganizationType());
        assertEquals("Vaasan kaupunki", organization.get().getAllOrganizationNames().iterator().next().getValue());
        assertEquals("Vaasa on yli 67 000 asukkaan voimakkaasti kasvava kaupunki",
                organization.get().getAllOrganizationDescriptions().iterator().next().getValue());
        assertEquals("vaasa@vaasa.fi", organization.get().getAllEmails().iterator().next().getValue());
        assertEquals("62249111", organization.get().getAllPhoneNumbers().iterator().next().getNumber());
        assertEquals("https://www.vaasa.fi/", organization.get().getAllWebPages().iterator().next().getUrl());
        assertEquals("Street", organization.get().getAllAddresses().iterator().next().getSubType());
        assertEquals("64200", organization.get().getAllAddresses().iterator().next()
                .getAllStreetAddresses().iterator().next().getPostalCode());
        assertEquals("Motellikuja", organization.get().getAllAddresses().iterator().next()
                .getAllStreetAddresses().iterator().next().getAllStreets().iterator().next().getValue());
        assertEquals("64200", organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getPostalCode());
        assertEquals("NIVALA", organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllPostOffices().iterator().next().getValue());
        assertEquals("NIVALA", organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllPostOfficeBoxes().iterator().next().getValue());
        assertEquals("Kaupungintalo/kaupunginjohtaja", organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllAdditionalInformation().iterator().next().getValue());
        assertEquals("545", organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllMunicipalities().iterator().next().getCode());
        assertEquals("Nivala", organization.get().getAllAddresses().iterator().next()
                .getAllPostOfficeBoxAddresses().iterator().next().getAllMunicipalities().iterator().next()
                .getAllMunicipalityNames().iterator().next().getValue());
    }

    @Test
    public void testSaveOrganization() {
        Organization organization = Organization.builder()
                .organizationType("Municipality")
                .businessCode("123456789-0")
                .guid("abcdef123456789")
                .publishingStatus("Published").build();
        Organization savedOrganization = organizationService.saveOrganization(organization);
        assertNotNull(savedOrganization);
        assertNotNull(savedOrganization.getId());
        assertEquals("abcdef123456789", savedOrganization.getGuid());
        assertEquals("123456789-0", savedOrganization.getBusinessCode());
        TestUtil.verifySavedStatusInfo(savedOrganization.getStatusInfo());
    }

    @Test
    public void testSaveOrganizationWhenAlreadyExists() {
        Organization organization = Organization.builder()
                .organizationType("Municipality")
                .businessCode("0123456-9")
                .guid("abcdef123456")
                .publishingStatus("Published").build();
        Organization savedOrganization = organizationService.saveOrganization(organization);
        assertNotNull(savedOrganization);
        assertNotNull(savedOrganization.getId());
        assertEquals("abcdef123456", savedOrganization.getGuid());
        assertEquals("0123456-9", savedOrganization.getBusinessCode());
        TestUtil.verifySavedStatusInfo(savedOrganization.getStatusInfo());
    }

    @Test
    public void testSaveOrganizationName() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        OrganizationName organizationName = OrganizationName.builder()
                .language("fi").type("Name").value("Vaasa").organization(organization.get()).build();
        organizationService.saveOrganizationName(organizationName);
        Optional<Organization> foundOrganization = organizationService.getOrganization("abcdef123456");
        assertEquals(1, foundOrganization.get().getAllOrganizationNames().size());
        assertEquals("fi", foundOrganization.get().getAllOrganizationNames().iterator().next().getLanguage());
        assertEquals("Vaasa", foundOrganization.get().getAllOrganizationNames().iterator().next().getValue());
        TestUtil.verifySavedStatusInfo(foundOrganization.get().getAllOrganizationNames().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveOrganizationDescription() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        OrganizationDescription organizationDescription = OrganizationDescription.builder()
                .language("fi").type("Description").value("Vaasa").organization(organization.get()).build();
        organizationService.saveOrganizationDescription(organizationDescription);
        Optional<Organization> foundOrganization = organizationService.getOrganization("abcdef123456");
        assertEquals(1, foundOrganization.get().getAllOrganizationDescriptions().size());
        assertEquals("fi", foundOrganization.get().getAllOrganizationDescriptions().iterator().next().getLanguage());
        assertEquals("Vaasa", foundOrganization.get().getAllOrganizationDescriptions().iterator().next().getValue());
        TestUtil.verifySavedStatusInfo(foundOrganization.get().getAllOrganizationDescriptions().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveEmail() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        Email email = Email.builder()
                .language("fi").description("Asiakaspalvelu").value("vaasa@vaasa.fi").organization(organization.get()).build();
        organizationService.saveEmail(email);
        Optional<Organization> foundOrganization = organizationService.getOrganization("abcdef123456");
        assertEquals(1, foundOrganization.get().getAllEmails().size());
        assertEquals("fi", foundOrganization.get().getAllEmails().iterator().next().getLanguage());
        assertEquals("vaasa@vaasa.fi", foundOrganization.get().getAllEmails().iterator().next().getValue());
        TestUtil.verifySavedStatusInfo(foundOrganization.get().getAllEmails().iterator().next().getStatusInfo());
    }

    @Test
    public void testSavePhoneNumber() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        PhoneNumber phoneNumber = PhoneNumber.builder()
                .additionalInformation("Puhelinvaihde")
                .organization(organization.get())
                .number("62249111")
                .isFinnishServiceNumber(false)
                .prefixNumber("+358")
                .language("FI")
                .chargeDescription("Chargeable")
                .serviceChargeType("charge").build();
        organizationService.savePhoneNumber(phoneNumber);
        Optional<Organization> foundOrganization = organizationService.getOrganization("abcdef123456");
        assertEquals(1, foundOrganization.get().getAllPhoneNumbers().size());
        assertEquals("62249111", foundOrganization.get().getAllPhoneNumbers().iterator().next().getNumber());
        assertEquals(false, foundOrganization.get().getAllPhoneNumbers().iterator().next().getIsFinnishServiceNumber());
        TestUtil.verifySavedStatusInfo(foundOrganization.get().getAllPhoneNumbers().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveWebPage() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        WebPage webPage = WebPage.builder()
                .language("fi").url("https://www.vaasa.fi/").value("Vaasa").organization(organization.get()).build();
        organizationService.saveWebPage(webPage);
        Optional<Organization> foundOrganization = organizationService.getOrganization("abcdef123456");
        assertEquals(1, foundOrganization.get().getAllWebPages().size());
        assertEquals("fi", foundOrganization.get().getAllWebPages().iterator().next().getLanguage());
        assertEquals("https://www.vaasa.fi/", foundOrganization.get().getAllWebPages().iterator().next().getUrl());
        assertEquals("Vaasa", foundOrganization.get().getAllWebPages().iterator().next().getValue());
        TestUtil.verifySavedStatusInfo(foundOrganization.get().getAllWebPages().iterator().next().getStatusInfo());
    }

    @Test
    public void testSaveAddress() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllOrganizationNames().size());
        Address address = Address.builder()
                .country("UK").organization(organization.get()).type("Postal").subType("Street").build();
        Address savedAddress = organizationService.saveAddress(address);
        assertNotNull(savedAddress);
        assertEquals("UK", savedAddress.getCountry());
        assertEquals("Postal", savedAddress.getType());
        assertEquals("Street", savedAddress.getSubType());
        TestUtil.verifySavedStatusInfo(savedAddress.getStatusInfo());
    }

    @Test
    public void testSaveStreetAddress() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        StreetAddress streetAddress = StreetAddress.builder()
                .postalCode("12345").longitude("23").latitude("59").coordinateState("Ok")
                .streetNumber("100").address(organization.get().getAllAddresses().iterator().next()).build();
        StreetAddress savedStreetAddress = organizationService.saveStreetAddress(streetAddress);
        assertNotNull(savedStreetAddress);
        assertEquals("12345", savedStreetAddress.getPostalCode());
        assertEquals("23", savedStreetAddress.getLongitude());
        assertEquals("59", savedStreetAddress.getLatitude());
        assertEquals("100", savedStreetAddress.getStreetNumber());
        TestUtil.verifySavedStatusInfo(savedStreetAddress.getStatusInfo());
    }

    @Test
    public void testSaveStreet() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        Street street = Street.builder()
                .streetAddress(organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().iterator().next())
                .language("fi").value("Motellikuja").build();
        Street savedStreet = organizationService.saveStreet(street);
        assertNotNull(savedStreet);
        assertEquals("fi", savedStreet.getLanguage());
        assertEquals("Motellikuja", savedStreet.getValue());
        TestUtil.verifySavedStatusInfo(savedStreet.getStatusInfo());
    }

    @Test
    public void testSaveStreetAddressPostOffice() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        StreetAddressPostOffice streetAddressPostOffice = StreetAddressPostOffice.builder()
                .streetAddress(organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().iterator().next())
                .language("fi").value("NIVALA").build();
        StreetAddressPostOffice savedStreetAddressPostOffice = organizationService.saveStreetAddressPostOffice(streetAddressPostOffice);
        assertNotNull(savedStreetAddressPostOffice);
        assertEquals("fi", savedStreetAddressPostOffice.getLanguage());
        assertEquals("NIVALA", savedStreetAddressPostOffice.getValue());
        TestUtil.verifySavedStatusInfo(savedStreetAddressPostOffice.getStatusInfo());
    }

    @Test
    public void testSaveStreetAddressAdditionalInformation() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        StreetAddressAdditionalInformation addressAdditionalInformation = StreetAddressAdditionalInformation.builder()
                .streetAddress(organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().iterator().next())
                .language("fi").value("Kaupungintalo/kaupunginjohtaja").build();
        StreetAddressAdditionalInformation savedStreetAddressAdditionalInformation =
                organizationService.saveStreetAddressAdditionalInformation(addressAdditionalInformation);
        assertNotNull(savedStreetAddressAdditionalInformation);
        assertEquals("fi", savedStreetAddressAdditionalInformation.getLanguage());
        assertEquals("Kaupungintalo/kaupunginjohtaja", savedStreetAddressAdditionalInformation.getValue());
        TestUtil.verifySavedStatusInfo(savedStreetAddressAdditionalInformation.getStatusInfo());
    }

    @Test
    public void testSaveStreetAddressMunicipality() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        StreetAddressMunicipality streetAddressMunicipality = StreetAddressMunicipality.builder()
                .streetAddress(organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().iterator().next())
                .code("999").build();
        StreetAddressMunicipality savedStreetAddressMunicipality = organizationService.saveStreetAddressMunicipality(streetAddressMunicipality);
        assertNotNull(savedStreetAddressMunicipality);
        assertEquals("999", savedStreetAddressMunicipality.getCode());
        TestUtil.verifySavedStatusInfo(savedStreetAddressMunicipality.getStatusInfo());
    }

    @Test
    public void testSaveStreetAddressMunicipalityName() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        StreetAddressMunicipalityName streetAddressMunicipalityName = StreetAddressMunicipalityName.builder()
                .streetAddressMunicipality(organization.get().getAllAddresses().iterator().next().getAllStreetAddresses()
                        .iterator().next().getAllMunicipalities().iterator().next())
                .language("fi").value("Nivala").build();
        StreetAddressMunicipalityName savedStreetAddressMunicipalityName =
                organizationService.saveStreetAddressMunicipalityName(streetAddressMunicipalityName);
        assertNotNull(savedStreetAddressMunicipalityName);
        assertEquals("fi", savedStreetAddressMunicipalityName.getLanguage());
        assertEquals("Nivala", savedStreetAddressMunicipalityName.getValue());
        TestUtil.verifySavedStatusInfo(savedStreetAddressMunicipalityName.getStatusInfo());
    }

    @Test
    public void testSavePostOfficeBoxAddress() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        PostOfficeBoxAddress postOfficeBoxAddress = PostOfficeBoxAddress.builder()
                .postalCode("9876").address(organization.get().getAllAddresses().iterator().next()).build();
        PostOfficeBoxAddress savedPostOfficeBoxAddress = organizationService.savePostOfficeBoxAddress(postOfficeBoxAddress);
        assertNotNull(savedPostOfficeBoxAddress);
        assertEquals("9876", savedPostOfficeBoxAddress.getPostalCode());
        TestUtil.verifySavedStatusInfo(savedPostOfficeBoxAddress.getStatusInfo());
    }

    @Test
    public void testSavePostOffice() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().size());
        PostOffice postOffice = PostOffice.builder()
                .postOfficeBoxAddress(organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().iterator().next())
                .language("FI").value("Posti").build();
        PostOffice savedPostOffice = organizationService.savePostOffice(postOffice);
        assertNotNull(savedPostOffice);
        assertEquals("FI", savedPostOffice.getLanguage());
        assertEquals("Posti", savedPostOffice.getValue());
        TestUtil.verifySavedStatusInfo(savedPostOffice.getStatusInfo());
    }

    @Test
    public void testSavePostOfficeBox() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().size());
        PostOfficeBox postOfficeBox = PostOfficeBox.builder()
                .postOfficeBoxAddress(organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().iterator().next())
                .language("FI").value("NIVALA").build();
        PostOfficeBox savedPostOfficeBox = organizationService.savePostOfficeBox(postOfficeBox);
        assertNotNull(savedPostOfficeBox);
        assertEquals("FI", savedPostOfficeBox.getLanguage());
        assertEquals("NIVALA", savedPostOfficeBox.getValue());
        TestUtil.verifySavedStatusInfo(savedPostOfficeBox.getStatusInfo());
    }

    @Test
    public void testSavePostOfficeBoxAddressAdditionalInformation() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().size());
        PostOfficeBoxAddressAdditionalInformation addressAdditionalInformation = PostOfficeBoxAddressAdditionalInformation.builder()
                .postOfficeBoxAddress(organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().iterator().next())
                .language("fi").value("Kaupungintalo/kaupunginjohtaja").build();
        PostOfficeBoxAddressAdditionalInformation savedAdditionalInformation =
                organizationService.savePostOfficeBoxAddressAdditionalInformation(addressAdditionalInformation);
        assertNotNull(savedAdditionalInformation);
        assertEquals("fi", savedAdditionalInformation.getLanguage());
        assertEquals("Kaupungintalo/kaupunginjohtaja", savedAdditionalInformation.getValue());
        TestUtil.verifySavedStatusInfo(savedAdditionalInformation.getStatusInfo());
    }

    @Test
    public void testSavePostOfficeBoxAddressMunicipality() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllStreetAddresses().size());
        PostOfficeBoxAddressMunicipality postOfficeBoxAddressMunicipality = PostOfficeBoxAddressMunicipality.builder()
                .postOfficeBoxAddress(organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().iterator().next())
                .code("222").build();
        PostOfficeBoxAddressMunicipality savedPostOfficeBoxAddressMunicipality =
                organizationService.savePostOfficeBoxAddressMunicipality(postOfficeBoxAddressMunicipality);
        assertNotNull(savedPostOfficeBoxAddressMunicipality);
        assertEquals("222", savedPostOfficeBoxAddressMunicipality.getCode());
        TestUtil.verifySavedStatusInfo(savedPostOfficeBoxAddressMunicipality.getStatusInfo());
    }

    @Test
    public void testSavePostOfficeBoxMunicipalityName() {
        Optional<Organization> organization = organizationService.getOrganization("abcdef123456");
        assertEquals(true, organization.isPresent());
        assertEquals(1, organization.get().getAllAddresses().size());
        assertEquals(1, organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses().size());
        PostOfficeBoxAddressMunicipalityName postOfficeBoxAddressMunicipalityName = PostOfficeBoxAddressMunicipalityName.builder()
                .postOfficeBoxAddressMunicipality(organization.get().getAllAddresses().iterator().next().getAllPostOfficeBoxAddresses()
                        .iterator().next().getAllMunicipalities().iterator().next())
                .language("fi").value("Nivala").build();
        PostOfficeBoxAddressMunicipalityName savedPostOfficeBoxMunicipalityName =
                organizationService.savePostOfficeBoxAddressMunicipalityName(postOfficeBoxAddressMunicipalityName);
        assertNotNull(savedPostOfficeBoxMunicipalityName);
        assertEquals("fi", savedPostOfficeBoxMunicipalityName.getLanguage());
        assertEquals("Nivala", savedPostOfficeBoxMunicipalityName.getValue());
        TestUtil.verifySavedStatusInfo(savedPostOfficeBoxMunicipalityName.getStatusInfo());
    }
}
