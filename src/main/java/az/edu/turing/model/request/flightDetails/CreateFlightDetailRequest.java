package az.edu.turing.model.request.flightDetails;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFlightDetailRequest {

    @NotBlank
    private String airlineName;

    @NotBlank
    private String planeModel;

    private Double maxWeight;

    @Min(1)
    private Long flightId;
}
