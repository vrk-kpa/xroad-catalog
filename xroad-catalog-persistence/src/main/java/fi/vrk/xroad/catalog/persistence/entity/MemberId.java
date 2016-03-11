package fi.vrk.xroad.catalog.persistence.entity;

import lombok.Getter;

/**
 * Utility class to works as a key for Members in Maps etc
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberId memberId = (MemberId) o;

        if (!xRoadInstance.equals(memberId.xRoadInstance)) return false;
        if (!memberClass.equals(memberId.memberClass)) return false;
        return memberCode.equals(memberId.memberCode);
    }

    @Override
    public int hashCode() {
        int result = xRoadInstance.hashCode();
        result = 31 * result + memberClass.hashCode();
        result = 31 * result + memberCode.hashCode();
        return result;
    }

}
