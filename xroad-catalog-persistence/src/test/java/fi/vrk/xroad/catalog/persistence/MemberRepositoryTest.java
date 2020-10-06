/**
 * The MIT License
 * Copyright (c) 2020, Population Register Centre (VRK)
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

import com.google.common.collect.Iterables;
import fi.vrk.xroad.catalog.persistence.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Slf4j
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TestUtil testUtil;

    @Test
    public void testFindByNaturalKey() {
        Member member = memberRepository.findActiveByNaturalKey("dev-cs", "PUB", "14151328");
        assertNotNull(member);
        assertNotNull(member.getStatusInfo());
        member = memberRepository.findActiveByNaturalKey("dev-cs-NOT-EXISTING", "PUB", "14151328");
        assertNull(member);
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

        Member peek = memberRepository.findActiveByNaturalKey(
                member.getXRoadInstance(),
                member.getMemberClass(),
                member.getMemberCode());
        assertNotNull(peek);
        assertNotNull(peek.getStatusInfo());

        Iterable<Member> members = memberRepository.findAllActive();
        assertEquals(8, Iterables.size(members));
        log.info("created member with id=" + member.getId());

        testUtil.entityManagerFlush();
        testUtil.entityManagerClear();

        Member savedRead = memberRepository.findActiveByNaturalKey(
                member.getXRoadInstance(),
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
        member.getStatusInfo().setTimestampsForNew(LocalDateTime.now());

        Subsystem ss1 = new Subsystem();
        Subsystem ss2 = new Subsystem();
        ss1.setSubsystemCode(name + "ss1");
        ss2.setSubsystemCode(name + "ss2");
        ss1.setMember(member);
        ss2.setMember(member);
        ss1.getStatusInfo().setTimestampsForNew(LocalDateTime.now());
        ss2.getStatusInfo().setTimestampsForNew(LocalDateTime.now());
        member.setSubsystems(new HashSet<>());
        member.getAllSubsystems().add(ss1);
        member.getAllSubsystems().add(ss2);

        Service s1 = new Service();
        Service s2 = new Service();
        s1.getStatusInfo().setTimestampsForNew(LocalDateTime.now());
        s2.getStatusInfo().setTimestampsForNew(LocalDateTime.now());
        s1.setSubsystem(ss1);
        s2.setSubsystem(ss1);
        ss2.setServices(new HashSet<>());
        ss2.getAllServices().add(s1);
        ss2.getAllServices().add(s2);
        s1.setServiceCode(name + "service1");
        s2.setServiceCode(name + "service2");

        Wsdl wsdl = new Wsdl();
        wsdl.getStatusInfo().setTimestampsForNew(LocalDateTime.now());
        s1.setWsdl(wsdl);
        wsdl.setService(s1);
        wsdl.initializeExternalId();
        wsdl.setData("<?xml version=\"1.0\" standalone=\"no\"?><wsdl/>");

        return member;
    }

    @Test
    public void testGetChangedSince() {
        LocalDateTime changedSince = testUtil.createDate(1, 1, 2017);
        Iterable<Member> members = memberRepository.findActiveChangedSince(changedSince);
        // members 3-7 have been changed since 1/1/2017,
        // 3-6 have different parts changed, #7 has all parts changed
        log.info("found changed members with ids: " + testUtil.getIds(members));
        assertEquals(5, Iterables.size(members));
        Set ids = testUtil.getIds(members);
        assertTrue(ids.containsAll(Arrays.asList(3L, 4L, 5L, 6L, 7L)));
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
        assertEquals(8, allServices7.size());
        ArrayList<Wsdl> allWsdls7 = new ArrayList<>();
        for (Service s: allServices7) {
            if (s.getWsdl() != null) {
                allWsdls7.add(s.getWsdl());
            }
        }
        // test data:
        // member (7) -> subsystem (8) -> service (6) -> wsdl (4)
        // member (7) -> subsystem (8) -> service (8, removed) -> wsdl (6)
        // member (7) -> subsystem (8) -> service (9, removed) -> wsdl (7, removed)
        // member (7) -> subsystem (8) -> service (10) -> (-)
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
