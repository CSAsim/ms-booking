package az.edu.turing.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDetailDto {

    private Long id;

    private String airlineName;

    private String planeModel;

    private Double allowedWeight;

    private Long flightId;
}
