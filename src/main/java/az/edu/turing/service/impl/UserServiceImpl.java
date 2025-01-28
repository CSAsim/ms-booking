package az.edu.turing.service.impl;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.domain.repository.UserRepository;
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
        return List.of(); // TODO: Flight ile baghlanacaq
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findAll().stream()
                .filter(user -> user.getEmail().equals(email))
                .map(userMapper::toDto)
                .findFirst();
    }

    @Override
    public UserDto create(CreateUserRequest userRequest) {
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
                .orElseThrow(() -> new RuntimeException("User not found"));

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
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findAll().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
