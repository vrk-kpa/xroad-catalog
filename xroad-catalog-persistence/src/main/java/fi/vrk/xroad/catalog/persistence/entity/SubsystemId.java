package fi.vrk.xroad.catalog.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Utility class to work as a key for Subsystems in Maps etc
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public class SubsystemId extends MemberId {
    private String subsystemCode;

    public SubsystemId(String xRoadInstance, String memberClass, String memberCode, String subsystemCode) {
        super(xRoadInstance, memberClass, memberCode);
        this.subsystemCode = subsystemCode;
    }
}
