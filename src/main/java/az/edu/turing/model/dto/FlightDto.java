package az.edu.turing.model.dto;

import az.edu.turing.model.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDto {

    private String flightNumber;
    private String departureLocation;
    private String destinationLocation;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer availableSeats;
    private FlightStatus flightStatus;
}
