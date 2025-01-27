package az.edu.turing.model.request.flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFlightRequest {

    private String departure;
    private String destination;
    private LocalDateTime departureTime;
    private String flightNumber;
    private int availableSeats;
}
