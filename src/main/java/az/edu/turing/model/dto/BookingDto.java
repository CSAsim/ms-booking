package az.edu.turing.model.dto;

import az.edu.turing.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Long flightId;
    private Long userId;
    private Integer seatNumber;
    private BookingStatus bookingStatus;
    private Double totalAmount;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
}
