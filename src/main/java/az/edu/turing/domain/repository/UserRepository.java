package az.edu.turing.domain.repository;

import az.edu.turing.domain.entity.UserEntity;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Registered
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserEntity u INNER JOIN BookingEntity b ON b.flight.id = :flightId and b.passenger.id = u.id")
    List<UserEntity> findAllByFlightId(Long flightId);
}
