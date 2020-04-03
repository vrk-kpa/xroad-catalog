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

import fi.vrk.xroad.catalog.persistence.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

@Component
public class TestUtil {

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    EntityManager entityManager;

    public Optional getEntity(Iterable entities, Long l) {
        return StreamSupport.stream(entities.spliterator(), false)
                .filter(e -> getIdentifier(e).equals(l))
                .findFirst();
    }

    public Set<Long> getIds(Iterable entities) {
        Set<Long> set = new HashSet<Long>();
        for (Object entity: entities) {
            Long id = getIdentifier(entity);
            set.add(id);
        }
        return set;
    }

    public Long getIdentifier(Object entity) {
        return (Long) entityManagerFactory.getPersistenceUnitUtil().getIdentifier(entity);
    }

    /**
     * @param day Day of month
     * @param month 1 = January
     * @param year Year
     * @return LocalDateTime with zero hours
     */
    public LocalDateTime createDate(int day, int month, int year) {
        return LocalDateTime.of(year, month, day, 0, 0, 0);
    }

    public Member createTestMember(String name) {
        LocalDateTime now = LocalDateTime.now();
        Member member = new Member();
        member.setName(name);
        member.setXRoadInstance("FI");
        member.setMemberClass("GOV");
        member.setMemberCode("code-" + name);
        member.getStatusInfo().setCreated(now);
        member.getStatusInfo().setChanged(now);

        Subsystem ss1 = createSubsystem(name, now, "ss1");
        Subsystem ss2 = createSubsystem(name, now, "ss2");
        ss1.setMember(member);
        ss2.setMember(member);
        member.setSubsystems(new HashSet<>());
        member.getAllSubsystems().add(ss1);
        member.getAllSubsystems().add(ss2);

        Service s1 = createService(name, now, "service1");
        Service s2 = createService(name, now, "service2");
        s1.setSubsystem(ss1);
        s2.setSubsystem(ss1);
        ss1.setServices(new HashSet<>());
        ss1.getAllServices().add(s1);
        ss1.getAllServices().add(s2);

        Wsdl wsdl = new Wsdl();
        s1.setWsdl(wsdl);
        wsdl.setService(s1);
        wsdl.setData("<?xml version=\"1.0\" standalone=\"no\"?><wsdl/>");
        wsdl.setExternalId("external-id-" + name);
        wsdl.getStatusInfo().setTimestampsForNew(now);

        return member;
    }

    private Service createService(String memberName, LocalDateTime d, String serviceName) {
        Service s1 = new Service();
        s1.setServiceCode(memberName + "-" + serviceName);
        s1.setServiceVersion("v1");
        s1.getStatusInfo().setTimestampsForNew(d);
        return s1;
    }

    private Subsystem createSubsystem(String memberName, LocalDateTime date, String ssName) {
        Subsystem ss1 = new Subsystem();
        ss1.setSubsystemCode(memberName + "-" + ssName);
        ss1.getStatusInfo().setTimestampsForNew(date);
        return ss1;
    }

    public Member createTestMember(String memberCode, int subsystems) {
        Member fooMember = new Member("dev-cs", "PUB", memberCode, "UnitTestMember-" + memberCode);
        fooMember.setSubsystems(new HashSet<>());
        for (int i = 0; i < subsystems; i++) {
            Subsystem subsystem1 = new Subsystem(null, "subsystem" + i);
            fooMember.getAllSubsystems().add(subsystem1);
            subsystem1.setMember(fooMember);
        }
        return fooMember;
    }

    public void shallowCopyFields(Member from, Member to) {
        // only copy simple non-jpa-magical primitive properties
        BeanUtils.copyProperties(from, to, "id", "statusInfo", "subsystems");
    }

    public void shallowCopyFields(Subsystem from, Subsystem to) {
        // only copy simple non-jpa-magical primitive properties
        BeanUtils.copyProperties(from, to, "id", "statusInfo", "member", "services");
    }

    public void shallowCopyFields(Service from, Service to) {
        // only copy simple non-jpa-magical primitive properties
        BeanUtils.copyProperties(from, to, "id", "statusInfo", "subsystem", "wsdl", "openApi");
    }

    public void entityManagerDetach(Object entity) {
        entityManager.detach(entity);
    }

    public void entityManagerFlush() {
        entityManager.flush();
    }

    public void entityManagerClear() {
        entityManager.clear();
    }

    public void assertEqualities(StatusInfo original, StatusInfo checked,
                                 boolean sameCreated, boolean sameChanged,
                                 boolean sameRemoved, boolean sameFetched) {

        assertEquals(sameCreated, Objects.equals(original.getCreated(), checked.getCreated()));
        assertEquals(sameChanged, Objects.equals(original.getChanged(), checked.getChanged()));
        assertEquals(sameRemoved, Objects.equals(original.getRemoved(), checked.getRemoved()));
        assertEquals(sameFetched, Objects.equals(original.getFetched(), checked.getFetched()));
    }

    public void assertAllSame(StatusInfo original, StatusInfo checked) {
        assertEqualities(original, checked, true, true, true, true);
    }

    public void assertFetchedIsOnlyDifferent(StatusInfo original, StatusInfo checked) {
        assertEqualities(original, checked, true, true, true, false);
    }
}
