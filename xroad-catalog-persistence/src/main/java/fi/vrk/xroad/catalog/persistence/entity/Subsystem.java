package fi.vrk.xroad.catalog.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
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
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @OneToMany(mappedBy = "subsystem", cascade = CascadeType.ALL)
    private Set<Service> services;
    @Embedded
    private StatusInfo statusInfo = new StatusInfo();

    public Subsystem() {}

    public SubsystemId createKey() {
        return new SubsystemId(
                getMember().getXRoadInstance(),
                getMember().getMemberClass(),
                getMember().getMemberCode(),
                subsystemCode);
    }

    public Subsystem(Member member, String subsystemCode) {
        this.member = member;
        this.subsystemCode = subsystemCode;
        statusInfo.setTimestampsForNew(new Date());
    }
}
