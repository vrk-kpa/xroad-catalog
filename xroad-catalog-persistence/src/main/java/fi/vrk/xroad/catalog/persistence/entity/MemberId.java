package fi.vrk.xroad.catalog.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Utility class to work as a key for Members in Maps etc
 */
@EqualsAndHashCode
@Getter
public class MemberId {
    private String xRoadInstance;
    private String memberClass;
    private String memberCode;

    public MemberId(String xRoadInstance, String memberClass, String memberCode) {
        this.xRoadInstance = xRoadInstance;
        this.memberClass = memberClass;
        this.memberCode = memberCode;
    }
}
