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
package fi.vrk.xroad.catalog.lister;

import com.google.common.collect.Iterables;
import fi.vrk.xroad.catalog.lister.util.JaxbUtil;
import fi.vrk.xroad.catalog.persistence.OrganizationService;
import fi.vrk.xroad.catalog.persistence.OrganizationServiceImpl;
import fi.vrk.xroad.catalog.persistence.entity.Address;
import fi.vrk.xroad.catalog.persistence.entity.Email;
import fi.vrk.xroad.catalog.persistence.entity.Organization;
import fi.vrk.xroad.catalog.persistence.entity.OrganizationDescription;
import fi.vrk.xroad.catalog.persistence.entity.OrganizationName;
import fi.vrk.xroad.catalog.persistence.entity.PhoneNumber;
import fi.vrk.xroad.catalog.persistence.entity.PostOffice;
import fi.vrk.xroad.catalog.persistence.entity.PostOfficeBox;
import fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddress;
import fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressAdditionalInformation;
import fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipality;
import fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipalityName;
import fi.vrk.xroad.catalog.persistence.entity.Street;
import fi.vrk.xroad.catalog.persistence.entity.StreetAddress;
import fi.vrk.xroad.catalog.persistence.entity.StreetAddressAdditionalInformation;
import fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipality;
import fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipalityName;
import fi.vrk.xroad.catalog.persistence.entity.StreetAddressPostOffice;
import fi.vrk.xroad.catalog.persistence.entity.WebPage;
import fi.vrk.xroad.xroad_catalog_lister.ChangedValue;
import org.junit.Before;
import org.junit.Test;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.Assert.assertEquals;

public class JaxbOrganizationServiceTest {

    JaxbOrganizationServiceImpl service = new JaxbOrganizationServiceImpl();
    JaxbConverter jaxbConverter = new JaxbConverter();

    private OrganizationService organizationService;
    private static final LocalDateTime DATETIME_2016 = LocalDateTime.of(2016, 1, 1, 0, 0);
    private static final LocalDateTime DATETIME_2015 = LocalDateTime.of(2015, 1, 1, 0, 0);

    @Before
    public void setup() {
        organizationService = new OrganizationServiceImpl() {
            @Override
            public Iterable<Organization> getOrganizations(String businessCode) {
                return mockGetOrganizations(businessCode);
            }
            @Override
            public Optional<Organization> getOrganization(String guid){
                return mockGetOrganization(guid);
            }
        };
        service = new JaxbOrganizationServiceImpl();
        service.setJaxbConverter(jaxbConverter);
        service.setOrganizationService(organizationService);
    }

    @Test
    public void testGetOrganizations() {
        Iterable<fi.vrk.xroad.xroad_catalog_lister.Organization> organizations = service.getOrganizations("123456-9");
        assertEquals(1, Iterables.size(organizations));
        assertEquals(1, organizations.iterator().next().getOrganizationNames().getOrganizationName().size());
        assertEquals(1, organizations.iterator().next().getOrganizationDescriptions()
                .getOrganizationDescription().size());
        assertEquals(1, organizations.iterator().next().getEmails().getEmail().size());
        assertEquals(1, organizations.iterator().next().getPhoneNumbers().getPhoneNumber().size());
        assertEquals(1, organizations.iterator().next().getWebPages().getWebPage().size());
        assertEquals(1, organizations.iterator().next().getAddresses().getAddress().size());
        assertEquals(1, organizations.iterator().next().getAddresses().getAddress().get(0)
                .getStreetAddresses().getStreetAddress().size());
        assertEquals(1, organizations.iterator().next().getAddresses().getAddress().get(0)
                .getPostOfficeBoxAddresses().getPostOfficeBoxAddress().size());
        assertEquals("123456-9", organizations.iterator().next().getBusinessCode());
        assertEquals("a123456789", organizations.iterator().next().getGuid());
        assertEquals("Published", organizations.iterator().next().getPublishingStatus());
        assertEquals("Municipality", organizations.iterator().next().getOrganizationType());
        assertEquals("Vaasan kaupunki", organizations.iterator().next().getOrganizationNames().getOrganizationName().get(0).getValue());
        assertEquals("Vaasa on yli 67 000 asukkaan voimakkaasti kasvava kaupunki",
                organizations.iterator().next().getOrganizationDescriptions().getOrganizationDescription().get(0).getValue());
        assertEquals("vaasa@vaasa.fi", organizations.iterator().next().getEmails().getEmail().get(0).getValue());
        assertEquals("62249111", organizations.iterator().next().getPhoneNumbers().getPhoneNumber().get(0).getNumber());
        assertEquals("https://www.vaasa.fi", organizations.iterator().next().getWebPages().getWebPage().get(0).getUrl());
        assertEquals("Street", organizations.iterator().next().getAddresses().getAddress().get(0).getSubType());
        assertEquals("64200", organizations.iterator().next().getAddresses().getAddress().get(0)
                .getStreetAddresses().getStreetAddress().get(0).getPostalCode());
        assertEquals("Motellikuja", organizations.iterator().next().getAddresses().getAddress().get(0)
                .getStreetAddresses().getStreetAddress().get(0).getStreets().getStreet().get(0).getValue());
    }

