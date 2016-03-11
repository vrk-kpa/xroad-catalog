package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@Transactional
@Slf4j
public class SubsystemRepositoryTest {

    @Autowired
    SubsystemRepository subsystemRepository;

    @Autowired
    TestUtil testUtil;

    @Test
    public void testFindByNaturalKey() {
        // 1->12 (subsystem_a3_removed) = removed, 1->1 (subsystem_a1) = ok, foo = not existing
        Subsystem subsystem = subsystemRepository.findByNaturalKey("dev-cs", "PUB", "14151328",
                "subsystem_a1");
        assertNotNull(subsystem);
        subsystem = subsystemRepository.findByNaturalKey("dev-cs", "PUB", "14151328",
                "subsystem_a3_removed");
        assertNull(subsystem);
        subsystem = subsystemRepository.findByNaturalKey("dev-cs", "PUB", "14151328",
                "N/A code");
        assertNull(subsystem);
    }
}