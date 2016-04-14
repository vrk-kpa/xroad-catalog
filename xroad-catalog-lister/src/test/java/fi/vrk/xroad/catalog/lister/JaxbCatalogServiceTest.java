package fi.vrk.xroad.catalog.lister;

import com.google.common.collect.Iterables;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.CatalogServiceImpl;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
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
        };
        service = new JaxbCatalogServiceImpl();
        service.setJaxbConverter(jaxbConverter);
        service.setCatalogService(catalogService);
    }

    @Test
    public void testGetAll() throws Exception {
        XMLGregorianCalendar calendar2015_5_5 = jaxbConverter.toXmlGregorianCalendar(
                LocalDateTime.of(2015, 5, 5, 0, 0)
        );
        XMLGregorianCalendar calendar2014_5_5 = jaxbConverter.toXmlGregorianCalendar(
                LocalDateTime.of(2014, 5, 5, 0, 0)
        );
        Iterable<fi.vrk.xroad.xroad_catalog_lister.Member> members = service.getAllMembers(calendar2015_5_5);
        assertEquals(2, Iterables.size(members));
        assertEquals(new HashSet<String>(Arrays.asList("2", "3")),
                new HashSet<String>(getMemberCodes(members)));
        assertMember2Contents(members);

        members = service.getAllMembers(calendar2014_5_5);
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

}
