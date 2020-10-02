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
public class ServiceData implements Serializable {

    private static final long serialVersionUID = 4049861366368046285L;

    private LocalDateTime created;

    private String serviceCode;

    private String serviceVersion;
}
