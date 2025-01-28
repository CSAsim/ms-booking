package az.edu.turing.model.request.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {

    private Long flightId;
    private Long passengerId;
    private int seatNumber;
    private Double totalAmount;
}
