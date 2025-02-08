package az.edu.turing.domain.entity;

import az.edu.turing.model.enums.FlightStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flight")
@EqualsAndHashCode(callSuper = true)
public class FlightEntity extends BaseEntity {

    @OneToOne(mappedBy = "flight", cascade = CascadeType.ALL)
    private FlightDetailEntity flightDetails;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<BookingEntity> bookings;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "departure", nullable = false)
    private String departureLocation;

    @Column(name = "destination", nullable = false)
    private String destinationLocation;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "flight_status", nullable = false)
    private FlightStatus flightStatus;
}
