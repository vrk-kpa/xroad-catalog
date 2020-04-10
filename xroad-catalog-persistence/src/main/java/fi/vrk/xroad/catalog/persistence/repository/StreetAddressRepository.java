package fi.vrk.xroad.catalog.persistence.repository;

import fi.vrk.xroad.catalog.persistence.entity.StreetAddress;
import org.springframework.data.repository.CrudRepository;

public interface StreetAddressRepository extends CrudRepository<StreetAddress, Long> {
}
