package az.edu.turing.model.request.flight;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFlightRequest {

    @NotBlank
    private String departure;

    @NotBlank
    private String destination;
    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    @NotBlank
    private String flightNumber;

    @Min(value = 0)
    private int availableSeats;
}
