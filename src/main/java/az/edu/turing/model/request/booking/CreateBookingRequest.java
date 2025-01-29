package az.edu.turing.model.request.booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {

    @NotBlank
    private Long flightId;

    @NotBlank
    private Long passengerId;

    @NotBlank
    private Integer seatNumber;

    @NotBlank
    @Min(0)
    private Double totalAmount;
}
