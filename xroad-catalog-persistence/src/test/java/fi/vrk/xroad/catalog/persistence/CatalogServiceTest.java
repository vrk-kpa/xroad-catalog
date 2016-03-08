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

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@Transactional
@Slf4j
public class CatalogServiceTest {

    @Autowired
    CatalogService catalogService;

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
        Member fooMember = new Member("dev-cs", "PUB", "333111", "UnitTestMember");
        Subsystem subsystem1 = new Subsystem(null, "subsystem1");
        Subsystem subsystem2 = new Subsystem(null, "subsystem2");
        fooMember.setSubsystems(Sets.newHashSet(subsystem1, subsystem2));
        subsystem1.setMember(fooMember);
        subsystem2.setMember(fooMember);
        List<Member> members = Lists.newArrayList(fooMember);
        catalogService.saveAllMembersAndSubsystems(members);

        // before tests: 7 members 9 subsystems (when REMOVED-status is not taken into account)
        assertEquals(8, Iterables.size(catalogService.getMembers()));
        Iterable<Member> mmm = catalogService.getMembers();
        long number = StreamSupport.stream(catalogService.getMembers().spliterator(), false)
                .filter(m -> !m.getStatusInfo().isRemoved())
                .count();

        assertEquals(12, Iterables.size(subsystemRepository.findAll()));

        // TODO: findAll() method with REMOVED = FALSE, and member.getSubsystems / REMOVED = FALSE
        assertEquals(1, StreamSupport.stream(catalogService.getMembers().spliterator(), false)
                .filter(m -> !m.getStatusInfo().isRemoved())
                .count());
        assertEquals(2, StreamSupport.stream(subsystemRepository.findAll().spliterator(), false)
                .filter(s -> !s.getStatusInfo().isRemoved())
                .count());
    }


    @Test
    public void testInsertMultipleMembersAndSubsystems() {
        throw new RuntimeException("not done yet");
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
        throw new RuntimeException("not done yet");
    }

    @Test
    public void testGetMembersSince() {
        Iterable<Member> members = catalogService.getMembers(testUtil.createDate(1, 1, 2000));
        log.info("found members: " + testUtil.getIds(members));
        assertEquals(7, Iterables.size(members));
        Optional optionalMember = testUtil.getEntity(members, 1L);
        assertTrue(optionalMember.isPresent());
        Member member = (Member) optionalMember.get();
        log(member);
        assertEquals(2, member.getSubsystems().size());
    }

    private void log(Member member) {
        log.info("************************** member with id: " + member.getId());
        log.info(member.toString());
        log.info("subsystems");
        for (Subsystem subs : member.getSubsystems()) {
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
