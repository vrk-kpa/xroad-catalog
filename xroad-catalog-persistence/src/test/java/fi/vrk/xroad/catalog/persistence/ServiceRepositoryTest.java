package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Service;
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
public class ServiceRepositoryTest {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    TestUtil testUtil;

    @Test
    public void testFindByNaturalKey() {
        // member(7) -> ss (8) -> service(6) = ok [dummy-service_7-1-2/v1]
        // member(7) -> ss (8) -> service(8) = removed [removed-service_7-1-3/v1]
        // member(7) -> ss (8) -> service(???) = non-existent
        Service service = serviceRepository.findByNaturalKey("dev-cs", "PUB", "15",
                "subsystem_7-1", "dummy-service_7-1-2", "v1");
        assertNotNull(service);
        service = serviceRepository.findByNaturalKey("dev-cs", "PUB", "15",
                "subsystem_7-1", "removed-service_7-1-3", "v1");
        assertNull(service);
        service = serviceRepository.findByNaturalKey("dev-cs", "PUB", "15",
                "subsystem_7-1", "N/A-service", "v1");
        assertNull(service);
    }
}