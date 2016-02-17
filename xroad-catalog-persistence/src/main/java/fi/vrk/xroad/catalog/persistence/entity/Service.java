package fi.vrk.xroad.catalog.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString(exclude = {"subsystem","wsdl"})
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
    private Date created;
    private Date updated;
    private Date removed;
    @OneToOne(optional=true, mappedBy="service", cascade = CascadeType.ALL)
    private Wsdl wsdl;

    public Service() {
    }

    public Service(Subsystem subsystem, String serviceCode, String serviceVersion) {
        this.subsystem = subsystem;
        this.serviceCode = serviceCode;
        this.serviceVersion = serviceVersion;
        this.created = new Date();
        this.updated = this.created;
    }

    public Service(Subsystem subsystem, String serviceCode, String serviceVersion, Date created, Date updated, Date removed) {
        this.subsystem = subsystem;
        this.serviceCode = serviceCode;
        this.serviceVersion = serviceVersion;
        this.created = created;
        this.updated = updated;
        this.removed = removed;
    }
}
