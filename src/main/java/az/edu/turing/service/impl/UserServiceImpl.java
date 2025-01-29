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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> findAllByFlightId(Long flightId) {
        if(!flightRepository.existsById(flightId)) {
            throw new NotFoundException("Flight not found for id:" + flightId);
        }
        return userMapper.toDto(userRepository.findAllByFlightId(flightId));
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + id));
    }

    @Override
    public UserDto findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NotFoundException("User not found for email: " + email));
    }

    @Override
    public UserDto create(Long userId, CreateUserRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidInputException("Password do not match");
        }

        existsByHeaderId(userId);
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User with email " + request.getEmail() + " already exists.");
        }

        UserEntity user = userMapper.toEntity(request);
        user.setDeleted(false);
        user.setCreatedBy(userId);
        user.setUpdatedBy(userId);
        return userMapper.toDto(userRepository.save(user));
    }


    @Override
    public UserDto update(Long userId, Long id, UpdateUserRequest userRequest) {

        existsByHeaderId(userId);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        if (userRequest.getName() != null) user.setName(userRequest.getName());
        if (userRequest.getSurname() != null) user.setSurname(userRequest.getSurname());
        if (userRequest.getEmail() != null) user.setEmail(userRequest.getEmail());
        if (userRequest.getPhoneNumber() != null) user.setPhoneNumber(userRequest.getPhoneNumber());
        if (userRequest.getPassword() != null) user.setPassword(userRequest.getPassword());
        if (userRequest.getUserRole() != null) user.setUserRole(userRequest.getUserRole());

        user.setUpdatedBy(userId);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void delete(Long userId, Long id) {
        existsByHeaderId(userId);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        // Soft delete
        user.setDeleted(true);
        user.setUpdatedBy(userId);
        userRepository.save(user);
    }

    private void existsByHeaderId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found for header id: " + userId);
        }
    }

    private void existsById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found for id: " + id);
        }
    }

    private void existsByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException("User not found for email: " + email);
        }
    }
}
