package az.edu.turing.domain.repository;

import az.edu.turing.domain.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query(value = "SELECT b FROM BookingEntity b WHERE b.flight.id = :flightId")
    List<BookingEntity> findAllByFlightId(@Param("flightId") Long id);

    @Query(value = "SELECT b FROM BookingEntity b WHERE b.passenger.id = :passengerId")
    List<BookingEntity> findAllByPassengerId(@Param("passengerId") Long id);

}
