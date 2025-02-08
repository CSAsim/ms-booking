package az.edu.turing.model.request.flight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateFlightRequest {

    private String departure;
    private String destination;
    private LocalDateTime departureTime;
    private int availableSeats;
}
