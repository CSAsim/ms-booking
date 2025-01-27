package az.edu.turing.service;

import az.edu.turing.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserEntity> findAll();
    List<UserEntity> findAllByFlightId(Long flightId); // Write @Query in left join.
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByEmail(String email);
    UserEntity create(UserEntity userEntity);
    UserEntity update(Long id, UserEntity userEntity);
    void delete(Long id); // Soft delete: Set isDeleted field to true.
    boolean existsById(Long id);
    boolean existsByEmail(String email);
}
