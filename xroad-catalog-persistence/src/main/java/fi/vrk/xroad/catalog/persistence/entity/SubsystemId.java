package fi.vrk.xroad.catalog.persistence.entity;

/**
 * Created by janne.mattila on 8.3.2016.
 */
public class SubsystemId extends MemberId {
    private String subsystemCode;

    public SubsystemId(String xRoadInstance, String memberClass, String memberCode, String subsystemCode) {
        super(xRoadInstance, memberClass, memberCode);
        this.subsystemCode = subsystemCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SubsystemId that = (SubsystemId) o;

        return subsystemCode.equals(that.subsystemCode);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + subsystemCode.hashCode();
        return result;
    }
}
