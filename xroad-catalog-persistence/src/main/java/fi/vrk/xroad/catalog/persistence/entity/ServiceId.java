package fi.vrk.xroad.catalog.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Identifiers for a service (within one subsystem - no identifiers for subsystem/member).
 */
@EqualsAndHashCode
@Getter
public class ServiceId {
    private String serviceCode;
    private String serviceVersion;

    public ServiceId(String serviceCode, String serviceVersion) {
        this.serviceCode = serviceCode;
        this.serviceVersion = serviceVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceId serviceId = (ServiceId) o;

        if (serviceCode != null ? !serviceCode.equals(serviceId.serviceCode) : serviceId.serviceCode != null)
            return false;
        return serviceVersion != null ? serviceVersion.equals(serviceId.serviceVersion) : serviceId.serviceVersion == null;

    }

    @Override
    public int hashCode() {
        int result = serviceCode != null ? serviceCode.hashCode() : 0;
        result = 31 * result + (serviceVersion != null ? serviceVersion.hashCode() : 0);
        return result;
    }
}
