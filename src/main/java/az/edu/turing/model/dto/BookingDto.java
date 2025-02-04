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

    private long id;
    private long flightId;
    private long userId;
    private long seatNumber;
    private BookingStatus bookingStatus;
    private double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
