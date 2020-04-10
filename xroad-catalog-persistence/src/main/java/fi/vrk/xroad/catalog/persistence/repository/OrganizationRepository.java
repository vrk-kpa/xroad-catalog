package fi.vrk.xroad.catalog.persistence.repository;

import fi.vrk.xroad.catalog.persistence.entity.Organization;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {

    @EntityGraph(value = "organization.full-tree.graph",
            type = EntityGraph.EntityGraphType.FETCH)
    Set<Organization> findAll();
}
