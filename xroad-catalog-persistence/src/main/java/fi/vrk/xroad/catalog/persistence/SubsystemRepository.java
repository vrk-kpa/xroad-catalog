package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import org.springframework.data.repository.CrudRepository;

public interface SubsystemRepository extends CrudRepository<Subsystem, Long> {
}

