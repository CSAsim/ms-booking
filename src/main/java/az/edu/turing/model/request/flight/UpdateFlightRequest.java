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
public class UpdateFlightRequest {

    @NotBlank
    private String departure;

    @NotBlank
    private String destination;
    private LocalDateTime departureTime;

    @Min(0)
    private int availableSeats;
}
