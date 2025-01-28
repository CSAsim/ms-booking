package az.edu.turing.model.request.booking;

import az.edu.turing.model.enums.StatusMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequest {

    private int seatNumber;
    private StatusMessage bookingStatus;
    private Double totalAmount;
}
