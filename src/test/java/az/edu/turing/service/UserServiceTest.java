package az.edu.turing.service;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.exception.AlreadyExistsException;
import az.edu.turing.exception.InvalidInputException;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.UserMapper;
import az.edu.turing.model.dto.UserDto;
import az.edu.turing.model.request.user.CreateUserRequest;
import az.edu.turing.model.request.user.UpdateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.Optional;

import static az.edu.turing.common.UserTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Spy
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findAll_ShouldReturnAllUsers() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(userRepository.findAll(PAGEABLE)).willReturn(USER_ENTITY_PAGE);
        given(userMapper.toDto(USER_ENTITY_PAGE)).willReturn(USER_DTO_PAGE);

        Page<UserDto> result = userService.findAll(USER_ID, PAGEABLE);

        assertNotNull(result);
        assertEquals(USER_DTO_PAGE.getContent(), result.getContent());
        then(userRepository).should(times(1)).findAll(PAGEABLE);
    }

    @Test
    void findById_ShouldReturnUser() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER_ENTITY));
        given(userMapper.toDto(USER_ENTITY)).willReturn(USER_DTO);

        UserDto result = userService.findById(USER_ID, USER_ID);

        assertNotNull(result);
        assertEquals(USER_DTO, result);
        then(userRepository).should(times(1)).findById(eq(USER_ID));
    }

    @Test
    void createUser_ShouldCreateUser() {
        given(userRepository.existsByEmail(EMAIL)).willReturn(false);
        given(userMapper.toEntity(CREATE_USER_REQUEST)).willReturn(USER_ENTITY);
        given(userRepository.save(any())).willReturn(USER_ENTITY);
        given(userMapper.toDto(USER_ENTITY)).willReturn(USER_DTO);

        UserDto result = userService.create(USER_ID, CREATE_USER_REQUEST);

        assertNotNull(result);
        assertEquals(USER_DTO, result);
        then(userRepository).should(times(1)).save(any());
    }

    @Test
    void createUser_ShouldThrowExceptionWhenEmailAlreadyExists() {
        given(userRepository.existsByEmail(EMAIL)).willReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userService.create(USER_ID, CREATE_USER_REQUEST));
        then(userRepository).should(never()).save(any());
    }

    @Test
    void updateUser_ShouldUpdateUser() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER_ENTITY));
        given(userRepository.save(USER_ENTITY)).willReturn(USER_ENTITY);
        given(userMapper.toDto(USER_ENTITY)).willReturn(USER_DTO);

        UserDto result = userService.update(USER_ID, USER_ID, UPDATE_USER_REQUEST);

        assertNotNull(result);
        assertEquals(USER_DTO, result);
        then(userRepository).should(times(1)).findById(USER_ID);
        then(userRepository).should(times(1)).save(USER_ENTITY);
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER_ENTITY));
        given(userRepository.save(USER_ENTITY)).willReturn(USER_ENTITY);

        userService.delete(USER_ID, USER_ID);

        then(userRepository).should(times(1)).findById(USER_ID);
        then(userRepository).should(times(1)).save(USER_ENTITY);
    }
}
