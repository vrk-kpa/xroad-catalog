package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.lister.entity.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MemberRepository extends CrudRepository<Member, Long> {
    List<Member> findByMemberClass(String memberClass);
}
