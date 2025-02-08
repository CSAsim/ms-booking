package az.edu.turing.model.request.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {

    @NotNull
    private Long flightId;

    @NotNull
    private Long passengerId;

    @NotNull
    private Integer seatNumber;

    @NotNull
    private Double totalAmount;
}
