package fi.vrk.xroad.catalog.persistence.entity;

import com.google.common.collect.ComparisonChain;
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
    // TODO: this field indicates when the data was updated, ie. it was created/updated/deleted
    // we should add one more field for "fetched", indicating when data was last read from source
    // (even if it was identical)
    // hmmm....should "updated" be "modified"? (deletion = modification, but maybe not updating?)
    private Date updated;
    private Date removed;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Subsystem> subsystems;

    // TODO: may need to clean up so that same timestamp-updating code is reused
    // in other entities
    /**
     * Updates data with values from a transient non-deleted Member object,
     * and sets all data fields accordingly
     * @param transientMember
     * @param timestamp
     */
    public void updateWithDataFrom(Member transientMember, Date timestamp) {
        assert transientMember.getRemoved() == null;
        if (!isDataIdentical(transientMember)) {
            updated = (Date) timestamp.clone();
        }
        if (removed != null) {
            // updating data for previously removed item -> updated
            updated = (Date) timestamp.clone();
        }
        name = transientMember.getName();
        removed = null;
    }

    /**
     * Sets timestamps to correct values for new instance
     */
    public void setTimestampsForNew(Date timestamp) {
        created = (Date) timestamp.clone();
        updated = (Date) timestamp.clone();
        removed = null;
    }

    /**
     * TODO: embeddable
     * @return comparable & equals-able natural key
     */
    public MemberId createKey() {
        return new MemberId(xRoadInstance, memberClass, memberCode);
    }

    public boolean isRemoved() {
        return getRemoved() != null;
    }

    /**
     * Compares objects with just the "direct payload" - not ids, references entities or timestamps
     * @param another
     * @return
     */
    private boolean isDataIdentical(Member another) {
        return ComparisonChain.start()
                .compare(this.name, another.name)
                .compare(this.xRoadInstance, another.xRoadInstance)
                .compare(this.memberClass, another.memberClass)
                .compare(this.memberCode, another.memberCode)
                .result() == 0;
    }

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


