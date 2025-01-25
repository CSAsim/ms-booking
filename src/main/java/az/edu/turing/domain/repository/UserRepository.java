package az.edu.turing.domain.repository;

import az.edu.turing.domain.entity.UserEntity;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
