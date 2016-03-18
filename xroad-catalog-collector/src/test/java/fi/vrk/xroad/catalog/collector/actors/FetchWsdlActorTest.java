package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import fi.vrk.xroad.catalog.collector.XRoadCatalogCollector;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadServiceIdentifierType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestOperations;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(XRoadCatalogCollector.class)
@Transactional
@Slf4j
public class FetchWsdlActorTest {


    @Autowired
    ActorSystem actorSystem;

    @Autowired
    SpringExtension springExtension;

    @Test
    public void testBasicPlumbing() throws Exception {
        // just tells actor to do something
        ActorRef fetchWsdlActor = actorSystem.actorOf(springExtension.props("fetchWsdlActor"));
        XRoadServiceIdentifierType service = new XRoadServiceIdentifierType();
        service.setXRoadInstance("inst");
        service.setMemberClass("mcl");
        service.setMemberCode("mco");
        service.setSubsystemCode("sub");
        service.setServiceCode("sc");
        service.setServiceVersion("sv");
        fetchWsdlActor.tell(service, ActorRef.noSender());
    }
}
