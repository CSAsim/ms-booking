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
    List<BookingEntity> findAllByPassengerId(@Param("userId") Long id);

    @Query("SELECT b FROM BookingEntity b WHERE b.flight.flightNumber = :flightNumber")
    List<BookingEntity> findAllByFlightNumber(@Param("flightNumber") String flightNumber);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BookingEntity b WHERE b.flight.id = :flightId")
    boolean existsByFlightId(@Param("flightId") Long id);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BookingEntity b WHERE b.flight.flightNumber = :flightNumber")
    boolean existsByFlightNumber(@Param("flightNumber") String flightNumber);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BookingEntity b WHERE b.passenger.id = :passengerId")
    boolean existsByPassengerId(@Param("passengerId") Long id);

}
