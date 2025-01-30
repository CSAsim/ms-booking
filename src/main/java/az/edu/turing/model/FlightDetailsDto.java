package az.edu.turing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDetailsDto {

    private Long id;

    private String airlineName;

    private String planeModel;

    private Integer capacity;

    private Long flightId;
}
