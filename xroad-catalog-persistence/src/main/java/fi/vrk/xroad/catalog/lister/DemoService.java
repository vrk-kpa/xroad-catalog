package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.lister.entity.Member;
import fi.vrk.xroad.catalog.lister.entity.Service;
import fi.vrk.xroad.catalog.lister.entity.Subsystem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component("demoService")
@Transactional
public class DemoService {

    public void demo(MemberRepository repository) {
        // save a couple of customers
//            repository.save(new Member("FI", "GOV", "123456", "VirastoX"));
//            repository.save(new Member("FI", "GOV", "456789", "VirastoY"));
//            repository.save(new Member("FI", "PUB", "885588", "Firma1"));

        // fetch all customers
        log.info("Members found with findAll():");
        log.info("-------------------------------");
        for (Member member : repository.findAll()) {
            log(member);
        }
        log.info("");

        // fetch an individual member by ID
        Member member = repository.findOne(1L);
        log.info("Member found with findOne(1L):");
        log.info("--------------------------------");
        log.info(member.toString());
        log.info("");

        // fetch customers by last name
        log.info("Member found with findByMemberClass('GOV'):");
        log.info("--------------------------------------------");
        for (Member gov : repository.findByMemberClass("GOV")) {
            log.info(gov.toString());
        }
        log.info("");

    }

    private void log(Member member) {
        log.info(member.toString());
        log.info("subsystems");
        for (Subsystem subs: member.getSubsystems()) {
            log.info(subs.toString());
            log.info("services");
            for (Service service: subs.getServices()) {
                log.info(service.toString());
                log.info("wsdl");
                log.info(service.getWsdl().toString());
            }
        }
    }

}
