package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import eu.x_road.xsd.xroad.ClientType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ClientActor extends UntypedActor {

    private final LoggingAdapter log = Logging
        .getLogger(getContext().system(), "ClientActor");

    @Autowired
    protected CatalogService catalogService;

    @Override
    public void onReceive(Object message) throws Exception {

        log.info("onReceive {}",message.toString());
        ClientType clientType = (ClientType)message;

        Member member = new Member();
        member.setName(clientType.getName());
        member.setMemberCode(clientType.getId().getMemberCode());
        member.setMemberClass(clientType.getId().getMemberClass());

        member = catalogService.saveMember(member);

        log.info("Member {} saved", member);

    }
}
