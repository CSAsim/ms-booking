package az.edu.turing.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "flight_details")
public class FlightDetailsEntity extends BaseEntity {

    @Column(name = "airline_name", nullable = false)
    private String airlineName;

    @Column(name = "plane_model", nullable = false)
    private String planeModel;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @OneToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private FlightEntity flight;

}