    @Test
    public void testGetChangedOrganizationValuesAfter2014() {
        XMLGregorianCalendar changedAfter20141231 = JaxbUtil.toXmlGregorianCalendar(LocalDateTime.of(2014, 12, 31, 0, 0));
        XMLGregorianCalendar endDateTime = JaxbUtil.toXmlGregorianCalendar(LocalDateTime.of(2022, 12, 31, 0, 0));
        Iterable<ChangedValue> changedValues = service.getChangedOrganizationValues("abc123456789",
                changedAfter20141231, endDateTime);
        assertEquals(19, Iterables.size(changedValues));
    }

    @Test
    public void testGetChangedOrganizationValuesAfter2015() {
        XMLGregorianCalendar changedAfter20151231 = JaxbUtil.toXmlGregorianCalendar(LocalDateTime.of(2015, 12, 31, 0, 0));
        XMLGregorianCalendar endDateTime = JaxbUtil.toXmlGregorianCalendar(LocalDateTime.of(2022, 12, 31, 0, 0));
        Iterable<ChangedValue> changedValues = service.getChangedOrganizationValues("abc123456789",
                changedAfter20151231, endDateTime);
        assertEquals(1, Iterables.size(changedValues));
        assertEquals("Email", changedValues.iterator().next().getName());
    }

    @Test
    public void testGetChangedOrganizationValuesAfter2016() {
        XMLGregorianCalendar changedAfter20160101 = JaxbUtil.toXmlGregorianCalendar(LocalDateTime.of(2016, 1, 1, 0, 0));
        XMLGregorianCalendar endDateTime = JaxbUtil.toXmlGregorianCalendar(LocalDateTime.of(2022, 12, 31, 0, 0));
        Iterable<ChangedValue> changedValues = service.getChangedOrganizationValues("abc123456789",
                changedAfter20160101, endDateTime);
        assertEquals(0, Iterables.size(changedValues));
    }

    private Iterable<Organization> mockGetOrganizations(String businessCode) {
        List<Organization> organizations = new ArrayList<>();
        organizations.add(createTestOrganizationByBusinessCode(businessCode));
        return organizations;
    }

    private Optional<Organization> mockGetOrganization(String guid) {
        Organization organization = createTestOrganizationByGuid(guid);
        return Optional.of(organization);
    }

    private Organization createTestOrganizationByBusinessCode(String businessCode) {
        Organization organization = new Organization();
        organization.setId(1L);
        organization.setBusinessCode(businessCode);
        organization.setPublishingStatus("Published");
        organization.setGuid("a123456789");
        organization.setOrganizationType("Municipality");
        organization.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        organization.setOrganizationNames(createOrganizationNames(organization));
        organization.setOrganizationDescriptions(createOrganizationDescriptions(organization));
        organization.setEmails(createEmails(organization));
        organization.setPhoneNumbers(createPhoneNumbers(organization));
        organization.setWebPages(createWebPages(organization));
        organization.setAddresses(createAddresses(organization));

        return organization;
    }

