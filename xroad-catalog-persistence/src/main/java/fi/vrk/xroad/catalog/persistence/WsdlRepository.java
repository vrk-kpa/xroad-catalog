package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import org.springframework.data.repository.CrudRepository;

public interface WsdlRepository extends CrudRepository<Wsdl, Long> {
}
