/**
 * The MIT License
 * Copyright (c) 2016, Population Register Centre (VRK)
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
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.CatalogServiceImpl;
import fi.vrk.xroad.catalog.persistence.entity.*;
import fi.vrk.xroad.xroad_catalog_lister.ChangedValue;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;

import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Text JAXB mapping with mock CatalogServiceImpl
 */
public class JaxbCatalogServiceTest {

    public static final String PROPERTY_MEMBER_CODE = "memberCode";
    public static final String PROPERTY_SUBSYSTEM_CODE = "subsystemCode";
    public static final String PROPERTY_SERVICE_CODE = "serviceCode";
    JaxbCatalogServiceImpl service = new JaxbCatalogServiceImpl();
    JaxbConverter jaxbConverter = new JaxbConverter();

    private CatalogService catalogService;

    private static final LocalDateTime DATETIME_1800 = LocalDateTime.of(1800, 1, 1, 0, 0);
    private static final LocalDateTime DATETIME_2016 = LocalDateTime.of(2016, 1, 1, 0, 0);
    private static final LocalDateTime DATETIME_2015 = LocalDateTime.of(2015, 1, 1, 0, 0);

    /**
     * Setup mock CatalogServiceImpl
     */
    @Before
    public void setup() throws Exception {
        catalogService = new CatalogServiceImpl() {
            @Override
            public Iterable<Member> getAllMembers(LocalDateTime localDateTime) {
                return mockGetAllMembers(localDateTime);
            }
            @Override
            public Iterable<Member> getAllMembers() {
                return mockGetAllMembers(DATETIME_1800);
            }
            @Override
            public Iterable<Organization> getOrganizations(String businessCode) {
                return mockGetOrganizations(businessCode);
            }
            @Override
            public Optional<Organization> getOrganization(String guid){
                return mockGetOrganization(guid);
            }
        };
        service = new JaxbCatalogServiceImpl();
        service.setJaxbConverter(jaxbConverter);
        service.setCatalogService(catalogService);
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
    public void testGetChangedValuesAfter2014() {
        XMLGregorianCalendar changedAfter20141231 = jaxbConverter.toXmlGregorianCalendar(
                LocalDateTime.of(2014, 12, 31, 0, 0)
        );
        Iterable<ChangedValue> changedValues = service.getChangedValues("abc123456789", changedAfter20141231);
        assertEquals(19, Iterables.size(changedValues));
    }

    @Test
    public void testGetChangedValuesAfter2015() {
        XMLGregorianCalendar changedAfter20151231 = jaxbConverter.toXmlGregorianCalendar(
                LocalDateTime.of(2015, 12, 31, 0, 0)
        );
        Iterable<ChangedValue> changedValues = service.getChangedValues("abc123456789", changedAfter20151231);
        assertEquals(1, Iterables.size(changedValues));
        assertEquals("Email", changedValues.iterator().next().getName());
    }

    @Test
    public void testGetChangedValuesAfter2016() {
        XMLGregorianCalendar changedAfter20160101 = jaxbConverter.toXmlGregorianCalendar(
                LocalDateTime.of(2016, 1, 1, 0, 0)
        );
        Iterable<ChangedValue> changedValues = service.getChangedValues("abc123456789", changedAfter20160101);
        assertEquals(0, Iterables.size(changedValues));
    }

    @Test
    public void testGetAll() throws Exception {
        XMLGregorianCalendar calendar20150505 = jaxbConverter.toXmlGregorianCalendar(
                LocalDateTime.of(2015, 5, 5, 0, 0)
        );
        XMLGregorianCalendar calendar20140505 = jaxbConverter.toXmlGregorianCalendar(
                LocalDateTime.of(2014, 5, 5, 0, 0)
        );
        Iterable<fi.vrk.xroad.xroad_catalog_lister.Member> members = service.getAllMembers(calendar20150505);
        assertEquals(2, Iterables.size(members));
        assertEquals(new HashSet<String>(Arrays.asList("2", "3")),
                new HashSet<String>(getMemberCodes(members)));
        assertMember2Contents(members);

        members = service.getAllMembers(calendar20140505);
        assertEquals(3, Iterables.size(members));
        assertEquals(new HashSet<String>(Arrays.asList("1", "2", "3")),
                new HashSet<String>(getMemberCodes(members)));
        assertMember2Contents(members);

        members = service.getAllMembers(null);
        assertEquals(3, Iterables.size(members));
        assertEquals(new HashSet<String>(Arrays.asList("1", "2", "3")),
                new HashSet<String>(getMemberCodes(members)));
        assertMember2Contents(members);
    }

    /**
     * Assert that member 2 has subsystem-service-wsdl contents that we expect
     */
    private void assertMember2Contents(Iterable<fi.vrk.xroad.xroad_catalog_lister.Member> members) throws
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        fi.vrk.xroad.xroad_catalog_lister.Member m2 = (fi.vrk.xroad.xroad_catalog_lister.Member)
                getItem(members, PROPERTY_MEMBER_CODE, "2");
        assertNotNull(m2);
        assertEquals(new HashSet<String>(Arrays.asList("21", "22", "23")),
                new HashSet<String>(getSubsystemCodes(m2.getSubsystems().getSubsystem())));
        fi.vrk.xroad.xroad_catalog_lister.Subsystem ss21 = (fi.vrk.xroad.xroad_catalog_lister.Subsystem)
                getItem(m2.getSubsystems().getSubsystem(), PROPERTY_SUBSYSTEM_CODE, "21");
        fi.vrk.xroad.xroad_catalog_lister.Subsystem ss22 = (fi.vrk.xroad.xroad_catalog_lister.Subsystem)
                getItem(m2.getSubsystems().getSubsystem(), PROPERTY_SUBSYSTEM_CODE, "22");
        fi.vrk.xroad.xroad_catalog_lister.Subsystem ss23 = (fi.vrk.xroad.xroad_catalog_lister.Subsystem)
                getItem(m2.getSubsystems().getSubsystem(), PROPERTY_SUBSYSTEM_CODE, "23");
        assertNotNull(ss21);
        assertNotNull(ss22);
        assertNotNull(ss23);
        assertNull(ss21.getRemoved());
        assertNull(ss22.getRemoved());
        assertNotNull(ss23.getRemoved());
        assertEquals(new HashSet<String>(Arrays.asList("211", "212", "213")),
                new HashSet<String>(getServiceCodes(ss21.getServices().getService())));
        assertEquals(new HashSet<String>(Arrays.asList("221", "222", "223")),
                new HashSet<String>(getServiceCodes(ss22.getServices().getService())));
        assertEquals(new HashSet<String>(Arrays.asList("231", "232", "233")),
                new HashSet<String>(getServiceCodes(ss23.getServices().getService())));
        fi.vrk.xroad.xroad_catalog_lister.Service s221 = (fi.vrk.xroad.xroad_catalog_lister.Service)
                getItem(ss22.getServices().getService(), PROPERTY_SERVICE_CODE, "221");
        fi.vrk.xroad.xroad_catalog_lister.Service s222 = (fi.vrk.xroad.xroad_catalog_lister.Service)
                getItem(ss22.getServices().getService(), PROPERTY_SERVICE_CODE, "222");
        fi.vrk.xroad.xroad_catalog_lister.Service s223 = (fi.vrk.xroad.xroad_catalog_lister.Service)
                getItem(ss22.getServices().getService(), PROPERTY_SERVICE_CODE, "223");
        assertNotNull(s221);
        assertNotNull(s222);
        assertNotNull(s223);
        assertNull(s221.getRemoved());
        assertNull(s222.getRemoved());
        assertNotNull(s223.getRemoved());
    }


