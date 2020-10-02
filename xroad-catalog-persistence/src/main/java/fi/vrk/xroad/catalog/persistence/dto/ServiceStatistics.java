package fi.vrk.xroad.catalog.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class ServiceStatistics implements Serializable {

    private static final long serialVersionUID = 4049961366368846485L;

    private LocalDateTime created;

    private Long numberOfSoapServices;

    private Long numberOfRestServices;

    private Long totalNumberOfDistinctServices;

    @Override
    public String toString() {
        return "{\"created\":" + created + ",\"numberOfSoapServices\":\"" + numberOfSoapServices
                + "\",\"numberOfRestServices\":\"" + numberOfRestServices + "\",\"totalNumberOfDistinctServices\":"
                + totalNumberOfDistinctServices + "}";
    }
}
