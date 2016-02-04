package fi.vrk.xroad.catalog.lister.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = {"member","services"})
public class Subsystem {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBSYSTEM_GEN")
    @SequenceGenerator(name = "SUBSYSTEM_GEN", sequenceName = "SUBSYSTEM_ID_SEQ", allocationSize = 1)
    private long id;
    private String subsystemCode;
    private Date created;
    private Date updated;
    private Date removed;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @OneToMany(mappedBy = "subsystem", cascade = CascadeType.ALL)
    private Set<Service> services;

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
