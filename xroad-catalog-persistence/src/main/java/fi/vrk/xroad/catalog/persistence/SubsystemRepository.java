package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface SubsystemRepository extends CrudRepository<Subsystem, Long> {
    @Modifying
    @Query("UPDATE Subsystem s SET s.statusInfo.removed = :when")
    void markAllRemoved(@Param("when") Date when);
}
