package fi.vrk.xroad.catalog.persistence.repository;

import fi.vrk.xroad.catalog.persistence.entity.Street;
import org.springframework.data.repository.CrudRepository;

public interface StreetRepository extends CrudRepository<Street, Long> {
}
