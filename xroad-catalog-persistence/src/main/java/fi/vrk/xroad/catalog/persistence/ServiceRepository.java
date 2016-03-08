package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Service;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface ServiceRepository extends CrudRepository<Service, Long> {
    @Modifying
    @Query("UPDATE Service s SET s.removed = :when")
    void markAllRemoved(@Param("when") Date when);
}
