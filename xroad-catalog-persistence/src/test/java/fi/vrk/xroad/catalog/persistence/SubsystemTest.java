package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@Transactional
@Slf4j
public class SubsystemTest {

    @Autowired
    SubsystemRepository subsystemRepository;

    @Autowired
    TestUtil testUtil;

    @Test
    public void testGetActiveSubsystems() {
        // 5+6 are active, 8+9 are removed
        Subsystem sub = subsystemRepository.findOne(8L);
        assertEquals(Arrays.asList(5L,6L),
                new ArrayList<Long>(testUtil.getIds(sub.getActiveServices())));
    }

    @Test
    public void testGetAllSubsystems() {
        // 5+6 are active, 8+9 are removed
        Subsystem sub = subsystemRepository.findOne(8L);
        assertEquals(Arrays.asList(5L,6L,8L,9L),
                new ArrayList<Long>(testUtil.getIds(sub.getAllServices())));
    }

}