    private Organization createTestOrganizationByGuid(String guid) {
        Organization organization = new Organization();
        organization.setId(1L);
        organization.setBusinessCode("1234567-9");
        organization.setPublishingStatus("Published");
        organization.setGuid(guid);
        organization.setOrganizationType("Municipality");
        organization.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        organization.setOrganizationNames(createOrganizationNames(organization));
        organization.setOrganizationDescriptions(createOrganizationDescriptions(organization));
        organization.setEmails(createEmails(organization));
        organization.setPhoneNumbers(createPhoneNumbers(organization));
        organization.setWebPages(createWebPages(organization));
        organization.setAddresses(createAddresses(organization));

        return organization;
    }

    private Set<OrganizationName> createOrganizationNames(Organization o) {
        Set<OrganizationName> organizationNames = new HashSet<>();
        OrganizationName organizationName = new OrganizationName();
        organizationName.setOrganization(o);
        organizationName.setId(1L);
        organizationName.setLanguage("FI");
        organizationName.setType("Name");
        organizationName.setValue("Vaasan kaupunki");
        organizationName.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        organizationNames.add(organizationName);
        return organizationNames;
    }

    private Set<OrganizationDescription> createOrganizationDescriptions(Organization o) {
        Set<OrganizationDescription> organizationDescriptions = new HashSet<>();
        OrganizationDescription organizationDescription = new OrganizationDescription();
        organizationDescription.setOrganization(o);
        organizationDescription.setId(1L);
        organizationDescription.setLanguage("FI");
        organizationDescription.setType("Description");
        organizationDescription.setValue("Vaasa on yli 67 000 asukkaan voimakkaasti kasvava kaupunki");
        organizationDescription.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        organizationDescriptions.add(organizationDescription);
        return organizationDescriptions;
    }

