package fi.vrk.xroad.catalog.persistence;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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

    // TODO: test after saveAllMembersAndSubsystems with detach / refresh

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

//    @Test
//    public void testMissingMemberIsRemoved() {
//        throw new RuntimeException("not done yet");
//    }
//
//    @Test
//    public void testMissingSubsystemIsRemoved() {
//        throw new RuntimeException("not done yet");
//    }

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
                new ArrayList<Long>(testUtil.getIds(members)));
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

    private void log(Member member) {
        log.info("************************** member with id: " + member.getId());
        log.info(member.toString());
        log.info("subsystems");
        for (Subsystem subs : member.getActiveSubsystems()) {
            log.info(subs.toString());
            log.info("services");
            for (Service service : subs.getServices()) {
                log.info(service.toString());
                log.info("wsdl");
                log.info(service.getWsdl().toString());
            }
        }
    }
}
