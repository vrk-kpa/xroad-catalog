package fi.vrk.xroad.catalog.collector.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class Endpoint implements Serializable {

    private static final long serialVersionUID = 4049961366399946785L;

    private String method;

    private String path;
}
