package fi.vrk.xroad.catalog.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Identifiers for a service (within one subsystem - no identifiers for subsystem/member).
 */
@EqualsAndHashCode
@Getter
@ToString
public class ServiceId {
    private String serviceCode;
    private String serviceVersion;

    public ServiceId(String serviceCode, String serviceVersion) {
        this.serviceCode = serviceCode;
        this.serviceVersion = serviceVersion;
    }
}
