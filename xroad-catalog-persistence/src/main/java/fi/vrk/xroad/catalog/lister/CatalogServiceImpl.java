package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.lister.entity.Member;
import fi.vrk.xroad.catalog.lister.entity.Wsdl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Component("catalogService")
@Transactional
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public Iterable<Member> getMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Iterable<Member> getMembers(Date changedAfter) {
        return memberRepository.findUpdatedSince(changedAfter);
    }

    @Override
    public Member saveMember(Member member) {
        // TODO proper merge, marking items deleted, etc. See javadoc at @Overridden method
        return memberRepository.save(member);
    }

    @Override
    public Wsdl saveWsdl(long serviceId, Wsdl wsdl) {
        return null;
    }

    @Override
    public void deleteMember(long memberId) {
    }
}
