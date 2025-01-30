package az.edu.turing.domain.repository;

import az.edu.turing.domain.entity.FlightDetailsEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightDetailsRepository extends JpaRepository<FlightDetailsEntity, Long> {

    @NotNull
    Page<FlightDetailsEntity> findAll(@NotNull Pageable pageable);

    @Query("SELECT fd FROM FlightDetailsEntity fd WHERE fd.flight.id = :flightId")
    Optional<FlightDetailsEntity> findByFlightId(@Param("flightId") Long flightId);

    @Query("DELETE FROM FlightDetailsEntity fd WHERE fd.flight.id = :flightId")
    void deleteByFlightId(@Param("flightId") Long flightId);
}
