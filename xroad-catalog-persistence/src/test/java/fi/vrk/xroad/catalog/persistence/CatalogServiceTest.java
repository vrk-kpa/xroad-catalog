package fi.vrk.xroad.catalog.persistence;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fi.vrk.xroad.catalog.persistence.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@Transactional
@Slf4j
public class CatalogServiceTest {

    /**
     * item counts for test data
     */
    public static final int TEST_DATA_MEMBERS = 8;
    public static final int TEST_DATA_ACTIVE_MEMBERS = 7;
    public static final int TEST_DATA_SUBSYSTEMS = 12;
    public static final int TEST_DATA_ACTIVE_SUBSYSTEMS = 10;
    @Autowired
    CatalogService catalogService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SubsystemRepository subsystemRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    WsdlRepository wsdlRepository;

    @Autowired
    TestUtil testUtil;

    @Test
    public void testGetWsdl() {
        // TODO: UNIQUE external_id
        Wsdl wsdl = catalogService.getWsdl("1000");
        assertNotNull(wsdl);
        assertEquals("<?xml version=\"1.0\" standalone=\"no\"?><wsdl-6-1-1-1-changed/>", wsdl.getData());
        assertEquals("456efg", wsdl.getDataHash());
        assertEquals(7, wsdl.getService().getSubsystem().getId());
    }

    @Test
    public void testInsertNewMemberAndSubsystems() {

        assertMemberAndSubsystemCounts(TEST_DATA_MEMBERS,
                TEST_DATA_ACTIVE_MEMBERS,
                TEST_DATA_SUBSYSTEMS,
                TEST_DATA_ACTIVE_SUBSYSTEMS);

        Member fooMember = new Member("dev-cs", "PUB", "333111", "UnitTestMember");
        Subsystem subsystem1 = new Subsystem(null, "subsystem1");
        Subsystem subsystem2 = new Subsystem(null, "subsystem2");
        fooMember.setSubsystems(Sets.newHashSet(subsystem1, subsystem2));
        subsystem1.setMember(fooMember);
        subsystem2.setMember(fooMember);
        List<Member> members = Lists.newArrayList(fooMember);
        catalogService.saveAllMembersAndSubsystems(members);

        assertMemberAndSubsystemCounts(TEST_DATA_MEMBERS + 1,
                1, TEST_DATA_SUBSYSTEMS + 2, 2);
    }


    @Test
    public void testInsertMultipleMembersAndSubsystems() {
        assertMemberAndSubsystemCounts(TEST_DATA_MEMBERS,
                TEST_DATA_ACTIVE_MEMBERS,
                TEST_DATA_SUBSYSTEMS,
                TEST_DATA_ACTIVE_SUBSYSTEMS);

        int subsystemsPerMember = 3;
        List<Member> members = Lists.newArrayList();
        members.add(createTestMember("200", subsystemsPerMember));
        members.add(createTestMember("201", subsystemsPerMember));
        members.add(createTestMember("202", subsystemsPerMember));
        catalogService.saveAllMembersAndSubsystems(members);
        int createdMembers = 3;
        int createdSubsystems = createdMembers * subsystemsPerMember;

        assertMemberAndSubsystemCounts(TEST_DATA_MEMBERS + createdMembers,
                createdMembers, TEST_DATA_SUBSYSTEMS + createdSubsystems, createdSubsystems);
    }

    private Member createTestMember(String memberCode, int subsystems) {
        Member fooMember = new Member("dev-cs", "PUB", memberCode, "UnitTestMember-" + memberCode);
        fooMember.setSubsystems(new HashSet<>());
        for (int i = 0; i < subsystems; i++) {
            Subsystem subsystem1 = new Subsystem(null, "subsystem" + i);
            fooMember.getAllSubsystems().add(subsystem1);
            subsystem1.setMember(fooMember);
        }
        return fooMember;
    }

    private void assertMemberAndSubsystemCounts(int members, int activeMembers, int subsystems, int activeSubsystems) {
        assertEquals(members, Iterables.size(catalogService.getAllMembers()));
        assertEquals(activeMembers, Iterables.size(catalogService.getActiveMembers()));
        assertEquals(subsystems, Iterables.size(subsystemRepository.findAll()));
        assertEquals(activeSubsystems, StreamSupport.stream(subsystemRepository.findAll().spliterator(), false)
                .filter(s -> !s.getStatusInfo().isRemoved())
                .count());
    }

