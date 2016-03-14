package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * To populate database with x items, un-ignore this test & run with
 * production profile
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@Transactional
@Slf4j
public class TestDataPopulator {

    private static final int NUMBER_OF_MEMBER = 10000;

    @Autowired
    CatalogService catalogService;

    @Autowired
    TestUtil testUtil;

    @Test
    @Ignore
    @Rollback(value = false)
    public void createTestData() {
        Collection<Member> members = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_MEMBER; i++) {
            log.info("creating member {}", i);
            Member testMember = testUtil.createTestMember("testmember-" + i);
            members.add(testMember);
        }
        catalogService.saveAllMembersAndSubsystems(members);
    }

}
