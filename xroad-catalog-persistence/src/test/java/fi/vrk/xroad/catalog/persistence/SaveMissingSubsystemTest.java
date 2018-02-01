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
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
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
