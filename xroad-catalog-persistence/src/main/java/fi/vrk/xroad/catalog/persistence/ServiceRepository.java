package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ServiceRepository extends CrudRepository<Service, Long> {
    /**
     * Only returns non-removed services
     */
    @Query("SELECT s FROM Service s WHERE "
            + "s.serviceVersion = :serviceVersion "
            + "AND s.serviceCode = :serviceCode "
            + "AND s.subsystem.subsystemCode = :subsystemCode "
            + "AND s.subsystem.member.memberCode = :memberCode "
            + "AND s.subsystem.member.memberClass = :memberClass "
            + "AND s.subsystem.member.xRoadInstance = :xRoadInstance "
            + "AND s.statusInfo.removed IS NULL")
    Service findActiveByNaturalKey(@Param("xRoadInstance") String xRoadInstance,
                                   @Param("memberClass") String memberClass,
                                   @Param("memberCode") String memberCode,
                                   @Param("subsystemCode") String subsystemCode,
                                   @Param("serviceCode") String serviceCode,
                                   @Param("serviceVersion") String serviceVersion);

    /**
     * Only returns non-removed services
     */
    @Query("SELECT s FROM Service s WHERE "
            + "s.serviceVersion IS NULL "
            + "AND s.serviceCode = :serviceCode "
            + "AND s.subsystem.subsystemCode = :subsystemCode "
            + "AND s.subsystem.member.memberCode = :memberCode "
            + "AND s.subsystem.member.memberClass = :memberClass "
            + "AND s.subsystem.member.xRoadInstance = :xRoadInstance "
            + "AND s.statusInfo.removed IS NULL")
    Service findActiveNullVersionByNaturalKey(@Param("xRoadInstance") String xRoadInstance,
                                   @Param("memberClass") String memberClass,
                                   @Param("memberCode") String memberCode,
                                   @Param("subsystemCode") String subsystemCode,
                                   @Param("serviceCode") String serviceCode);
}
