package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Set;

public interface MemberRepository extends CrudRepository<Member, Long> {

    @EntityGraph(value = "member.full-tree.graph", type = EntityGraph.EntityGraphType.LOAD)
    Set<Member> findAll();

    Set<Member> findChangedSince(@Param("since") Date since);

    // TODO: index for keys for Member, and other tables too
    @Query("SELECT m FROM Member m WHERE m.xRoadInstance = :xRoadInstance "
            + "AND m.memberClass = :memberClass "
            + "AND m.memberCode = :memberCode ")
    Member findByNaturalKey(@Param("xRoadInstance") String xRoadInstance,
                            @Param("memberClass") String memberClass,
                            @Param("memberCode") String memberCode);

    // TODO: make all markAllRemoved() methods shared same code?
    // http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.single-repository-behaviour
    // "Example 23. Custom repository base class"

    @Modifying
    @Query("UPDATE Member m SET m.statusInfo.removed = :when")
    void markAllRemoved(@Param("when") Date when);
}
