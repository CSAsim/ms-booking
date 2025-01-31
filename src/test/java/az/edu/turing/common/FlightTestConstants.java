package az.edu.turing.common;

import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.model.FlightDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import az.edu.turing.model.request.flight.UpdateFlightRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

public interface FlightTestConstants {

    Long ID = 1L;
    Long USER_ID = 2L;
    int PAGE = 1;
    int SIZE = 4;
    String SORT_BY = "id";
    String FLIGHT_NUMBER = "flightNumber";
    String DEPARTURE = "Baku";
    String DESTINATION = "Kiev";
    LocalDateTime DEPARTURE_TIME = LocalDateTime.now();
    StatusMessage FLIGHT_STATUS = StatusMessage.PENDING;
    int AVAILABLE_SEATS = 17;

    Pageable PAGEABLE = PageRequest.of(PAGE, SIZE, Sort.by(SORT_BY));

    CreateFlightRequest CREATE_FLIGHT_REQUEST = new CreateFlightRequest(DEPARTURE, DESTINATION, DEPARTURE_TIME,
            FLIGHT_NUMBER, AVAILABLE_SEATS);
    UpdateFlightRequest UPDATE_FLIGHT_REQUEST = new UpdateFlightRequest(DEPARTURE, DESTINATION, DEPARTURE_TIME,
            AVAILABLE_SEATS);

    FlightEntity FLIGHT_ENTITY = FlightEntity.builder()
            .id(ID)
            .flightNumber(FLIGHT_NUMBER)
            .departure(DEPARTURE)
            .destination(DESTINATION)
            .arrivalTime(DEPARTURE_TIME)
            .availableSeats(AVAILABLE_SEATS)
            .flightStatus(FLIGHT_STATUS)
            .build();

    FlightDto FLIGHT_DTO = FlightDto.builder()
            .id(ID)
            .flightNumber(FLIGHT_NUMBER)
            .departure(DEPARTURE)
            .destination(DESTINATION)
            .arrivalTime(DEPARTURE_TIME)
            .availableSeats(AVAILABLE_SEATS)
            .flightStatus(FLIGHT_STATUS)
            .build();
}
