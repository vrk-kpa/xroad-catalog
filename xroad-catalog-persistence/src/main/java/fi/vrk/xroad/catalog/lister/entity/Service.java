package fi.vrk.xroad.catalog.lister.entity;

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
    // TODO: from sequence
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "SUBSYSTEM_ID")
    private Subsystem subsystem;
    private String serviceCode;
    private String serviceVersion;
    private Date created;
    private Date updated;
    private Date removed;
    @OneToOne(optional=false, mappedBy="service")
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
