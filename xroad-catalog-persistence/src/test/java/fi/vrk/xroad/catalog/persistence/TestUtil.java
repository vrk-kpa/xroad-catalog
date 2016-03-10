package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

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
     * @param day
     * @param month 1 = January
     * @param year
     * @return
     */
    public Date createDate(int day, int month, int year) {
        Date date = Date.from(LocalDateTime.of(year, month, day, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    public Member createTestMember(String name) {
        Date now = new Date();
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
        member.getActiveSubsystems().add(ss1);
        member.getActiveSubsystems().add(ss2);

        Service s1 = createService(name, now, "service1");
        Service s2 = createService(name, now, "service2");
        s1.setSubsystem(ss1);
        s2.setSubsystem(ss1);
        ss1.setServices(new HashSet<>());
        ss1.getServices().add(s1);
        ss1.getServices().add(s2);

        Wsdl wsdl = new Wsdl();
        s1.setWsdl(wsdl);
        wsdl.setService(s1);
        wsdl.setData("<?xml version=\"1.0\" standalone=\"no\"?><wsdl/>");
        wsdl.setDataHash("foohash");
        wsdl.setExternalId("external-id-" + name);
        wsdl.getStatusInfo().setTimestampsForNew(now);

        return member;
    }

    private Service createService(String memberName, Date d, String serviceName) {
        Service s1 = new Service();
        s1.setServiceCode(memberName + "-" + serviceName);
        s1.setServiceVersion("v1");
        s1.getStatusInfo().setTimestampsForNew(d);
        return s1;
    }

    private Subsystem createSubsystem(String memberName, Date date, String ssName) {
        Subsystem ss1 = new Subsystem();
        ss1.setSubsystemCode(memberName + "-" + ssName);
        ss1.getStatusInfo().setTimestampsForNew(date);
        return ss1;
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

}
