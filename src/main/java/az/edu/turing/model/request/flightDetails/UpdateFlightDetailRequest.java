package az.edu.turing.model.request.flightDetails;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFlightDetailRequest {

    @NotBlank
    private String airlineName;

    @NotBlank
    private String planeModel;

    @NotNull
    private Double maxWeight;

    private Long flightId;

}
