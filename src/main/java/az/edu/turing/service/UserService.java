package az.edu.turing.service;

import az.edu.turing.model.UserDto;
import az.edu.turing.model.request.user.CreateUserRequest;
import az.edu.turing.model.request.user.UpdateUserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<UserDto> findAll(Pageable pageable);

    Page<UserDto> findAllByFlightId(Long flightId, Pageable pageable); // Write @Query in left join.

    UserDto findById(Long id);

    UserDto findByEmail(String email);

    UserDto create(Long userId, CreateUserRequest userRequest);

    UserDto update(Long userId, Long id, UpdateUserRequest userRequest);

    void delete(Long userId, Long id);
}
