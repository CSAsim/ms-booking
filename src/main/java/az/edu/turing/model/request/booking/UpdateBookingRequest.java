package az.edu.turing.model.request.booking;

import az.edu.turing.model.enums.BookingStatus;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequest {
    @Min(0)
    private Integer seatNumber;

    private BookingStatus bookingStatus;

    @Min(0)
    private Double totalAmount;
}
