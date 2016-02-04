package fi.vrk.xroad.catalog.lister.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter @Setter @ToString(exclude = "subsystems")
// Entity graph defining tree member-subsystem-service.
@NamedEntityGraph(
        name = "member.full-tree.graph",
        attributeNodes = {
                @NamedAttributeNode(value = "subsystems", subgraph = "subsystem.services.graph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name="subsystem.services.graph",
                        attributeNodes = @NamedAttributeNode(value = "services", subgraph = "service.wsdl.graph")),
                @NamedSubgraph(
                        name="service.wsdl.graph",
                        attributeNodes = @NamedAttributeNode(value = "wsdl")),
        }
)
@NamedQueries({
        // query fetches all members that have been updated, or have child entities
        // (subsystems, services, wsdls) that have been updated since given date
        @NamedQuery(name = "Member.findUpdatedSince",
                query = "SELECT DISTINCT mem " +
                        "FROM Member mem " +
                        "LEFT JOIN FETCH mem.subsystems fetchedSubs " +
                        "LEFT JOIN FETCH fetchedSubs.services fetchedSers " +
                        "LEFT JOIN FETCH fetchedSers.wsdl fetchedWsdl " +
                        "WHERE mem.updated > :since " +
                        "OR EXISTS ( " +
                        "SELECT sub " +
                        "FROM Subsystem sub " +
                        "WHERE sub.member = mem " +
                        "AND sub.updated > :since)" +
                        "OR EXISTS ( " +
                        "SELECT service " +
                        "FROM Service service " +
                        "WHERE service.subsystem.member = mem " +
                        "AND service.updated > :since)" +
                        "OR EXISTS ( " +
                        "SELECT wsdl " +
                        "FROM Wsdl wsdl " +
                        "WHERE wsdl.service.subsystem.member = mem " +
                        "AND wsdl.updated > :since)"),
})
public class Member {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_GEN")
    @SequenceGenerator(name = "MEMBER_GEN", sequenceName = "MEMBER_ID_SEQ", allocationSize = 1)
    private long id;
    private String xRoadInstance;
    private String memberClass;
    private String memberCode;
    private String name;
    private Date created;
    private Date updated;
    private Date removed;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Subsystem> subsystems;

    public Member() {}

    public Member(String xRoadInstance,
                  String memberClass,
                  String memberCode,
                  String name) {
        this.xRoadInstance = xRoadInstance;
        this.memberClass = memberClass;
        this.memberCode = memberCode;
        this.name = name;
        this.created = new Date();
        this.updated = this.created;
    }

    public Member(String xRoadInstance, String memberClass, String memberCode, String name,
                  Date created, Date updated, Date removed) {
        this.xRoadInstance = xRoadInstance;
        this.memberClass = memberClass;
        this.memberCode = memberCode;
        this.name = name;
        this.created = created;
        this.updated = updated;
        this.removed = removed;
    }

}


