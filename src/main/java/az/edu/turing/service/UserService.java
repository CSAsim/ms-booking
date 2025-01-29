package az.edu.turing.service;

import az.edu.turing.model.UserDto;
import az.edu.turing.model.request.user.CreateUserRequest;
import az.edu.turing.model.request.user.UpdateUserRequest;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    List<UserDto> findAllByFlightId(Long flightId); // Write @Query in left join.

    UserDto findById(Long id);

    UserDto findByEmail(String email);

    UserDto create(Long userId, CreateUserRequest userRequest);

    UserDto update(Long userId, Long id, UpdateUserRequest userRequest);

    void delete(Long userId, Long id);
}
