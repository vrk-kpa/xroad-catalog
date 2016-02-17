package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Set;

public interface MemberRepository extends CrudRepository<Member, Long> {

    @EntityGraph(value = "member.full-tree.graph", type = EntityGraph.EntityGraphType.LOAD)
    Set<Member> findAll();

    Set<Member> findUpdatedSince(@Param("since") Date since);

}
