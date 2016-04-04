package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SubsystemRepository extends CrudRepository<Subsystem, Long> {

    @Query("SELECT s FROM Subsystem s WHERE s.subsystemCode = :subsystemCode "
            + "AND s.member.xRoadInstance = :xRoadInstance "
            + "AND s.member.memberClass = :memberClass "
            + "AND s.member.memberCode = :memberCode "
            + "AND s.statusInfo.removed IS NULL")
    Subsystem findActiveByNaturalKey(@Param("xRoadInstance") String xRoadInstance,
                                     @Param("memberClass") String memberClass,
                                     @Param("memberCode") String memberCode,
                                     @Param("subsystemCode") String subsystemCode);
}

