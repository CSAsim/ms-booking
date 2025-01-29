package az.edu.turing.model.request.booking;

import az.edu.turing.model.enums.StatusMessage;
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
public class UpdateBookingRequest {

    @NotBlank
    private Integer seatNumber;

    @NotBlank
    private StatusMessage bookingStatus;

    @NotBlank
    @Min(0)
    private Double totalAmount;
}
