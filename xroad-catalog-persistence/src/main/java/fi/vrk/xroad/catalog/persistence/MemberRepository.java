package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

public interface MemberRepository extends CrudRepository<Member, Long> {

    @EntityGraph(value = "member.all-but-wsdl-tree.graph",
            type = EntityGraph.EntityGraphType.FETCH)
    Set<Member> findAll();

    @EntityGraph(value = "member.all-but-wsdl-tree.graph",
            type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT m FROM Member m WHERE m.statusInfo.removed IS NULL")
    Set<Member> findAllActive();

    // uses named query Member.findAllChangedSince
    Set<Member> findAllChangedSince(@Param("since") LocalDateTime since);

    // uses named query Member.findActiveChangedSince
    Set<Member> findActiveChangedSince(@Param("since") LocalDateTime since);

    /**
     * Returns only active items (non-deleted)
     * @param xRoadInstance
     * @param memberClass
     * @param memberCode
     * @return
     */
    @Query("SELECT m FROM Member m WHERE m.xRoadInstance = :xRoadInstance "
            + "AND m.memberClass = :memberClass "
            + "AND m.memberCode = :memberCode "
            + "AND m.statusInfo.removed IS NULL")
    Member findByNaturalKey(@Param("xRoadInstance") String xRoadInstance,
                            @Param("memberClass") String memberClass,
                            @Param("memberCode") String memberCode);

}