    private void shallowCopyFields(Member from, Member to) {
        // only copy simple non-jpa-magical primitive properties
        BeanUtils.copyProperties(from, to, "id", "statusInfo", "subsystems");
    }

    private void shallowCopyFields(Subsystem from, Subsystem to) {
        // only copy simple non-jpa-magical primitive properties
        BeanUtils.copyProperties(from, to, "id", "statusInfo", "member", "services");
    }

    private void shallowCopyFields(Service from, Service to) {
        // only copy simple non-jpa-magical primitive properties
        BeanUtils.copyProperties(from, to, "id", "statusInfo", "subsystem", "wsdl");
    }

    @Test
    public void testMissingSubsystemIsRemoved() {
        // member 1: subsystems 1,2 (active) and 12 (removed)
        Member original = memberRepository.findOne(1L);
        Subsystem ss1original = subsystemRepository.findOne(1L);
        Subsystem ss2original = subsystemRepository.findOne(2L);
        Subsystem ss12original = subsystemRepository.findOne(12L);
        ss1original.getAllServices().size();
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        // save m1-(ss2) -> ss 1 and 12 are removed
        Member savedMember = new Member();
        shallowCopyFields(original, savedMember);
        Subsystem ss2saved = new Subsystem();
        shallowCopyFields(ss2original, ss2saved);
        ss2saved.setMember(savedMember);
        savedMember.getAllSubsystems().add(ss2saved);

        catalogService.saveAllMembersAndSubsystems(Lists.newArrayList(savedMember));
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        Member checkedMember = memberRepository.findOne(1L);
        assertNotNull(checkedMember);
        assertFetchedIsOnlyDifferent(original.getStatusInfo(), checkedMember.getStatusInfo());
        assertEquals(Arrays.asList(2L),
                new ArrayList<Long>(testUtil.getIds(checkedMember.getActiveSubsystems())));
        assertEquals(Arrays.asList(1L, 2L, 12L),
                new ArrayList<Long>(testUtil.getIds(checkedMember.getAllSubsystems())));

        // save m1-(ss2,ss12) -> ss 1 is removed
        savedMember = new Member();
        shallowCopyFields(original, savedMember);
        ss2saved = new Subsystem();
        shallowCopyFields(ss2original, ss2saved);
        Subsystem ss12saved = new Subsystem();
        shallowCopyFields(ss12original, ss12saved);
        ss2saved.setMember(savedMember);
        ss12saved.setMember(savedMember);
        savedMember.getAllSubsystems().add(ss2saved);
        savedMember.getAllSubsystems().add(ss12saved);

        catalogService.saveAllMembersAndSubsystems(Lists.newArrayList(savedMember));
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        checkedMember = memberRepository.findOne(1L);
        assertNotNull(checkedMember);
        assertFetchedIsOnlyDifferent(original.getStatusInfo(), checkedMember.getStatusInfo());
        assertEquals(Arrays.asList(1L, 2L, 12L),
                new ArrayList<Long>(testUtil.getIds(checkedMember.getAllSubsystems())));
        assertEquals(Arrays.asList(2L, 12L),
                new ArrayList<Long>(testUtil.getIds(checkedMember.getActiveSubsystems())));

        // save m1-(ss1,ss2,ss3) -> ss 1,2,12 are all active
        // the service & wsdl entities should NOT have been modified by the deletes
        savedMember = new Member();
        shallowCopyFields(original, savedMember);
        ss2saved = new Subsystem();
        shallowCopyFields(ss2original, ss2saved);
        ss12saved = new Subsystem();
        shallowCopyFields(ss12original, ss12saved);
        Subsystem ss1saved = new Subsystem();
        shallowCopyFields(ss1original, ss1saved);
        ss1saved.setMember(savedMember);
        ss2saved.setMember(savedMember);
        ss12saved.setMember(savedMember);
        savedMember.getAllSubsystems().add(ss1saved);
        savedMember.getAllSubsystems().add(ss2saved);
        savedMember.getAllSubsystems().add(ss12saved);

        catalogService.saveAllMembersAndSubsystems(Lists.newArrayList(savedMember));
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        checkedMember = memberRepository.findOne(1L);
        assertNotNull(checkedMember);
        assertFetchedIsOnlyDifferent(original.getStatusInfo(), checkedMember.getStatusInfo());
        assertEquals(Arrays.asList(1L, 2L, 12L),
                new ArrayList<Long>(testUtil.getIds(checkedMember.getAllSubsystems())));
        assertEquals(Arrays.asList(1L, 2L, 12L),
                new ArrayList<Long>(testUtil.getIds(checkedMember.getActiveSubsystems())));

        assertEquals(1, ss1original.getAllServices().size());
        Subsystem ss1now = subsystemRepository.findOne(1L);
        assertEquals(1, ss1now.getAllServices().size());
        Service originalService = ss1original.getAllServices().iterator().next();
        Service currentService = ss1now.getAllServices().iterator().next();
        assertAllSame(originalService.getStatusInfo(), currentService.getStatusInfo());
        Wsdl originalWsdl = originalService.getWsdl();
        Wsdl currentWsdl = currentService.getWsdl();
        assertAllSame(originalWsdl.getStatusInfo(), currentWsdl.getStatusInfo());
    }

