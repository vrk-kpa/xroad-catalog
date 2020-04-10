package fi.vrk.xroad.catalog.persistence.repository;

import fi.vrk.xroad.catalog.persistence.entity.Webpage;
import org.springframework.data.repository.CrudRepository;

public interface WebpageRepository extends CrudRepository<Webpage, Long> {
}
