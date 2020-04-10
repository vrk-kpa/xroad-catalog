package fi.vrk.xroad.catalog.persistence.repository;

import fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddress;
import org.springframework.data.repository.CrudRepository;

public interface PostOfficeBoxAddressRepository extends CrudRepository<PostOfficeBoxAddress, Long> {
}