    private void assertFetchedIsOnlyDifferent(StatusInfo original, StatusInfo checked) {
        assertEqualities(original, checked, true, true, true, false);
    }

    private void assertAllSame(StatusInfo original, StatusInfo checked) {
        assertEqualities(original, checked, true, true, true, true);
    }

    private void assertEqualities(StatusInfo original, StatusInfo checked,
                                  boolean sameCreated, boolean sameChanged,
                                  boolean sameRemoved, boolean sameFetched) {

        assertEquals(sameCreated, Objects.equals(original.getCreated(), checked.getCreated()));
        assertEquals(sameChanged, Objects.equals(original.getChanged(), checked.getChanged()));
        assertEquals(sameRemoved, Objects.equals(original.getRemoved(), checked.getRemoved()));
        assertEquals(sameFetched, Objects.equals(original.getFetched(), checked.getFetched()));
    }


    @Test
    public void testMemberIsChangedOnlyWhenNameIsChanged() {
        Member member1 = memberRepository.findOne(1L);
        Date changed = member1.getStatusInfo().getChanged();

        String oldName = "Nahka-Albert";
        String modifiedName = "Viskoosi-Jooseppi";
        Member updateToSameName = new Member();
        updateToSameName.setXRoadInstance(member1.getXRoadInstance());
        updateToSameName.setMemberClass(member1.getMemberClass());
        updateToSameName.setMemberCode(member1.getMemberCode());
        updateToSameName.setName(oldName);
        updateToSameName.setSubsystems(new HashSet<>());
        catalogService.saveAllMembersAndSubsystems(Arrays.asList(updateToSameName));

        Member member2 = memberRepository.findOne(1L);
        assertEquals(changed, member2.getStatusInfo().getChanged());

        Member updateToDifferentName = new Member();
        updateToDifferentName.setXRoadInstance(member1.getXRoadInstance());
        updateToDifferentName.setMemberClass(member1.getMemberClass());
        updateToDifferentName.setMemberCode(member1.getMemberCode());
        updateToDifferentName.setName(modifiedName);
        updateToDifferentName.setSubsystems(new HashSet<>());
        catalogService.saveAllMembersAndSubsystems(Arrays.asList(updateToDifferentName));

        Member member3 = memberRepository.findOne(1L);
        assertNotEquals(changed, member3.getStatusInfo().getChanged());
    }

    @Test
    public void testGetActiveMembersSince() {
        // all non-deleted members that contain parts that were modified since 1.1.2007 (3-7)
        Iterable<Member> members = catalogService.getActiveMembers(testUtil.createDate(1, 1, 2017));
        log.info("found members: " + testUtil.getIds(members));
        assertEquals(Arrays.asList(3L,4L,5L,6L,7L),
                new ArrayList<>(testUtil.getIds(members)));
    }

    @Test
    public void testGetAllMembersSince() {
        // all members that contain parts that were modified since 1.1.2007 (3-8)
        Iterable<Member> members = catalogService.getAllMembers(testUtil.createDate(1, 1, 2017));
        log.info("found members: " + testUtil.getIds(members));
        assertEquals(Arrays.asList(3L,4L,5L,6L,7L,8L),
                new ArrayList<Long>(testUtil.getIds(members)));
    }