    private Set<Email> createEmails(Organization o) {
        Set<Email> emails = new HashSet<>();
        Email email = new Email();
        email.setOrganization(o);
        email.setId(1L);
        email.setLanguage("FI");
        email.setDescription("Asiakaspalvelu");
        email.setValue("vaasa@vaasa.fi");
        email.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2016, DATETIME_2015, null));
        emails.add(email);
        return emails;
    }

    private Set<PhoneNumber> createPhoneNumbers(Organization o) {
        Set<PhoneNumber> phoneNumbers = new HashSet<>();
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setOrganization(o);
        phoneNumber.setId(1L);
        phoneNumber.setLanguage("FI");
        phoneNumber.setAdditionalInformation("Puhelinvaihde");
        phoneNumber.setChargeDescription("charge");
        phoneNumber.setServiceChargeType("chargeable");
        phoneNumber.setIsFinnishServiceNumber(true);
        phoneNumber.setNumber("62249111");
        phoneNumber.setPrefixNumber("+358");
        phoneNumber.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        phoneNumbers.add(phoneNumber);
        return phoneNumbers;
    }

    private Set<WebPage> createWebPages(Organization o) {
        Set<WebPage> webPages = new HashSet<>();
        WebPage webPage = new WebPage();
        webPage.setOrganization(o);
        webPage.setId(1L);
        webPage.setLanguage("FI");
        webPage.setUrl("https://www.vaasa.fi");
        webPage.setValue("Vaasan kaupunki");
        webPage.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        webPages.add(webPage);
        return webPages;
    }

    private Set<Address> createAddresses(Organization o) {
        Set<Address> addresses = new HashSet<>();
        Address address = new Address();
        address.setOrganization(o);
        address.setId(1L);
        address.setPostOfficeBoxAddresses(null);
        address.setCountry("FI");
        address.setType("Postal");
        address.setSubType("Street");
        address.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));

        address.setStreetAddresses(createStreetAddresses(address));
        address.setPostOfficeBoxAddresses(createPostOfficeBoxAddresses(address));

        addresses.add(address);
        return addresses;
    }

    private Set<StreetAddress> createStreetAddresses(Address a) {
        Set<StreetAddress> streetAddresses = new HashSet<>();
        StreetAddress streetAddress = new StreetAddress();
        streetAddress.setId(1L);
        streetAddress.setAddress(a);
        streetAddress.setStreetNumber("2");
        streetAddress.setPostalCode("64200");
        streetAddress.setLongitude("208229.722");
        streetAddress.setLatitude("6939589.246");
        streetAddress.setCoordinateState("Ok");
        streetAddress.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));

        streetAddress.setStreets(createStreets(streetAddress));
        streetAddress.setPostOffices(createStreetAddressPostOffices(streetAddress));
        streetAddress.setAdditionalInformation(createStreetAddressAdditionalInformation(streetAddress));
        streetAddress.setMunicipalities(createStreetAddressMunicipalities(streetAddress));

        streetAddresses.add(streetAddress);
        return streetAddresses;
    }

    private Set<Street> createStreets(StreetAddress s) {
        Set<Street> streets = new HashSet<>();
        Street street = new Street();
        street.setStreetAddress(s);
        street.setId(1L);
        street.setLanguage("FI");
        street.setValue("Motellikuja");
        street.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        streets.add(street);
        return streets;
    }

    private Set<StreetAddressPostOffice> createStreetAddressPostOffices(StreetAddress s) {
        Set<StreetAddressPostOffice> streetAddressPostOffices = new HashSet<>();
        StreetAddressPostOffice streetAddressPostOffice = new StreetAddressPostOffice();
        streetAddressPostOffice.setStreetAddress(s);
        streetAddressPostOffice.setId(1L);
        streetAddressPostOffice.setLanguage("FI");
        streetAddressPostOffice.setValue("NIVALA");
        streetAddressPostOffice.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        streetAddressPostOffices.add(streetAddressPostOffice);
        return streetAddressPostOffices;
    }

    private Set<StreetAddressAdditionalInformation> createStreetAddressAdditionalInformation(StreetAddress s) {
        Set<StreetAddressAdditionalInformation> streetAddressAdditionalInformationList = new HashSet<>();
        StreetAddressAdditionalInformation streetAddressAdditionalInformation = new StreetAddressAdditionalInformation();
        streetAddressAdditionalInformation.setStreetAddress(s);
        streetAddressAdditionalInformation.setId(1L);
        streetAddressAdditionalInformation.setLanguage("FI");
        streetAddressAdditionalInformation.setValue("Kaupungintalo/kaupunginjohtaja");
        streetAddressAdditionalInformation.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        streetAddressAdditionalInformationList.add(streetAddressAdditionalInformation);
        return streetAddressAdditionalInformationList;
    }

    private Set<StreetAddressMunicipality> createStreetAddressMunicipalities(StreetAddress s) {
        Set<StreetAddressMunicipality> streetAddressMunicipalities = new HashSet<>();
        StreetAddressMunicipality streetAddressMunicipality = new StreetAddressMunicipality();
        streetAddressMunicipality.setStreetAddress(s);
        streetAddressMunicipality.setId(1L);
        streetAddressMunicipality.setCode("545");
        streetAddressMunicipality.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));

        streetAddressMunicipality.setStreetAddressMunicipalityNames(createStreetAddressMunicipalityNames(streetAddressMunicipality));

        streetAddressMunicipalities.add(streetAddressMunicipality);
        return streetAddressMunicipalities;
    }

    private Set<StreetAddressMunicipalityName> createStreetAddressMunicipalityNames(StreetAddressMunicipality s) {
        Set<StreetAddressMunicipalityName> streetAddressMunicipalityNames = new HashSet<>();
        StreetAddressMunicipalityName streetAddressMunicipalityName = new StreetAddressMunicipalityName();
        streetAddressMunicipalityName.setStreetAddressMunicipality(s);
        streetAddressMunicipalityName.setId(1L);
        streetAddressMunicipalityName.setLanguage("FI");
        streetAddressMunicipalityName.setValue("Nivala");
        streetAddressMunicipalityName.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        streetAddressMunicipalityNames.add(streetAddressMunicipalityName);
        return streetAddressMunicipalityNames;
    }

    private Set<PostOfficeBoxAddress> createPostOfficeBoxAddresses(Address a) {
        Set<PostOfficeBoxAddress> postOfficeBoxAddresses = new HashSet<>();
        PostOfficeBoxAddress postOfficeBoxAddress = new PostOfficeBoxAddress();
        postOfficeBoxAddress.setId(1L);
        postOfficeBoxAddress.setAddress(a);
        postOfficeBoxAddress.setPostalCode("64200");
        postOfficeBoxAddress.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));

        postOfficeBoxAddress.setPostOffices(createPostOffices(postOfficeBoxAddress));
        postOfficeBoxAddress.setPostOfficesBoxes(createPostOfficeBoxes(postOfficeBoxAddress));
        postOfficeBoxAddress.setAdditionalInformation(createAdditionalInformation(postOfficeBoxAddress));
        postOfficeBoxAddress.setPostOfficeBoxAddressMunicipalities(createPostOfficeBoxAddressMunicipalities(postOfficeBoxAddress));

        postOfficeBoxAddresses.add(postOfficeBoxAddress);
        return postOfficeBoxAddresses;
    }

    private Set<PostOffice> createPostOffices(PostOfficeBoxAddress p) {
        Set<PostOffice> postOffices = new HashSet<>();
        PostOffice postOffice = new PostOffice();
        postOffice.setId(1L);
        postOffice.setPostOfficeBoxAddress(p);
        postOffice.setLanguage("FI");
        postOffice.setValue("NIVALA");
        postOffice.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        postOffices.add(postOffice);
        return postOffices;
    }

    private Set<PostOfficeBox> createPostOfficeBoxes(PostOfficeBoxAddress p) {
        Set<PostOfficeBox> postOfficeBoxes = new HashSet<>();
        PostOfficeBox postOfficeBox = new PostOfficeBox();
        postOfficeBox.setId(1L);
        postOfficeBox.setPostOfficeBoxAddress(p);
        postOfficeBox.setLanguage("FI");
        postOfficeBox.setValue("123");
        postOfficeBox.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        postOfficeBoxes.add(postOfficeBox);
        return postOfficeBoxes;
    }

    private Set<PostOfficeBoxAddressAdditionalInformation> createAdditionalInformation(PostOfficeBoxAddress p) {
        Set<PostOfficeBoxAddressAdditionalInformation> addressAdditionalInformationList = new HashSet<>();
        PostOfficeBoxAddressAdditionalInformation addressAdditionalInformation = new PostOfficeBoxAddressAdditionalInformation();
        addressAdditionalInformation.setId(1L);
        addressAdditionalInformation.setPostOfficeBoxAddress(p);
        addressAdditionalInformation.setLanguage("FI");
        addressAdditionalInformation.setValue("Something");
        addressAdditionalInformation.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        addressAdditionalInformationList.add(addressAdditionalInformation);
        return addressAdditionalInformationList;
    }

    private Set<PostOfficeBoxAddressMunicipality> createPostOfficeBoxAddressMunicipalities(PostOfficeBoxAddress p) {
        Set<PostOfficeBoxAddressMunicipality> postOfficeBoxAddressMunicipalities = new HashSet<>();
        PostOfficeBoxAddressMunicipality postOfficeBoxAddressMunicipality = new PostOfficeBoxAddressMunicipality();
        postOfficeBoxAddressMunicipality.setPostOfficeBoxAddress(p);
        postOfficeBoxAddressMunicipality.setId(1L);
        postOfficeBoxAddressMunicipality.setCode("545");
        postOfficeBoxAddressMunicipality.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));

        postOfficeBoxAddressMunicipality.setPostOfficeBoxAddressMunicipalityNames(
                createPostOfficeBoxAddressMunicipalityNames(postOfficeBoxAddressMunicipality));

        postOfficeBoxAddressMunicipalities.add(postOfficeBoxAddressMunicipality);
        return postOfficeBoxAddressMunicipalities;
    }

    private Set<PostOfficeBoxAddressMunicipalityName> createPostOfficeBoxAddressMunicipalityNames(PostOfficeBoxAddressMunicipality p) {
        Set<PostOfficeBoxAddressMunicipalityName> postOfficeBoxAddressMunicipalityNames = new HashSet<>();
        PostOfficeBoxAddressMunicipalityName postOfficeBoxAddressMunicipalityName = new PostOfficeBoxAddressMunicipalityName();
        postOfficeBoxAddressMunicipalityName.setPostOfficeBoxAddressMunicipality(p);
        postOfficeBoxAddressMunicipalityName.setId(1L);
        postOfficeBoxAddressMunicipalityName.setLanguage("FI");
        postOfficeBoxAddressMunicipalityName.setValue("Nivala");
        postOfficeBoxAddressMunicipalityName.setStatusInfo(TestUtil.createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        postOfficeBoxAddressMunicipalityNames.add(postOfficeBoxAddressMunicipalityName);
        return postOfficeBoxAddressMunicipalityNames;
    }
}
