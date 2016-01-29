package fi.vrk.xroad.catalog.lister.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"member","services"})
public class Subsystem {

    // TODO: from sequence
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    private String subsystemCode;
    private Date created;
    private Date updated;
    private Date removed;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @OneToMany(mappedBy = "subsystem")
    private List<Service> services;

    public Subsystem() {}

    public Subsystem(Member member, String subsystemCode) {
        this.member = member;
        this.subsystemCode = subsystemCode;
        this.created = new Date();
        this.updated = this.created;
    }

    public Subsystem(Member member, String subsystemCode, Date created, Date updated, Date removed) {
        this.member = member;
        this.subsystemCode = subsystemCode;
        this.created = created;
        this.updated = updated;
        this.removed = removed;
    }

}
