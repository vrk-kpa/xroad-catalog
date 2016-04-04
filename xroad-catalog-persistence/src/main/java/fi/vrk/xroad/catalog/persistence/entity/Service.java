package fi.vrk.xroad.catalog.persistence.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = {"subsystem","wsdls"})
public class Service {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SERVICE_GEN")
    @SequenceGenerator(name = "SERVICE_GEN", sequenceName = "SERVICE_ID_SEQ", allocationSize = 1)
    private long id;
    @ManyToOne
    @JoinColumn(name = "SUBSYSTEM_ID")
    private Subsystem subsystem;
    @Column(nullable = false)
    private String serviceCode;
    private String serviceVersion;
    @Embedded
    private StatusInfo statusInfo = new StatusInfo();
    // using OneToMany wsdls instead of simpler - first choice - OneToOne wsdl
    // due to OneToOne not working with lazy loading:
    // http://stackoverflow.com/questions/1444227/making-a-onetoone-relation-lazy
    // https://developer.jboss.org/wiki/SomeExplanationsOnLazyLoadingone-to-one
    @Getter(AccessLevel.NONE) // do not create default getter/setter, we provide a wrapper that hides the collection
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Wsdl> wsdls = new HashSet<>();

    public Service() {
    }

    public Service(Subsystem subsystem, String serviceCode, String serviceVersion) {
        this.subsystem = subsystem;
        this.serviceCode = serviceCode;
        this.serviceVersion = serviceVersion;
        statusInfo.setTimestampsForNew(LocalDateTime.now());
    }

    public void setWsdl(Wsdl wsdl) {
        if (wsdls == null) {
            wsdls = new HashSet<>();
        }
        wsdls.clear();
        wsdls.add(wsdl);
    }
    public Wsdl getWsdl() { return wsdls.isEmpty() ? null : wsdls.iterator().next(); }

    /**
     * @return comparable & equals-able natural key _within one subsystem_
     */
    public ServiceId createKey() {
        return new ServiceId(serviceCode, serviceVersion);
    }

}
