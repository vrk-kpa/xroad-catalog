package fi.vrk.xroad.catalog.persistence;

import com.google.common.collect.Iterables;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@Slf4j
public class CatalogServiceTest {

    @Autowired
    CatalogService catalogService;

    @Autowired
    TestUtil testUtil;

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
