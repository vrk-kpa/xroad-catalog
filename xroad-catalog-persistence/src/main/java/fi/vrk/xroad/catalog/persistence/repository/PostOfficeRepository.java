package fi.vrk.xroad.catalog.persistence.repository;

import fi.vrk.xroad.catalog.persistence.entity.PostOffice;
import org.springframework.data.repository.CrudRepository;

public interface PostOfficeRepository extends CrudRepository<PostOffice, Long> {
}
