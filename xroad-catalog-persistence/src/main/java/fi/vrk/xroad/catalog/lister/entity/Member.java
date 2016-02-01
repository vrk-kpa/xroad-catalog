package fi.vrk.xroad.catalog.lister.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @ToString(exclude = "subsystems")
/*
 Entity graph defining tree member-subsystem-service.
 */
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
public class Member {

    // TODO: from sequence
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String xRoadInstance;
    private String memberClass;
    private String memberCode;
    private String name;
    private Date created;
    private Date updated;
    private Date removed;
    @OneToMany(mappedBy = "member")
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


