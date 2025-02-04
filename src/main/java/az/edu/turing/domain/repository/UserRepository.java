package az.edu.turing.domain.repository;

import az.edu.turing.domain.entity.UserEntity;
import jdk.jfr.Registered;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Registered
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT CASE WHEN u.userRole = 'ADMIN' THEN TRUE ELSE FALSE END FROM UserEntity u WHERE u.id = :userId")
    boolean isAdmin(Long userId);

    Page<UserEntity> findAll(Pageable pageable);

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserEntity u INNER JOIN BookingEntity b ON b.flight.id = :flightId and b.user.id = u.id")
    Page<UserEntity> findAllByFlightId(Long flightId, Pageable pageable);
}
