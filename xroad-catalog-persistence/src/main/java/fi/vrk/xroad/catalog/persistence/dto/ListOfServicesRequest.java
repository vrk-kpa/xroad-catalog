package fi.vrk.xroad.catalog.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class ListOfServicesRequest implements Serializable {

    private static final long serialVersionUID = 4049861366368846289L;

    private Long historyAmountInDays;

}
