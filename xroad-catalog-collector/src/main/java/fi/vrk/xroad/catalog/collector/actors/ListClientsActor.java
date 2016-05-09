/**
 * The MIT License
 * Copyright (c) 2016, Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.ActorRef;
import fi.vrk.xroad.catalog.collector.extension.SpringExtension;
import fi.vrk.xroad.catalog.collector.util.ClientTypeUtil;
import fi.vrk.xroad.catalog.collector.wsimport.ClientListType;
import fi.vrk.xroad.catalog.collector.wsimport.ClientType;
import fi.vrk.xroad.catalog.collector.wsimport.XRoadObjectType;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.Member;
import fi.vrk.xroad.catalog.persistence.entity.MemberId;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Actor which fetches all clients, and delegates listing
 * their methods to ListMethodsActors
 */
@Component
@Scope("prototype")
@Slf4j
public class ListClientsActor extends XRoadCatalogActor {

    public static final String START_COLLECTING = "StartCollecting";

    private static AtomicInteger COUNTER = new AtomicInteger(0);

    private static final int NO_OF_ACTORS = 5;

    @Autowired
    @Qualifier("listClientsRestOperations")
    private RestOperations restOperations;

    @Autowired
    private SpringExtension springExtension;

    @Autowired
    protected CatalogService catalogService;


    @Value("${xroad-catalog.list-clients-host}")
    private String host;


    // supervisor-created pool of list methods actors
    protected ActorRef listMethodsPoolRef;

    @Override
    public void preStart() throws Exception {
        log.info("preStart {}", this.hashCode());
        listMethodsPoolRef = new RelativeActorRefUtil(getContext())
                .resolvePoolRef(Supervisor.LIST_METHODS_ACTOR_ROUTER);
        super.preStart();
    }

    @Override
    protected boolean handleMessage(Object message) throws Exception {

        if (START_COLLECTING.equals(message)) {

            String listClientsUrl = host + "/listClients";

            log.info("Getting client list from {}", listClientsUrl);
            ClientListType clientList = restOperations.getForObject(listClientsUrl, ClientListType
                    .class);

            int counter = 1;
            HashMap<MemberId, Member> m = new HashMap();

            List<Subsystem> subsystems = new ArrayList<>();
            for (ClientType clientType : clientList.getMember()) {
                log.info("{} - ClientType {}  ", counter++, ClientTypeUtil.toString(clientType));
                Member newMember = new Member(clientType.getId().getXRoadInstance(), clientType.getId()
                        .getMemberClass(),
                        clientType.getId().getMemberCode(), clientType.getName());
                newMember.setSubsystems(new HashSet<>());
                if (m.get(newMember.createKey()) == null) {
                    m.put(newMember.createKey(), newMember);
                }

                if (XRoadObjectType.SUBSYSTEM.equals(clientType.getId().getObjectType())) {
                    Subsystem newSubsystem = new Subsystem(newMember, clientType.getId().getSubsystemCode());
                    m.get(newMember.createKey()).getAllSubsystems().add(newSubsystem);
                }
            }

            // Save members
            catalogService.saveAllMembersAndSubsystems(m.values());
            for (ClientType clientType : clientList.getMember()) {
                if (XRoadObjectType.SUBSYSTEM.equals(clientType.getId().getObjectType())) {
                    listMethodsPoolRef.tell(clientType, getSelf());
                }
            }
            log.info("all clients (" + (counter-1) + ") sent to actor");
            return true;
        } else {
            return false;
        }
    }
}
