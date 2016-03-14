package fi.vrk.xroad.catalog.persistence.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.Date;
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
    private String serviceCode;
    private String serviceVersion;
    @Embedded
    private StatusInfo statusInfo = new StatusInfo();
    @Getter(AccessLevel.NONE) // do not create default getter/setter, we provide a wrapper that hides the collection
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Wsdl> wsdls;

    public Service() {
    }

    public Service(Subsystem subsystem, String serviceCode, String serviceVersion) {
        this.subsystem = subsystem;
        this.serviceCode = serviceCode;
        this.serviceVersion = serviceVersion;
        statusInfo.setTimestampsForNew(new Date());
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
