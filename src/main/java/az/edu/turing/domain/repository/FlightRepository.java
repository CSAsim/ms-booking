package az.edu.turing.domain.repository;

import az.edu.turing.domain.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, Long>, JpaSpecificationExecutor<FlightEntity> {

    Optional<FlightEntity> findByFlightNumber(String flightNumber);

    @Modifying
    @Query("UPDATE FlightEntity f SET f.availableSeats = f.availableSeats - 1 WHERE f.id = :flightId AND " +
            "f.availableSeats > 0")
    void decrementAvailableSeats(@Param("flightId") Long flightId);

    boolean existsByFlightNumber(String flightNumber);
}
