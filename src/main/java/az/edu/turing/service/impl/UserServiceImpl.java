package az.edu.turing.service.impl;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.exception.AlreadyExistsException;
import az.edu.turing.exception.InvalidInputException;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.UserMapper;
import az.edu.turing.model.UserDto;
import az.edu.turing.model.request.user.CreateUserRequest;
import az.edu.turing.model.request.user.UpdateUserRequest;
import az.edu.turing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userMapper.toDto(userRepository.findAll(pageable));
    }

    @Override
    public Page<UserDto> findAllByFlightId(Long flightId, Pageable pageable) {
        checkIfFlightExists(flightId);
        return userMapper.toDto(userRepository.findAllByFlightId(flightId, pageable));
    }

    @Override
    public UserDto findById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto findByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found for email: " + email));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto create(Long userId, CreateUserRequest request) {
        validatePasswords(request);
        checkIfUserExistsByEmail(request.getEmail());

        UserEntity user = userMapper.toEntity(request);
        user.setDeleted(false);
        user.setCreatedBy(userId);
        user.setUpdatedBy(userId);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto update(Long userId, Long id, UpdateUserRequest userRequest) {
        checkIfUserExistsById(userId);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        updateUserFields(userRequest, user);
        user.setUpdatedBy(userId);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void delete(Long userId, Long id) {
        checkIfUserExistsById(userId);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        // Soft delete
        user.setDeleted(true);
        user.setUpdatedBy(userId);
        userRepository.save(user);
    }

    private void checkIfUserExistsById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found for header id: " + userId);
        }
    }

    private void checkIfUserExistsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("User with email " + email + " already exists.");
        }
    }

    private void checkIfFlightExists(Long flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw new NotFoundException("Flight not found for id:" + flightId);
        }
    }

    private void validatePasswords(CreateUserRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidInputException("Password and confirm password do not match.");
        }
    }

    private void updateUserFields(UpdateUserRequest userRequest, UserEntity user) {
        if (userRequest.getName() != null) user.setName(userRequest.getName());
        if (userRequest.getSurname() != null) user.setSurname(userRequest.getSurname());
        if (userRequest.getEmail() != null) user.setEmail(userRequest.getEmail());
        if (userRequest.getPhoneNumber() != null) user.setPhoneNumber(userRequest.getPhoneNumber());
        if (userRequest.getPassword() != null) user.setPassword(userRequest.getPassword());
        if (userRequest.getUserRole() != null) user.setUserRole(userRequest.getUserRole());
    }
}
