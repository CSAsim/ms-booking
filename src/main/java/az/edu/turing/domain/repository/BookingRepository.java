package az.edu.turing.domain.repository;

import az.edu.turing.domain.entity.BookingEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @NotNull
    Page<BookingEntity> findAll(@NotNull Pageable pageable);

    @Query(value = "SELECT b FROM BookingEntity b WHERE b.flight.id = :flightId")
    Page<BookingEntity> findAllByFlightId(@Param("flightId") Long flightId, Pageable pageable);

    @Query(value = "SELECT b FROM BookingEntity b WHERE b.passenger.id = :passengerId")
    Page<BookingEntity> findAllByPassengerId(@Param("passengerId") Long passengerId, Pageable pageable);

}
