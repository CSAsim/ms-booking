package az.edu.turing.domain.repository;

import az.edu.turing.domain.entity.FlightDetailEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightDetailRepository extends JpaRepository<FlightDetailEntity, Long> {

    @NotNull
    Page<FlightDetailEntity> findAll(@NotNull Pageable pageable);

    @Query("SELECT fd FROM FlightDetailEntity fd WHERE fd.flight.id = :flightId")
    Optional<FlightDetailEntity> findByFlightId(@Param("flightId") Long flightId);

    @Query("DELETE FROM FlightDetailEntity fd WHERE fd.flight.id = :flightId")
    void deleteByFlightId(@Param("flightId") Long flightId);
}
