package az.edu.turing.service;

import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.exception.AlreadyExistsException;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.UserMapper;
import az.edu.turing.model.UserDto;
import az.edu.turing.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static az.edu.turing.common.UserTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FlightRepository flightRepository;

    @Spy
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAll_Should_ReturnSuccess() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(userRepository.findAll(PAGEABLE)).willReturn(USER_ENTITY_PAGE);
        given(userMapper.toDto(USER_ENTITY_PAGE)).willReturn(USER_DTO_PAGE);

        Page<UserDto> result = userService.findAll(USER_ID, PAGEABLE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(List.of(USER_DTO), result.getContent());

        then(userRepository).should(times(1)).findAll(PAGEABLE);
    }

    @Test
    void findById_Should_ReturnSuccess() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER_ENTITY));
        given(userMapper.toDto(USER_ENTITY)).willReturn(USER_DTO);

        UserDto result = userService.findById(USER_ID, USER_ID);

        assertNotNull(result);
        assertEquals(USER_DTO, result);

        then(userRepository).should(times(1)).findById(USER_ID);
    }

    @Test
    void findById_Should_ThrowNotFoundException_WhenUserNotFound() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.findById(USER_ID, USER_ID));

        assertEquals("User not found with id: " + USER_ID, exception.getMessage());

        then(userRepository).should(times(1)).findById(USER_ID);
    }

    @Test
    void create_Should_ReturnSuccess() {
        given(userRepository.existsByEmail(EMAIL)).willReturn(false);
        given(userRepository.save(USER_ENTITY)).willReturn(USER_ENTITY);
        given(userMapper.toEntity(CREATE_USER_REQUEST)).willReturn(USER_ENTITY);
        given(userMapper.toDto(USER_ENTITY)).willReturn(USER_DTO);

        UserDto result = userService.create(USER_ID, CREATE_USER_REQUEST);

        assertNotNull(result);
        assertEquals(USER_DTO, result);

        then(userRepository).should(times(1)).existsByEmail(EMAIL);
        then(userRepository).should(times(1)).save(USER_ENTITY);
    }

    @Test
    void create_Should_ThrowAlreadyExistsException_WhenUserAlreadyExists() {
        given(userRepository.existsByEmail(EMAIL)).willReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> userService.create(USER_ID, CREATE_USER_REQUEST));

        assertEquals("User with email " + EMAIL + " already exists.", exception.getMessage());

        then(userRepository).should(times(1)).existsByEmail(EMAIL);
        then(userRepository).should(never()).save(USER_ENTITY);
    }

    @Test
    void update_Should_ReturnSuccess() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER_ENTITY));
        given(userRepository.save(USER_ENTITY)).willReturn(USER_ENTITY);
        given(userMapper.toDto(USER_ENTITY)).willReturn(USER_DTO);

        UserDto result = userService.update(USER_ID, USER_ID, UPDATE_USER_REQUEST);

        assertNotNull(result);
        assertEquals(USER_DTO, result);

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(userRepository).should(times(1)).findById(USER_ID);
        then(userRepository).should(times(1)).save(USER_ENTITY);
    }

    @Test
    void update_Should_ThrowNotFoundException_WhenUserNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.update(USER_ID, USER_ID, UPDATE_USER_REQUEST));

        assertEquals("User not found with id: " + USER_ID, exception.getMessage());

        then(userRepository).should(times(1)).findById(USER_ID);
        then(userRepository).should(never()).save(USER_ENTITY);
    }

    @Test
    void delete_Should_ReturnSuccess() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER_ENTITY));

        userService.delete(USER_ID, USER_ID);

        assertTrue(USER_ENTITY.isDeleted());

        then(userRepository).should(times(1)).findById(USER_ID);
        then(userRepository).should(times(1)).save(USER_ENTITY);
    }

    @Test
    void delete_Should_ThrowNotFoundException_WhenUserNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.delete(USER_ID, USER_ID));

        assertEquals("User not found with id: " + USER_ID, exception.getMessage());

        then(userRepository).should(times(1)).findById(USER_ID);
    }

    @Test
    void findByEmail_Should_ReturnSuccess() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(userRepository.findByEmail(EMAIL)).willReturn(Optional.of(USER_ENTITY));
        given(userMapper.toDto(USER_ENTITY)).willReturn(USER_DTO);

        UserDto result = userService.findByEmail(USER_ID, EMAIL);

        assertNotNull(result);
        assertEquals(USER_DTO, result);

        then(userRepository).should(times(1)).findByEmail(EMAIL);
    }

    @Test
    void findByEmail_Should_ThrowNotFoundException_WhenEmailNotFound() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(userRepository.findByEmail(EMAIL)).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.findByEmail(USER_ID, EMAIL));

        assertEquals("User not found for email: " + EMAIL, exception.getMessage());

        then(userRepository).should(times(1)).findByEmail(EMAIL);
    }
}
