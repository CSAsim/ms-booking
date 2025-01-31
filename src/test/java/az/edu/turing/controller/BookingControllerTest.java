package az.edu.turing.controller;

import az.edu.turing.exception.NotFoundException;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static az.edu.turing.common.BookingTestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_Should_ReturnSuccess() throws Exception {
        given(bookingService.createBooking(USER_ID, CREATE_BOOKING_REQUEST)).willReturn(BOOKING_DTO);

        mockMvc.perform(post(BASE_URL)
                        .header("userid", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CREATE_BOOKING_REQUEST))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(BOOKING_DTO)))
                .andDo(print());
        then(bookingService).should(times(1)).createBooking(USER_ID, CREATE_BOOKING_REQUEST);
    }

    @Test
    void create_Should_Return404_When_FlightNotFound() throws Exception {
        given(bookingService.createBooking(USER_ID, CREATE_BOOKING_REQUEST))
                .willThrow(new NotFoundException("Flight not found for id: " + FLIGHT_ID));

        mockMvc.perform(post(BASE_URL)
                        .header("userid", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CREATE_BOOKING_REQUEST))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Flight not found for id: " + FLIGHT_ID))
                .andDo(print());
        then(bookingService).should(times(1)).createBooking(USER_ID, CREATE_BOOKING_REQUEST);
    }

    @Test
    void getAll_Should_ReturnSuccess() throws Exception {
        given(bookingService.findAll(eq(USER_ID), any(Pageable.class))).willReturn(BOOKING_DTO_PAGE);

        mockMvc.perform(get(BASE_URL)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
        then(bookingService).should(times(1)).findAll(eq(USER_ID), any(Pageable.class));
    }

    @Test
    void getAll_Should_ReturnSuccess_When_PassengerIdNotNull() throws Exception {
        given(bookingService.findAllByPassengerId(eq(PASSENGER_ID), any(Pageable.class))).willReturn(BOOKING_DTO_PAGE);

        mockMvc.perform(get(BASE_URL)
                        .header("userId", USER_ID)
                        .param("passengerId", String.valueOf(PASSENGER_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andDo(print());
        then(bookingService).should(times(1))
                .findAllByPassengerId(eq(PASSENGER_ID), any(Pageable.class));
    }

    @Test
    void getAll_Should_Return404_When_PassengerIdNotFound() throws Exception {
        given(bookingService.findAllByPassengerId(eq(PASSENGER_ID), any(Pageable.class)))
                .willThrow(new NotFoundException("Passenger not found for id: " + PASSENGER_ID));

        mockMvc.perform(get(BASE_URL)
                        .header("userId", USER_ID)
                        .param("passengerId", String.valueOf(PASSENGER_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Passenger not found for id: " + PASSENGER_ID))
                .andDo(print());
        then(bookingService).should(times(1))
                .findAllByPassengerId(eq(PASSENGER_ID), any(Pageable.class));
    }

    @Test
    void getAllByFlightId_Should_ReturnSuccess() throws Exception {
        given(bookingService.findAllByFlightId(eq(USER_ID), eq(FLIGHT_ID), any(Pageable.class))).willReturn(BOOKING_DTO_PAGE);

        mockMvc.perform(get(BASE_URL + "/{flightId}", FLIGHT_ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andDo(print());
        then(bookingService).should(times(1))
                .findAllByFlightId(eq(USER_ID),eq(FLIGHT_ID), any(Pageable.class));
    }

    @Test
    void getAll_Should_Return404_When_FlightIdNotFound() throws Exception {
        given(bookingService.findAllByFlightId(eq(USER_ID),eq(FLIGHT_ID), any(Pageable.class)))
                .willThrow(new NotFoundException("Flight not found for id: " + FLIGHT_ID));

        mockMvc.perform(get(BASE_URL + "/{flightId}", FLIGHT_ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Flight not found for id: " + FLIGHT_ID))
                .andDo(print());
        then(bookingService).should(times(1))
                .findAllByFlightId(eq(USER_ID),eq(FLIGHT_ID), any(Pageable.class));
    }

    @Test
    void updateBooking_Should_ReturnSuccess() throws Exception {
        given(bookingService.updateBooking(eq(USER_ID), eq(ID), eq(UPDATE_BOOKING_REQUEST))).willReturn(BOOKING_DTO);

        mockMvc.perform(put(BASE_URL + "/{id}", ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UPDATE_BOOKING_REQUEST))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(BOOKING_DTO)))
                .andDo(print());
        then(bookingService).should(times(1))
                .updateBooking(eq(USER_ID), eq(ID), eq(UPDATE_BOOKING_REQUEST));
    }

    @Test
    void updateBooking_Should_Return404_When_BookingIdIdNotFound() throws Exception {
        given(bookingService.updateBooking(eq(USER_ID), eq(ID), eq(UPDATE_BOOKING_REQUEST)))
                .willThrow(new NotFoundException("Booking not found for id: " + ID));

        mockMvc.perform(put(BASE_URL + "/{id}", ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UPDATE_BOOKING_REQUEST))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Booking not found for id: " + ID))
                .andDo(print());
        then(bookingService).should(times(1))
                .updateBooking(eq(USER_ID), eq(ID), eq(UPDATE_BOOKING_REQUEST));
    }

    @Test
    void updateBookingStatus_Should_ReturnSuccess() throws Exception {
        given(bookingService.updateBookingStatus(eq(USER_ID), eq(ID), eq(StatusMessage.COMPLETED))).willReturn(BOOKING_DTO);

        mockMvc.perform(patch(BASE_URL + "/{bookingId}", ID)
                        .header("userId", USER_ID)
                        .param("statusMessage", StatusMessage.COMPLETED.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BOOKING_DTO))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(BOOKING_DTO)))
                .andDo(print());
        then(bookingService).should(times(1))
                .updateBookingStatus(eq(USER_ID), eq(ID), eq(StatusMessage.COMPLETED));
    }

    @Test
    void updateBookingStatus_Should_Return404_When_BookingIdNotFound() throws Exception {
        given(bookingService.updateBookingStatus(eq(USER_ID), eq(ID), eq(StatusMessage.COMPLETED)))
                .willThrow(new NotFoundException("Booking not found for id: " + ID));

        mockMvc.perform(patch(BASE_URL + "/{bookingId}", ID)
                        .header("userId", USER_ID)
                        .param("statusMessage", StatusMessage.COMPLETED.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BOOKING_DTO))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Booking not found for id: " + USER_ID))
                .andDo(print());
        then(bookingService).should(times(1))
                .updateBookingStatus(eq(USER_ID), eq(ID), eq(StatusMessage.COMPLETED));
    }

    @Test
    void delete_Should_ReturnSuccess() throws Exception {
        willDoNothing().given(bookingService).deleteBooking(USER_ID, ID);

        mockMvc.perform(delete(BASE_URL + "/{id}", ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(print());
        then(bookingService).should(times(1)).deleteBooking(USER_ID, ID);
    }

    @Test
    void delete_Should_Return404_When_BookingIdNotFound() throws Exception {
        willThrow(new NotFoundException("Booking not found for id: " + ID)).given(bookingService).deleteBooking(USER_ID, ID);

        mockMvc.perform(delete(BASE_URL + "/{id}", ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Booking not found for id: " + ID))
                .andDo(print());
        then(bookingService).should(times(1)).deleteBooking(USER_ID, ID);
    }


}
