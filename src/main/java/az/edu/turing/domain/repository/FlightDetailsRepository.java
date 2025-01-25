package az.edu.turing.domain.repository;

import az.edu.turing.domain.entity.FlightDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDetailsRepository extends JpaRepository<FlightDetailsEntity, Long> {
}
