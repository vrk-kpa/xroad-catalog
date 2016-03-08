package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface WsdlRepository extends CrudRepository<Wsdl, Long> {
    @Modifying
    @Query("UPDATE Wsdl w SET w.removed = :when")
    void markAllRemoved(@Param("when") Date when);
}
