package fi.vrk.xroad.catalog.persistence;

import com.google.common.collect.Lists;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@Transactional
@Slf4j
public class SaveMissingSubsystemTest {

    @Autowired
    CatalogService catalogService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SubsystemRepository subsystemRepository;

    @Autowired
    TestUtil testUtil;

    Member original;
    Subsystem ss1original;
    Subsystem ss2original;
    Subsystem ss12original;
    Service originalService;
    Wsdl originalWsdl;

    Member savedMember;
    Subsystem ss1saved;
    Subsystem ss2saved;
    Subsystem ss12saved;

    @Before
    public void init() {
        // member 1: subsystems 1,2 (active) and 12 (removed)
        original = memberRepository.findOne(1L);
        ss1original = subsystemRepository.findOne(1L);
        ss2original = subsystemRepository.findOne(2L);
        ss12original = subsystemRepository.findOne(12L);
        originalService = ss1original.getAllServices().iterator().next();
        originalWsdl = originalService.getWsdl();
        ss1original.getAllServices().size();
        // detach, so we dont modify those objects in the next steps
        testUtil.entityManagerClear();

        // prepare saved items
        savedMember = new Member();
        testUtil.shallowCopyFields(original, savedMember);
        ss1saved = new Subsystem();
        testUtil.shallowCopyFields(ss1original, ss1saved);
        ss2saved = new Subsystem();
        testUtil.shallowCopyFields(ss2original, ss2saved);
        ss12saved = new Subsystem();
        testUtil.shallowCopyFields(ss12original, ss12saved);
    }

    @Test
    public void testMissingSubsystemIsRemoved() {
        // save m1-(ss2,ss12) -> ss 1 is removed
        ss2saved.setMember(savedMember);
        ss12saved.setMember(savedMember);
        savedMember.getAllSubsystems().add(ss2saved);
        savedMember.getAllSubsystems().add(ss12saved);

        catalogService.saveAllMembersAndSubsystems(Lists.newArrayList(savedMember));
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        Member checkedMember = memberRepository.findOne(1L);
        assertNotNull(checkedMember);
        testUtil.assertFetchedIsOnlyDifferent(original.getStatusInfo(),
                checkedMember.getStatusInfo());
        assertEquals(Arrays.asList(1L, 2L, 12L),
                new ArrayList<Long>(testUtil.getIds(checkedMember.getAllSubsystems())));
        assertEquals(Arrays.asList(2L, 12L),
                new ArrayList<Long>(testUtil.getIds(checkedMember.getActiveSubsystems())));
    }
    @Test
    public void testSubsystemIsResurrected() {
        // B save m1-(ss1,ss2,ss3) -> ss 1,2,12 are all active
        // the service & wsdl entities should NOT have been modified by the deletes
        ss1saved.setMember(savedMember);
        ss2saved.setMember(savedMember);
        ss12saved.setMember(savedMember);
        savedMember.getAllSubsystems().add(ss1saved);
        savedMember.getAllSubsystems().add(ss2saved);
        savedMember.getAllSubsystems().add(ss12saved);

        catalogService.saveAllMembersAndSubsystems(Lists.newArrayList(savedMember));
        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        Member checkedMember = memberRepository.findOne(1L);
        assertNotNull(checkedMember);
        testUtil.assertFetchedIsOnlyDifferent(original.getStatusInfo(), checkedMember.getStatusInfo());
        assertEquals(Arrays.asList(1L, 2L, 12L),
                new ArrayList<Long>(testUtil.getIds(checkedMember.getAllSubsystems())));
        assertEquals(Arrays.asList(1L, 2L, 12L),
                new ArrayList<Long>(testUtil.getIds(checkedMember.getActiveSubsystems())));

        assertEquals(1, ss1original.getAllServices().size());
        Subsystem ss1now = subsystemRepository.findOne(1L);
        assertEquals(1, ss1now.getAllServices().size());
        Service currentService = ss1now.getAllServices().iterator().next();
        testUtil.assertAllSame(originalService.getStatusInfo(), currentService.getStatusInfo());
        Wsdl currentWsdl = currentService.getWsdl();
        testUtil.assertAllSame(originalWsdl.getStatusInfo(), currentWsdl.getStatusInfo());
    }

}
