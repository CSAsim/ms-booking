package az.edu.turing.service.impl;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.exception.AlreadyExistsException;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.UserMapper;
import az.edu.turing.model.UserDto;
import az.edu.turing.model.request.user.CreateUserRequest;
import az.edu.turing.model.request.user.UpdateUserRequest;
import az.edu.turing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findAllByFlightId(Long flightId) {
        return List.of(); // TODO: Flight ile baglanacak
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return Optional.of(userMapper.toDto(user));
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        UserEntity user = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        return Optional.of(userMapper.toDto(user));
    }

    @Override
    public UserDto create(CreateUserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new AlreadyExistsException("User with email " + userRequest.getEmail() + " already exists.");
        }

        UserEntity user = UserEntity.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .password(userRequest.getPassword())
                .userRole(userRequest.getUserRole())
                .isDeleted(false)
                .build();

        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto update(Long id, UpdateUserRequest userRequest) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        if (userRequest.getName() != null) user.setName(userRequest.getName());
        if (userRequest.getSurname() != null) user.setSurname(userRequest.getSurname());
        if (userRequest.getEmail() != null) user.setEmail(userRequest.getEmail());
        if (userRequest.getPhoneNumber() != null) user.setPhoneNumber(userRequest.getPhoneNumber());
        if (userRequest.getPassword() != null) user.setPassword(userRequest.getPassword());
        if (userRequest.getUserRole() != null) user.setUserRole(userRequest.getUserRole());

        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public void delete(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        // Soft delete
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
