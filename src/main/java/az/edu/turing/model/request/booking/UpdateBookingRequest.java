package az.edu.turing.model.request.booking;

import az.edu.turing.model.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequest {

    @NotNull
    private Integer seatNumber;

    @NotNull
    private BookingStatus bookingStatus;

    @NotNull
    private Double totalAmount;
}
