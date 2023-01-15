/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS) Copyright (c) 2016-2022 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import com.google.common.collect.Lists;
import fi.vrk.xroad.catalog.persistence.repository.MemberRepository;
import fi.vrk.xroad.catalog.persistence.repository.SubsystemRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    @BeforeAll
    public void init() {
        // member 1: subsystems 1,2 (active) and 12 (removed)
        original = memberRepository.findById(1L).get();
        ss1original = subsystemRepository.findById(1L).get();
        ss2original = subsystemRepository.findById(2L).get();
        ss12original = subsystemRepository.findById(12L).get();
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

        Member checkedMember = memberRepository.findById(1L).get();
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

        Member checkedMember = memberRepository.findById(1L).get();
        assertNotNull(checkedMember);
        testUtil.assertFetchedIsOnlyDifferent(original.getStatusInfo(), checkedMember.getStatusInfo());
        assertEquals(Arrays.asList(1L, 2L, 12L),
                new ArrayList<Long>(testUtil.getIds(checkedMember.getAllSubsystems())));
        assertEquals(Arrays.asList(1L, 2L, 12L),
                new ArrayList<Long>(testUtil.getIds(checkedMember.getActiveSubsystems())));

        assertEquals(1, ss1original.getAllServices().size());
        Subsystem ss1now = subsystemRepository.findById(1L).get();
        assertEquals(1, ss1now.getAllServices().size());
        Service currentService = ss1now.getAllServices().iterator().next();
        testUtil.assertAllSame(originalService.getStatusInfo(), currentService.getStatusInfo());
        Wsdl currentWsdl = currentService.getWsdl();
        testUtil.assertAllSame(originalWsdl.getStatusInfo(), currentWsdl.getStatusInfo());
    }

}
