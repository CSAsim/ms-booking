package az.edu.turing.model.request.flightDetails;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFlightDetailsRequest {

    @NotBlank
    private String airlineName;

    @NotBlank
    private String planeModel;

    @NotBlank
    private Integer capacity;

}
