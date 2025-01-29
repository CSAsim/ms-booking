package az.edu.turing.service;

import az.edu.turing.domain.repository.BookingRepository;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.BookingMapper;
import az.edu.turing.model.BookingDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static az.edu.turing.common.BookingTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;


    @Test
    void findAll_Should_ReturnSuccess() {
        given(bookingRepository.findAll(PAGEABLE)).willReturn(BOOKING_ENTITY_PAGE);
        given(bookingMapper.toDto(BOOKING_ENTITY_PAGE)).willReturn(BOOKING_DTO_PAGE);

        Page<BookingDto> result = bookingService.findAll(PAGE, SIZE, SORT_BY);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(List.of(BOOKING_DTO), result.getContent());

        then(bookingRepository).should(times(1)).findAll(PAGEABLE);
    }

    @Test
    void findAllByFlightId_Should_ReturnSuccess() {
        given(bookingRepository.findAllByFlightId(FLIGHT_ID, PAGEABLE)).willReturn(BOOKING_ENTITY_PAGE);
        given(flightRepository.existsById(FLIGHT_ID)).willReturn(true);
        given(bookingMapper.toDto(BOOKING_ENTITY_PAGE)).willReturn(BOOKING_DTO_PAGE);

        Page<BookingDto> result = bookingService.findAllByFlightId(FLIGHT_ID, PAGE, SIZE, SORT_BY);

        assertNotNull(result);
        assertEquals(List.of(BOOKING_DTO), result.getContent());
        assertFalse(result.isEmpty());

        then(bookingRepository).should(times(1)).findAllByFlightId(FLIGHT_ID, PAGEABLE);
    }

    @Test
    void findAllByFlightId_Should_ThrowNotfoundException_WhenFlightIdNotFound() {
        given(flightRepository.existsById(FLIGHT_ID)).willReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.findAllByFlightId(FLIGHT_ID, PAGE, SIZE, SORT_BY));

        assertEquals("Flight not found for id: " + FLIGHT_ID, exception.getMessage());

        then(bookingRepository).should(never()).findAllByFlightId(FLIGHT_ID, PAGEABLE);
    }

    @Test
    void findAllByPassengerId_Should_ReturnSuccess() {
        given(bookingRepository.findAllByPassengerId(PASSENGER_ID, PAGEABLE)).willReturn(BOOKING_ENTITY_PAGE);
        given(userRepository.existsById(PASSENGER_ID)).willReturn(true);
        given(bookingMapper.toDto(BOOKING_ENTITY_PAGE)).willReturn(BOOKING_DTO_PAGE);

        Page<BookingDto> result = bookingService.findAllByPassengerId(PASSENGER_ID, PAGE, SIZE, SORT_BY);

        assertNotNull(result);
        assertEquals(List.of(BOOKING_DTO), result.getContent());
        assertFalse(result.isEmpty());

        then(bookingRepository).should(times(1)).findAllByPassengerId(PASSENGER_ID, PAGEABLE);
    }

    @Test
    void findAllByPassengerId_Should_ThrowNotfoundException_WhenPassengerIdNotFound() {
        given(userRepository.existsById(PASSENGER_ID)).willReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.findAllByPassengerId(PASSENGER_ID, PAGE, SIZE, SORT_BY));

        assertEquals("Passenger not found for id: " + PASSENGER_ID, exception.getMessage());

        then(bookingRepository).should(never()).findAllByPassengerId(PASSENGER_ID, PAGEABLE);
    }

    @Test
    void createBooking_Should_ReturnSuccess() {
        given(bookingRepository.save(BOOKING_ENTITY)).willReturn(BOOKING_ENTITY);
        given(flightRepository.findById(FLIGHT_ID)).willReturn(Optional.of(FLIGHT_ENTITY));
        given(userRepository.findById(PASSENGER_ID)).willReturn(Optional.of(USER_ENTITY));
        given(bookingMapper.toEntity(CREATE_BOOKING_REQUEST)).willReturn(BOOKING_ENTITY);
        given(bookingMapper.toDto(BOOKING_ENTITY)).willReturn(BOOKING_DTO);
        given(userRepository.existsById(USER_ID)).willReturn(true);

        BookingDto result = bookingService.createBooking(USER_ID, CREATE_BOOKING_REQUEST);

        assertNotNull(result);
        assertEquals(BOOKING_DTO, result);

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(flightRepository).should(times(1)).findById(FLIGHT_ID);
        then(userRepository).should(times(1)).findById(PASSENGER_ID);
        then(bookingRepository).should(times(1)).save(BOOKING_ENTITY);
    }

    @Test
    void createBooking_Should_ThrowNotFoundException_When_UserNotFound() {

        given(userRepository.existsById(USER_ID)).willReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(USER_ID, CREATE_BOOKING_REQUEST));

        assertEquals("User not found for id: " + USER_ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(flightRepository).should(never()).findById(FLIGHT_ID);
        then(userRepository).should(never()).findById(PASSENGER_ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }

    @Test
    void createBooking_Should_ThrowNotFoundException_When_FlightIdNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(flightRepository.findById(FLIGHT_ID)).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(USER_ID, CREATE_BOOKING_REQUEST));

        assertEquals("Flight not found for id: " + FLIGHT_ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(flightRepository).should(times(1)).findById(FLIGHT_ID);
        then(userRepository).should(never()).findById(PASSENGER_ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }

    @Test
    void createBooking_Should_ThrowNotFoundException_When_PassengerIdNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(flightRepository.findById(FLIGHT_ID)).willReturn(Optional.of(FLIGHT_ENTITY));
        given(userRepository.findById(PASSENGER_ID)).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(USER_ID, CREATE_BOOKING_REQUEST));

        assertEquals("Passenger not found for id: " + PASSENGER_ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(flightRepository).should(times(1)).findById(FLIGHT_ID);
        then(userRepository).should(times(1)).findById(PASSENGER_ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }

    @Test
    void updateBooking_Should_ReturnSuccess() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.findById(ID)).willReturn(Optional.of(BOOKING_ENTITY));
        given(bookingRepository.save(BOOKING_ENTITY)).willReturn(BOOKING_ENTITY);
        given(bookingMapper.toDto(BOOKING_ENTITY)).willReturn(BOOKING_DTO);

        BookingDto result = bookingService.updateBooking(USER_ID, ID, UPDATE_BOOKING_REQUEST);

        assertNotNull(result);
        assertEquals(BOOKING_DTO, result);

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).findById(ID);
        then(bookingRepository).should(times(1)).save(BOOKING_ENTITY);
    }

    @Test
    void updateBooking_Should_ThrowNotFoundException_When_UserIdNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.updateBooking(USER_ID, ID, UPDATE_BOOKING_REQUEST));

        assertEquals("User not found for id: " + USER_ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(never()).findById(ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }


    @Test
    void updateBooking_Should_ThrowNotFoundException_When_BookingIdNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.findById(ID)).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.updateBooking(USER_ID, ID, UPDATE_BOOKING_REQUEST));

        assertEquals("Booking not found for id: " + ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).findById(ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }

    @Test
    void updateBookingStatus_Should_ReturnSuccess() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.findById(ID)).willReturn(Optional.of(BOOKING_ENTITY));
        given(bookingRepository.save(BOOKING_ENTITY)).willReturn(BOOKING_ENTITY);
        given(bookingMapper.toDto(BOOKING_ENTITY)).willReturn(BOOKING_DTO);

        BookingDto result = bookingService.updateBookingStatus(USER_ID, ID, StatusMessage.COMPLETED);

        assertNotNull(result);
        assertEquals(BOOKING_DTO, result);

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).findById(ID);
        then(bookingRepository).should(times(1)).save(BOOKING_ENTITY);
    }

    @Test
    void updateBookingStatus_Should_Throw_NotFoundException_When_UserNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.updateBookingStatus(USER_ID, ID, StatusMessage.COMPLETED));

        assertEquals("User not found for id: " + USER_ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(never()).findById(ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }

    @Test
    void updateBookingStatus_Should_ThrowNotFoundException_When_BookingIdNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.findById(ID)).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.updateBookingStatus(USER_ID, ID, StatusMessage.COMPLETED));

        assertEquals("Booking not found for id: " + ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).findById(ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }

    @Test
    void deleteBooking_Should_ReturnSuccess() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.existsById(ID)).willReturn(true);
        given(bookingRepository.findById(ID)).willReturn(Optional.of(BOOKING_ENTITY));

        bookingService.deleteBooking(USER_ID, ID);

        assertEquals(StatusMessage.CANCELLED, BOOKING_ENTITY.getBookingStatus());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).existsById(ID);
        then(bookingRepository).should(times(1)).findById(ID);
        then(bookingRepository).should(times(1)).save(BOOKING_ENTITY);
    }

    @Test
    void deleteBooking_Should_ThrowNotFoundException_When_UserIdNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.deleteBooking(USER_ID, ID));

        assertEquals("User not found for id: " + USER_ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(never()).existsById(ID);
        then(bookingRepository).should(never()).findById(ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }

    @Test
    void deleteBooking_Should_ThrowNotFoundException_When_BookingIdNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.existsById(ID)).willReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.deleteBooking(USER_ID, ID));

        assertEquals("Booking not found for id: " + ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).existsById(ID);
        then(bookingRepository).should(never()).findById(ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }
}