    @Test
    public void testGetAllMembers() {
        Iterable<Member> members = catalogService.getAllMembers();
        assertEquals(Arrays.asList(1L, 2L, 3L,4L,5L,6L,7L,8L),
                new ArrayList<Long>(testUtil.getIds(members)));
    }

    @Test
    public void testGetActiveMembers() {
        Iterable<Member> members = catalogService.getActiveMembers();
        assertEquals(Arrays.asList(1L, 2L, 3L,4L,5L,6L,7L),
                new ArrayList<Long>(testUtil.getIds(members)));
    }

    @Test
    public void testSaveUnmodifiedServices() {
        // test data:
        // member (7) -> subsystem (8) -> service (6) -> wsdl (4)
        // member (7) -> subsystem (8) -> service (8, removed) -> wsdl (6)
        // member (7) -> subsystem (8) -> service (9, removed) -> wsdl (7, removed)
        Subsystem originalSub = subsystemRepository.findOne(8L);
        Member originalMember = originalSub.getMember();
        Service originalService5 = serviceRepository.findOne(5L);
        Service originalService6 = serviceRepository.findOne(6L);
        Service originalRemovedService8 = serviceRepository.findOne(8L);
        Service originalRemovedService9 = serviceRepository.findOne(9L);
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        Subsystem savedSub = new Subsystem();
        shallowCopyFields(originalSub, savedSub);
        savedSub.setMember(originalMember);
        Service savedService5 = new Service();
        Service savedService6 = new Service();
        shallowCopyFields(originalService5, savedService5);
        shallowCopyFields(originalService6, savedService6);

        catalogService.saveServices(savedSub, Lists.newArrayList(savedService5, savedService6));
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        // read back and verify
        Subsystem checkedSub = subsystemRepository.findOne(8L);
        assertAllSame(originalSub.getStatusInfo(), checkedSub.getStatusInfo());
        Service checkedService5 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 5L).get();
        Service checkedService6 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 6L).get();
        Service checkedService8 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 8L).get();
        Service checkedService9 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 9L).get();

        assertFalse(checkedService5.getStatusInfo().isRemoved());
        assertFalse(checkedService6.getStatusInfo().isRemoved());
        assertTrue(checkedService8.getStatusInfo().isRemoved());
        assertTrue(checkedService9.getStatusInfo().isRemoved());
        assertFetchedIsOnlyDifferent(originalService5.getStatusInfo(), checkedService5.getStatusInfo());
        assertFetchedIsOnlyDifferent(originalService6.getStatusInfo(), checkedService6.getStatusInfo());
        assertAllSame(originalRemovedService8.getStatusInfo(), checkedService8.getStatusInfo());
        assertAllSame(originalRemovedService9.getStatusInfo(), checkedService9.getStatusInfo());
    }

    @Test
    public void testSaveAddedServices() {
        // test data:
        // member (7) -> subsystem (8) -> service (6) -> wsdl (4)
        // member (7) -> subsystem (8) -> service (8, removed) -> wsdl (6)
        // member (7) -> subsystem (8) -> service (9, removed) -> wsdl (7, removed)
        Subsystem originalSub = subsystemRepository.findOne(8L);
        Member originalMember = originalSub.getMember();
        Service originalService5 = serviceRepository.findOne(5L);
        Service originalService6 = serviceRepository.findOne(6L);
        Service originalRemovedService8 = serviceRepository.findOne(8L);
        Service originalRemovedService9 = serviceRepository.findOne(9L);
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        Subsystem savedSub = new Subsystem();
        shallowCopyFields(originalSub, savedSub);
        savedSub.setMember(originalMember);
        Service savedService5 = new Service();
        Service savedService6 = new Service();
        shallowCopyFields(originalService5, savedService5);
        shallowCopyFields(originalService6, savedService6);
        Service newService = new Service();
        newService.setServiceCode("foocode-asddsa-ads");
        newService.setServiceVersion("v6");

        catalogService.saveServices(savedSub,
                Lists.newArrayList(savedService5, savedService6,newService));
        testUtil.entityManagerFlush();
        long newId = newService.getId();
        testUtil.entityManagerClear();

        // read back and verify
        Subsystem checkedSub = subsystemRepository.findOne(8L);
        assertAllSame(originalSub.getStatusInfo(), checkedSub.getStatusInfo());
        Service checkedService5 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 5L).get();
        Service checkedService6 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 6L).get();
        Service checkedService8 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 8L).get();
        Service checkedService9 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 9L).get();
        Service checkedNewService = (Service) testUtil.getEntity(checkedSub.getAllServices(), newId).get();

        assertFalse(checkedNewService.getStatusInfo().isRemoved());
        assertNotNull(checkedNewService.getStatusInfo().getFetched());
        assertNotNull(checkedNewService.getStatusInfo().getCreated());
        assertNotNull(checkedNewService.getStatusInfo().getChanged());
        assertNull(checkedNewService.getStatusInfo().getRemoved());

        assertFalse(checkedService5.getStatusInfo().isRemoved());
        assertFalse(checkedService6.getStatusInfo().isRemoved());
        assertTrue(checkedService8.getStatusInfo().isRemoved());
        assertTrue(checkedService9.getStatusInfo().isRemoved());
        assertFetchedIsOnlyDifferent(originalService5.getStatusInfo(), checkedService5.getStatusInfo());
        assertFetchedIsOnlyDifferent(originalService6.getStatusInfo(), checkedService6.getStatusInfo());
        assertAllSame(originalRemovedService8.getStatusInfo(), checkedService8.getStatusInfo());
        assertAllSame(originalRemovedService9.getStatusInfo(), checkedService9.getStatusInfo());
    }

    @Test
    public void testSaveRemovedServices() {
        // test data:
        // member (7) -> subsystem (8) -> service (6) -> wsdl (4)
        // member (7) -> subsystem (8) -> service (8, removed) -> wsdl (6)
        // member (7) -> subsystem (8) -> service (9, removed) -> wsdl (7, removed)
        Subsystem originalSub = subsystemRepository.findOne(8L);
        Member originalMember = originalSub.getMember();
        Service originalService5 = serviceRepository.findOne(5L);
        Service originalService6 = serviceRepository.findOne(6L);
        Service originalRemovedService8 = serviceRepository.findOne(8L);
        Service originalRemovedService9 = serviceRepository.findOne(9L);
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        // remove all services = save subsystem with empty services-collection
        Subsystem savedSub = new Subsystem();
        shallowCopyFields(originalSub, savedSub);
        savedSub.setMember(originalMember);

        catalogService.saveServices(savedSub,
                Lists.newArrayList());
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        // read back and verify
        Subsystem checkedSub = subsystemRepository.findOne(8L);
        assertAllSame(originalSub.getStatusInfo(), checkedSub.getStatusInfo());

        assertEquals(Arrays.asList(5L,6L,8L,9L),
                new ArrayList<>(testUtil.getIds(checkedSub.getAllServices())));
        assertTrue(checkedSub.getActiveServices().isEmpty());
        Service checkedService5 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 5L).get();
        Service checkedService6 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 6L).get();
        Service checkedService8 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 8L).get();
        Service checkedService9 = (Service) testUtil.getEntity(checkedSub.getAllServices(), 9L).get();

        assertTrue(checkedService5.getStatusInfo().isRemoved());
        assertTrue(checkedService6.getStatusInfo().isRemoved());
        assertTrue(checkedService8.getStatusInfo().isRemoved());
        assertTrue(checkedService9.getStatusInfo().isRemoved());
        assertEqualities(originalService5.getStatusInfo(), checkedService5.getStatusInfo(),
                true, false, false, false);
        assertEqualities(originalService6.getStatusInfo(), checkedService6.getStatusInfo(),
                true, false, false, false);
        assertAllSame(originalRemovedService8.getStatusInfo(), checkedService8.getStatusInfo());
        assertAllSame(originalRemovedService9.getStatusInfo(), checkedService9.getStatusInfo());
    }

    private void log(Member member) {
        log.info("************************** member with id: " + member.getId());
        log.info(member.toString());
        log.info("subsystems");
        for (Subsystem subs : member.getActiveSubsystems()) {
            log.info(subs.toString());
            log.info("services");
            for (Service service : subs.getAllServices()) {
                log.info(service.toString());
                log.info("wsdl");
                log.info(service.getWsdl().toString());
            }
        }
    }
}
