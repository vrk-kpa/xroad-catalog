package fi.vrk.xroad.catalog.persistence;

import com.google.common.collect.Iterables;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@Transactional
@Slf4j
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TestUtil testUtil;

    @Test
    public void testFindByNaturalKey() {
        Member member = memberRepository.findByNaturalKey("dev-cs", "PUB", "14151328");
        assertNotNull(member);
        assertNotNull(member.getStatusInfo());
        member = memberRepository.findByNaturalKey("dev-cs-NOT-EXISTING", "PUB", "14151328");
        assertNull(member);
    }

    @Test
    public void testRemoveAll() {
        Date now = new Date();
        memberRepository.deleteAll();
        Iterable<Member> members = memberRepository.findAll();
        for (Member member: members) {
            assertEquals(now, member.getStatusInfo().getRemoved());
        }
    }

    @Test
    public void testFindAll() {
        Iterable<Member> members = memberRepository.findAll();
        assertEquals(8, Iterables.size(members));

        members = memberRepository.findAllActive();
        assertEquals(7, Iterables.size(members));
    }

    @Test
    public void testAddMember() {
        String name = "memberx";
        Member member = createTestMember(name);
        memberRepository.save(member);

        Member peek = memberRepository.findByNaturalKey
                (member.getXRoadInstance(),
                        member.getMemberClass(),
                        member.getMemberCode());
        assertNotNull(peek);
        assertNotNull(peek.getStatusInfo());

        Iterable<Member> members = memberRepository.findAllActive();
        assertEquals(8, Iterables.size(members));
        log.info("created member with id=" + member.getId());

        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        Member savedRead = memberRepository.findByNaturalKey
                (member.getXRoadInstance(),
                member.getMemberClass(),
                member.getMemberCode());
        assertNotNull(savedRead);
        assertEquals(2, savedRead.getActiveSubsystems().size());
    }

    private Member createTestMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setXRoadInstance("xroadinstance-" + name);
        member.setMemberClass("mclass-" + name);
        member.setMemberCode("mcode-" + name);

        Subsystem ss1 = new Subsystem();
        Subsystem ss2 = new Subsystem();
        ss1.setMember(member);
        ss2.setMember(member);
        ss1.getStatusInfo().setTimestampsForNew(new Date());
        ss2.getStatusInfo().setTimestampsForNew(new Date());
        member.setSubsystems(new HashSet<>());
        member.getAllSubsystems().add(ss1);
        member.getAllSubsystems().add(ss2);
        ss1.setSubsystemCode(name + "ss1");
        ss1.setSubsystemCode(name + "ss2");

        Service s1 = new Service();
        Service s2 = new Service();
        s1.setSubsystem(ss1);
        s2.setSubsystem(ss1);
        ss2.setServices(new HashSet<>());
        ss2.getAllServices().add(s1);
        ss2.getAllServices().add(s2);
        s1.setServiceCode(name + "service1");
        s2.setServiceCode(name + "service2");

        Wsdl wsdl = new Wsdl();
        s1.setWsdl(wsdl);
        wsdl.setService(s1);
        wsdl.setData("<?xml version=\"1.0\" standalone=\"no\"?><wsdl/>");

        return member;
    }

    @Test
    public void testModified() {
        Date changedSince = testUtil.createDate(1, 1, 2017);
        Iterable<Member> members = memberRepository.findActiveChangedSince(changedSince);
        // members 3-7 have been changed since 1/1/2017,
        // 3-6 have different parts changed, #7 has all parts changed
        log.info("found changed members with ids: " + testUtil.getIds(members));
        assertEquals(5, Iterables.size(members));
        Set ids = testUtil.getIds(members);
        assertTrue(ids.containsAll(Arrays.asList(3L,4L,5L,6L,7L)));
        log.info("found members:");
        for (Member member: members) {
            log(member);
        }
        // verify that member #7 was fetched correctly with all the bits and pieces
        Optional optionalMember = testUtil.getEntity(members, 7L);
        assertTrue(optionalMember.isPresent());
        Member member7 = (Member) optionalMember.get();
        assertEquals(3, member7.getActiveSubsystems().size());
        ArrayList<Service> allServices7 = new ArrayList<>();
        for (Subsystem s: member7.getActiveSubsystems()) {
            allServices7.addAll(s.getAllServices());
        }
        // TODO: create and test getAllServices() / getActiveServices()
        assertEquals(5, allServices7.size());
        ArrayList<Wsdl> allWsdls7 = new ArrayList<>();
        for (Service s: allServices7) {
            if (s.getWsdl() != null) {
                allWsdls7.add(s.getWsdl());
            }
        }
        // TODO: test also for active / removed wsdls
        // test data:
        // member (7) -> subsystem (8) -> service (6) -> wsdl (4)
        // member (7) -> subsystem (8) -> service (8, removed) -> wsdl (6)
        // member (7) -> subsystem (8) -> service (9, removed) -> wsdl (7, removed)
        // member (7) -> subsystem (9) -> service (7) -> wsdl (5)
        assertEquals(4, allWsdls7.size());
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
                log.info(service.getWsdl() == null ? "null" : service.getWsdl().toString());
            }
        }
    }

}