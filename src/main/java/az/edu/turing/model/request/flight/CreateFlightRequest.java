package az.edu.turing.model.request.flight;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    private String flightNumber;

    @Size(min = 0)
    private int availableSeats;
}
