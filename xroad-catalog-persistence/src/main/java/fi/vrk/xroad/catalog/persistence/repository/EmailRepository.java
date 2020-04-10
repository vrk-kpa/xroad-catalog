package fi.vrk.xroad.catalog.persistence.repository;

import fi.vrk.xroad.catalog.persistence.entity.Email;
import org.springframework.data.repository.CrudRepository;

public interface EmailRepository extends CrudRepository<Email, Long> {
}
