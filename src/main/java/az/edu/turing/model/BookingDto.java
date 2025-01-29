package az.edu.turing.model;

import az.edu.turing.model.enums.StatusMessage;
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
    private long passengerId;
    private long seatNumber;
    private StatusMessage bookingStatus;
    private double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
