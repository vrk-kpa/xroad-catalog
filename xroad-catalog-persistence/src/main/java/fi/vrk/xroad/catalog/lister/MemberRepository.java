package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.lister.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface MemberRepository extends CrudRepository<Member, Long> {
    List<Member> findByMemberClass(String memberClass);

    @EntityGraph(value = "member.full-tree.graph", type = EntityGraph.EntityGraphType.LOAD)
    Set<Member> findAll();
}
