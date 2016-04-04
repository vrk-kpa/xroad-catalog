package fi.vrk.xroad.catalog.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString(exclude = {"services"})
@EqualsAndHashCode(exclude = {"id", "services", "statusInfo"} ) // why?
public class Subsystem {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBSYSTEM_GEN")
    @SequenceGenerator(name = "SUBSYSTEM_GEN", sequenceName = "SUBSYSTEM_ID_SEQ", allocationSize = 1)
    private long id;
    @Column(nullable = false)
    private String subsystemCode;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @Getter(AccessLevel.NONE) // do not create default getter, we provide the substitute
    @OneToMany(mappedBy = "subsystem", cascade = CascadeType.ALL)
    private Set<Service> services = new HashSet<>();
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
        statusInfo.setTimestampsForNew(LocalDateTime.now());
    }

    /**
     * Note: Read-only collection, do not use this to modify collection
     * @return
     */
    public Set<Service> getActiveServices() {
        return Collections.unmodifiableSet(services.stream()
                .filter(service -> !service.getStatusInfo().isRemoved())
                .collect(Collectors.toSet()));
    }

    /**
     * This collection can be used to add new items
     * @return
     */
    public Set<Service> getAllServices() {
        return services;
    }

}