    private Collection<String> getPropertyValues(Iterable items, String propertyName) throws IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        List<String> values = new ArrayList<>();
        for (Object item: items) {
            values.add(getStringProperty(propertyName, item));
        }
        return values;
    }

    private String getStringProperty(String propertyName, Object item) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return (String) PropertyUtils.getProperty(item, propertyName);
    }

    private Object getItem(Iterable items, String propertyName, String value) throws IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        for (Object item: items) {
            if (value.equals(getStringProperty(propertyName, item))) {
                return item;
            }
        }
        return null;
    }

    private Collection<String> getMemberCodes(Iterable<fi.vrk.xroad.xroad_catalog_lister.Member> members) throws
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return getPropertyValues(members, PROPERTY_MEMBER_CODE);
    }

    private Collection<String> getSubsystemCodes(Iterable<fi.vrk.xroad.xroad_catalog_lister.Subsystem> subsystems)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return getPropertyValues(subsystems, PROPERTY_SUBSYSTEM_CODE);
    }

    private Collection<String> getServiceCodes(Iterable<fi.vrk.xroad.xroad_catalog_lister.Service> services) throws
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return getPropertyValues(services, PROPERTY_SERVICE_CODE);
    }

    // setup test data member-subsystem-service-wsdl

    private Map<Long, Member> createTestMembers() {
        Map<Long, Member> members = new HashMap<>();
        Member m1 = createMember(DATETIME_2015, 1, false);
        Member m2 = createMember(DATETIME_2016, 2, false);
        Member m3 = createMember(DATETIME_2016, 3, true);
        members.put(m1.getId(), m1);
        members.put(m2.getId(), m2);
        members.put(m3.getId(), m3);
        return members;
    }

    private Member createMember(LocalDateTime updated, int id, boolean removed) {
        Member m = new Member();
        m.setMemberClass("GOV");
        m.setXRoadInstance("FI");
        m.setMemberCode("" + id);
        m.setId(id);
        m.setName("membername-" + id);
        m.getStatusInfo().setChanged(updated);
        m.getStatusInfo().setCreated(updated);
        m.getStatusInfo().setFetched(updated);
        m.getStatusInfo().setRemoved(removed ? updated : null);

        Subsystem s1 = createSubsystem(DATETIME_2015, 1, false, m);
        Subsystem s2 = createSubsystem(DATETIME_2016, 2, false, m);
        Subsystem s3 = createSubsystem(DATETIME_2016, 3, true, m);
        m.getAllSubsystems().add(s1);
        m.getAllSubsystems().add(s2);
        m.getAllSubsystems().add(s3);

        return m;
    }

    private Subsystem createSubsystem(LocalDateTime updated, int id, boolean removed, Member m) {
        Subsystem subsystem = new Subsystem();
        subsystem.setMember(m);
        subsystem.setId(m.getId() * 10 + id);
        subsystem.getStatusInfo().setChanged(updated);
        subsystem.getStatusInfo().setCreated(updated);
        subsystem.getStatusInfo().setFetched(updated);
        subsystem.getStatusInfo().setRemoved(removed ? updated : null);
        subsystem.setSubsystemCode("" + subsystem.getId());

        Service s1 = createService(updated, false, subsystem, 1);
        Service s2 = createService(updated, false, subsystem, 2);
        Service s3 = createService(updated, true, subsystem, 3);

        subsystem.getAllServices().add(s1);
        subsystem.getAllServices().add(s2);
        subsystem.getAllServices().add(s3);

        return subsystem;
    }

    private Service createService(LocalDateTime updated, boolean removed, Subsystem subsystem, int id) {
        Service s1 = new Service();
        s1.setId((subsystem.getId() * 10) + id);
        s1.setServiceVersion("v1");
        s1.setServiceCode("" + s1.getId());
        s1.getStatusInfo().setChanged(updated);
        s1.getStatusInfo().setCreated(updated);
        s1.getStatusInfo().setFetched(updated);
        s1.getStatusInfo().setRemoved(removed ? updated : null);
        s1.setSubsystem(subsystem);

        s1.setWsdl(createWsdl(s1));
        return s1;
    }

    private Wsdl createWsdl(Service s1) {
        Wsdl wsdl = new Wsdl();
        wsdl.setData("<xml/>");
        wsdl.setExternalId(s1.getId() * 10 + "1");
        wsdl.setService(s1);
        wsdl.setId(s1.getId() * 10 + 1);
        wsdl.getStatusInfo().setChanged(s1.getStatusInfo().getChanged());
        wsdl.getStatusInfo().setCreated(s1.getStatusInfo().getCreated());
        wsdl.getStatusInfo().setFetched(s1.getStatusInfo().getFetched());
        wsdl.getStatusInfo().setRemoved(s1.getStatusInfo().getRemoved());
        return wsdl;
    }

    private Iterable<Member> mockGetAllMembers(LocalDateTime l) {
        Collection<Long> keys;
        if (l.isAfter(DATETIME_2015)) {
            keys = Arrays.asList(2L, 3L);
        } else {
            keys = Arrays.asList(1L, 2L, 3L);
        }
        List<Member> matchingMembers = new ArrayList<>();
        Map<Long, Member> allMembers = createTestMembers();
        for (Long key: keys) {
            matchingMembers.add(allMembers.get(key));
        }
        return matchingMembers;
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

    private StatusInfo createStatusInfo(LocalDateTime created,
                                        LocalDateTime changed,
                                        LocalDateTime fetched,
                                        LocalDateTime removed) {
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setCreated(created);
        statusInfo.setChanged(changed);
        statusInfo.setFetched(fetched);
        statusInfo.setRemoved(removed);
        return statusInfo;
    }

    private Organization createTestOrganizationByBusinessCode(String businessCode) {
        Organization organization = new Organization();
        organization.setId(1L);
        organization.setBusinessCode(businessCode);
        organization.setPublishingStatus("Published");
        organization.setGuid("a123456789");
        organization.setOrganizationType("Municipality");
        organization.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
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
        organization.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
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
        organizationName.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
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
        organizationDescription.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
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
        email.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2016, DATETIME_2015, null));
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
        phoneNumber.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
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
        webPage.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
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
        address.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));

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
        streetAddress.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));

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
        street.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
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
        streetAddressPostOffice.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
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
        streetAddressAdditionalInformation.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        streetAddressAdditionalInformationList.add(streetAddressAdditionalInformation);
        return streetAddressAdditionalInformationList;
    }

    private Set<StreetAddressMunicipality> createStreetAddressMunicipalities(StreetAddress s) {
        Set<StreetAddressMunicipality> streetAddressMunicipalities = new HashSet<>();
        StreetAddressMunicipality streetAddressMunicipality = new StreetAddressMunicipality();
        streetAddressMunicipality.setStreetAddress(s);
        streetAddressMunicipality.setId(1L);
        streetAddressMunicipality.setCode("545");
        streetAddressMunicipality.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));

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
        streetAddressMunicipalityName.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        streetAddressMunicipalityNames.add(streetAddressMunicipalityName);
        return streetAddressMunicipalityNames;
    }

    private Set<PostOfficeBoxAddress> createPostOfficeBoxAddresses(Address a) {
        Set<PostOfficeBoxAddress> postOfficeBoxAddresses = new HashSet<>();
        PostOfficeBoxAddress postOfficeBoxAddress = new PostOfficeBoxAddress();
        postOfficeBoxAddress.setId(1L);
        postOfficeBoxAddress.setAddress(a);
        postOfficeBoxAddress.setPostalCode("64200");
        postOfficeBoxAddress.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));

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
        postOffice.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
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
        postOfficeBox.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
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
        addressAdditionalInformation.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        addressAdditionalInformationList.add(addressAdditionalInformation);
        return addressAdditionalInformationList;
    }

    private Set<PostOfficeBoxAddressMunicipality> createPostOfficeBoxAddressMunicipalities(PostOfficeBoxAddress p) {
        Set<PostOfficeBoxAddressMunicipality> postOfficeBoxAddressMunicipalities = new HashSet<>();
        PostOfficeBoxAddressMunicipality postOfficeBoxAddressMunicipality = new PostOfficeBoxAddressMunicipality();
        postOfficeBoxAddressMunicipality.setPostOfficeBoxAddress(p);
        postOfficeBoxAddressMunicipality.setId(1L);
        postOfficeBoxAddressMunicipality.setCode("545");
        postOfficeBoxAddressMunicipality.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));

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
        postOfficeBoxAddressMunicipalityName.setStatusInfo(createStatusInfo(DATETIME_2015, DATETIME_2015, DATETIME_2015, null));
        postOfficeBoxAddressMunicipalityNames.add(postOfficeBoxAddressMunicipalityName);
        return postOfficeBoxAddressMunicipalityNames;
    }

}
