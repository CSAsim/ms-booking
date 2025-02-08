package az.edu.turing.model.request.flight;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFlightRequest {

    @NotBlank
    private String departureLocation;

    @NotBlank
    private String destinationLocation;

    private LocalDateTime departureTime;

    @NotBlank
    private String flightNumber;

    @Min(value = 0)
    private Integer availableSeats;

    @Min(value = 0)
    private Integer totalSeats;
    
    @NotBlank
    private String airlineName;

    @NotBlank
    private String planeModel;

    private Double maxWeight;
}
