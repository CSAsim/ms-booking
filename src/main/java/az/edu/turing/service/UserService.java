package az.edu.turing.service;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.exception.AlreadyExistsException;
import az.edu.turing.exception.InvalidInputException;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.UserMapper;
import az.edu.turing.model.dto.UserDto;
import az.edu.turing.model.request.user.CreateUserRequest;
import az.edu.turing.model.request.user.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final UserMapper userMapper;

    public Page<UserDto> findAll(Long userId, Pageable pageable) {
        checkUserRole(userId);
        return userMapper.toDto(userRepository.findAll(pageable));
    }

    public Page<UserDto> findAllByFlightId(Long userId, Long flightId, Pageable pageable) {
        checkUserRole(userId);
        checkIfFlightExists(flightId);
        return userMapper.toDto(userRepository.findAllByFlightId(flightId, pageable));
    }

    public UserDto findById(Long userId, Long id) {
        checkUserRole(userId);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + id));
        return userMapper.toDto(user);
    }

    public UserDto findByEmail(Long userId, String email) {
        checkUserRole(userId);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found for email: " + email));
        return userMapper.toDto(user);
    }

    public UserDto create(Long userId, CreateUserRequest request) {
        validatePasswords(request);
        checkIfUserExistsByEmail(request.getEmail());

        UserEntity user = userMapper.toEntity(request);
        user.setDeleted(false);
        user.setCreatedBy(getUser(userId));
        user.setUpdatedBy(getUser(userId));
        return userMapper.toDto(userRepository.save(user));
    }

    public UserDto update(Long userId, Long id, UpdateUserRequest userRequest) {
        checkIfUserExistsById(userId);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + id));

        updateUserFields(userRequest, user);
        user.setUpdatedBy(getUser(userId));
        return userMapper.toDto(userRepository.save(user));
    }

    public void delete(Long userId, Long id) {
        checkIfUserExistsById(userId);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + id));

        // Soft delete
        user.setDeleted(true);
        user.setUpdatedBy(getUser(userId));
        userRepository.save(user);
    }

    private void checkUserRole(Long userId) {
        if(!userRepository.isAdmin(userId)) {
            throw new InvalidInputException("You can not get all users infos");
        }
    }

    private void checkIfUserExistsById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found for header id: " + userId);
        }
    }

    private UserEntity getUser(Long userId) {
        return  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + userId));
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
