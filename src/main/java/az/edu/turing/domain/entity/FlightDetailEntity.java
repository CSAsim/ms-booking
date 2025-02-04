package az.edu.turing.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "flight_detail")
public class FlightDetailEntity extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private FlightEntity flight;

    @Column(name = "airline_name", nullable = false)
    private String airlineName;

    @Column(name = "plane_model", nullable = false)
    private String planeModel;

    @Column(name = "allowed_weight", nullable = false)
    private Double allowedWeight;


}
