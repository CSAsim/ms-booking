package az.edu.turing.service;

import az.edu.turing.model.UserDto;
import az.edu.turing.model.request.user.CreateUserRequest;
import az.edu.turing.model.request.user.UpdateUserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAll();
    List<UserDto> findAllByFlightId(Long flightId); // Write @Query in left join.
    Optional<UserDto> findById(Long id);
    Optional<UserDto> findByEmail(String email);
    UserDto create(CreateUserRequest userRequest);
    UserDto update(Long id, UpdateUserRequest userRequest);
    void delete(Long id); // Soft delete: Set isDeleted field to true.
    boolean existsById(Long id);
    boolean existsByEmail(String email);
}
