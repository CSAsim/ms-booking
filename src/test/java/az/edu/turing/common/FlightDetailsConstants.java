package az.edu.turing.common;

import az.edu.turing.domain.entity.FlightDetailsEntity;
import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.model.FlightDetailsDto;
import az.edu.turing.model.enums.UserRole;
import az.edu.turing.model.request.flightDetails.CreateFlightDetailsRequest;
import az.edu.turing.model.request.flightDetails.UpdateFlightDetailsRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface FlightDetailsConstants {

    Long ID = 1L;
    Long FLIGHT_ID = 2L;
    Long USER_ID = 3L;
    int PAGE = 1;
    int SIZE = 4;
    String SORT_BY = "id";
    String NAME = "user";
    String AIRLINE_NAME = "airline";
    String PLANE_MODEL = "mode1";
    Integer CAPACITY = 33;

    FlightEntity FLIGHT_ENTITY = FlightEntity.builder()
            .id(FLIGHT_ID)
            .build();

    UserEntity USER_ENTITY = UserEntity.builder()
            .id(USER_ID)
            .name(NAME)
            .userRole(UserRole.USER)
            .build();

    FlightDetailsEntity FLIGHT_DETAILS_ENTITY = FlightDetailsEntity.builder()
            .id(ID)
            .airlineName(AIRLINE_NAME)
            .planeModel(PLANE_MODEL)
            .capacity(CAPACITY)
            .flight(FLIGHT_ENTITY)
            .build();

    FlightDetailsDto FLIGHT_DETAILS_DTO = FlightDetailsDto.builder()
            .id(ID)
            .airlineName(AIRLINE_NAME)
            .planeModel(PLANE_MODEL)
            .capacity(CAPACITY)
            .flightId(FLIGHT_ID)
            .build();

    CreateFlightDetailsRequest CREATE_FLIGHT_DETAILS_REQUEST = CreateFlightDetailsRequest.builder()
            .airlineName(AIRLINE_NAME)
            .planeModel(PLANE_MODEL)
            .capacity(CAPACITY)
            .flightId(FLIGHT_ID)
            .build();

    UpdateFlightDetailsRequest UPDATE_FLIGHT_DETAILS_REQUEST = UpdateFlightDetailsRequest.builder()
            .flightId(FLIGHT_ID)
            .airlineName(AIRLINE_NAME)
            .planeModel(PLANE_MODEL)
            .capacity(CAPACITY)
            .build();

    Pageable PAGEABLE = PageRequest.of(PAGE, SIZE, Sort.by(SORT_BY));
    Page<FlightDetailsEntity> FLIGHT_DETAILS_ENTITY_PAGE = new PageImpl<>(List.of(FLIGHT_DETAILS_ENTITY), PAGEABLE, 1);
    Page<FlightDetailsDto> FLIGHT_DETAILS_DTO_PAGE = new PageImpl<>(List.of(FLIGHT_DETAILS_DTO), PAGEABLE, 1);
}
