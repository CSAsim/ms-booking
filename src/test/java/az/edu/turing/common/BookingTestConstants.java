package az.edu.turing.common;

import az.edu.turing.domain.entity.BookingEntity;
import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.model.BookingDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.model.enums.UserRole;
import az.edu.turing.model.request.booking.CreateBookingRequest;
import az.edu.turing.model.request.booking.UpdateBookingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface BookingTestConstants {

    Long ID = 1L;
    Long USER_ID = 1L;
    Long FLIGHT_ID = 2L;
    Long PASSENGER_ID = 2L;
    int PAGE = 1;
    int SIZE = 4;
    String SORT_BY = "id";
    Integer SEAT_NUMBER = 23;
    Double TOTAL_AMOUNT = 234.0;
    String NAME = "test_name";
    String BASE_URL = "/api/v1/bookings";

    Pageable PAGEABLE = PageRequest.of(PAGE, SIZE, Sort.by(SORT_BY));

    UserEntity USER_ENTITY = UserEntity.builder()
            .id(ID)
            .name(NAME)
            .userRole(UserRole.USER)
            .build();

    FlightEntity FLIGHT_ENTITY = FlightEntity.builder()
            .id(ID)
            .build();

    BookingEntity BOOKING_ENTITY = BookingEntity.builder()
            .id(ID)
            .flight(FLIGHT_ENTITY)
            .passenger(USER_ENTITY)
            .bookingStatus(StatusMessage.PENDING)
            .totalAmount(TOTAL_AMOUNT)
            .build();


    BookingDto BOOKING_DTO = BookingDto.builder()
            .id(ID)
            .flightId(FLIGHT_ID)
            .passengerId(PASSENGER_ID)
            .totalAmount(TOTAL_AMOUNT)
            .bookingStatus(StatusMessage.PENDING)
            .build();

    CreateBookingRequest CREATE_BOOKING_REQUEST = CreateBookingRequest.builder()
            .flightId(FLIGHT_ID)
            .passengerId(PASSENGER_ID)
            .seatNumber(SEAT_NUMBER)
            .totalAmount(TOTAL_AMOUNT)
            .build();

    UpdateBookingRequest UPDATE_BOOKING_REQUEST = UpdateBookingRequest.builder()
            .bookingStatus(StatusMessage.COMPLETED)
            .seatNumber(SEAT_NUMBER)
            .totalAmount(TOTAL_AMOUNT)
            .build();

    Page<BookingEntity> BOOKING_ENTITY_PAGE = new PageImpl<>(List.of(BOOKING_ENTITY), PAGEABLE, 1);
    Page<BookingDto> BOOKING_DTO_PAGE = new PageImpl<>(List.of(BOOKING_DTO), PAGEABLE, 1);
}
