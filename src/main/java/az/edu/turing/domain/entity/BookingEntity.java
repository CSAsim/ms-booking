package az.edu.turing.domain.entity;

import az.edu.turing.enums.StatusMessage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class BookingEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false, referencedColumnName = "id")
    private FlightEntity flight;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private UserEntity passenger;

    @Column(name = "seat_number", nullable = false)
    private String seatNumbers;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private StatusMessage bookingStatus;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;
}
