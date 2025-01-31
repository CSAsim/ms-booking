package az.edu.turing.service;

import az.edu.turing.model.UserDto;
import az.edu.turing.model.request.user.CreateUserRequest;
import az.edu.turing.model.request.user.UpdateUserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserDto> findAll(Long userId, Pageable pageable);

    Page<UserDto> findAllByFlightId(Long userId, Long flightId, Pageable pageable); // Write @Query in left join.

    UserDto findById(Long userId, Long id);

    UserDto findByEmail(Long userId, String email);

    UserDto create(Long userId, CreateUserRequest userRequest);

    UserDto update(Long userId, Long id, UpdateUserRequest userRequest);

    void delete(Long userId, Long id);
}
