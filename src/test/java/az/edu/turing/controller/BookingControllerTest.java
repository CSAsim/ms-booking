package az.edu.turing.controller;

import az.edu.turing.exception.NotFoundException;
import az.edu.turing.model.enums.BookingStatus;
import az.edu.turing.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static az.edu.turing.common.BookingTestConstants.BASE_URL;
import static az.edu.turing.common.BookingTestConstants.BOOKING_DTO;
import static az.edu.turing.common.BookingTestConstants.BOOKING_DTO_PAGE;
import static az.edu.turing.common.BookingTestConstants.CREATE_BOOKING_REQUEST;
import static az.edu.turing.common.BookingTestConstants.FLIGHT_ID;
import static az.edu.turing.common.BookingTestConstants.ID;
import static az.edu.turing.common.BookingTestConstants.PASSENGER_ID;
import static az.edu.turing.common.BookingTestConstants.UPDATE_BOOKING_REQUEST;
import static az.edu.turing.common.BookingTestConstants.USER_ID;
import static az.edu.turing.common.JsonFiles.BOOKING_DTO_JSON;
import static az.edu.turing.common.JsonFiles.CREATE_BOOKING_REQUEST_JSON;
import static az.edu.turing.common.JsonFiles.UPDATED_BOOKING_REQUEST_JSON;
import static az.edu.turing.common.TestUtil.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_Should_ReturnSuccess() throws Exception {
        given(bookingService.createBooking(USER_ID, CREATE_BOOKING_REQUEST)).willReturn(BOOKING_DTO);

        var response = json(BOOKING_DTO_JSON);
        mockMvc.perform(post(BASE_URL)
                        .header("userid", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(CREATE_BOOKING_REQUEST_JSON))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(response))
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
                        .content(json(CREATE_BOOKING_REQUEST_JSON))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Flight not found for id: "
                        + FLIGHT_ID))
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
                .andExpect(jsonPath("$.data").isArray());
        then(bookingService).should(times(1)).findAll(eq(USER_ID),any(Pageable.class));
    }

    @Test
    void getAll_Should_ReturnSuccess_When_PassengerIdNotNull() throws Exception {
        given(bookingService.findAllByPassengerId(eq(PASSENGER_ID), any(Pageable.class))).willReturn(BOOKING_DTO_PAGE);

        mockMvc.perform(get(BASE_URL + "/passenger/{id}", PASSENGER_ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(print());
        then(bookingService).should(times(1))
                .findAllByPassengerId(eq(PASSENGER_ID), any(Pageable.class));
    }

    @Test
    void getAll_Should_Return404_When_PassengerIdNotFound() throws Exception {
        given(bookingService.findAllByPassengerId(eq(PASSENGER_ID), any(Pageable.class)))
                .willThrow(new NotFoundException("Passenger not found for id: " + PASSENGER_ID));

        mockMvc.perform(get(BASE_URL + "/passenger/{id}", PASSENGER_ID)
                        .header("userId", USER_ID)
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

        mockMvc.perform(get(BASE_URL + "/flight/{flightId}", FLIGHT_ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(print());
        then(bookingService).should(times(1)).findAllByFlightId(eq(USER_ID),eq(FLIGHT_ID), any(Pageable.class));
    }

    @Test
    void getAll_Should_Return404_When_FlightIdNotFound() throws Exception {
        given(bookingService.findAllByFlightId(eq(USER_ID),eq(FLIGHT_ID), any(Pageable.class)))
                .willThrow(new NotFoundException("Flight not found for id: " + FLIGHT_ID));

        mockMvc.perform(get(BASE_URL + "/flight/{flightId}", FLIGHT_ID)
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
        given(bookingService.updateBooking(USER_ID, ID, UPDATE_BOOKING_REQUEST)).willReturn(BOOKING_DTO);

        var response = json(BOOKING_DTO_JSON);
        mockMvc.perform(put(BASE_URL + "/{id}", ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(UPDATED_BOOKING_REQUEST_JSON))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
        then(bookingService).should(times(1))
                .updateBooking(USER_ID, ID, UPDATE_BOOKING_REQUEST);
    }

    @Test
    void updateBooking_Should_Return404_When_BookingIdIdNotFound() throws Exception {
        given(bookingService.updateBooking(USER_ID, ID, UPDATE_BOOKING_REQUEST))
                .willThrow(new NotFoundException("Booking not found for id: " + ID));

        mockMvc.perform(put(BASE_URL + "/{id}", ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(UPDATED_BOOKING_REQUEST_JSON))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Booking not found for id: " + ID))
                .andDo(print());
        then(bookingService).should(times(1))
                .updateBooking(USER_ID, ID, UPDATE_BOOKING_REQUEST);
    }

    @Test
    void updateBookingStatus_Should_ReturnSuccess() throws Exception {
        given(bookingService.updateBookingStatus(USER_ID, ID, BookingStatus.COMPLETED)).willReturn(BOOKING_DTO);

        var response = json(BOOKING_DTO_JSON);
        mockMvc.perform(patch(BASE_URL + "/{bookingId}", ID)
                        .header("userId", USER_ID)
                        .param("statusMessage", BookingStatus.COMPLETED.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BookingStatus.COMPLETED))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
        then(bookingService).should(times(1))
                .updateBookingStatus(USER_ID, ID, BookingStatus.COMPLETED);
    }

    @Test
    void updateBookingStatus_Should_Return404_When_BookingIdNotFound() throws Exception {
        given(bookingService.updateBookingStatus(USER_ID, ID, BookingStatus.COMPLETED))

                .willThrow(new NotFoundException("Booking not found for id: " + ID));

        mockMvc.perform(patch(BASE_URL + "/{bookingId}", ID)
                        .header("userId", USER_ID)
                        .param("statusMessage", BookingStatus.COMPLETED.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BookingStatus.COMPLETED))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Booking not found for id: " + ID))
                .andDo(print());
        then(bookingService).should(times(1))
                .updateBookingStatus(USER_ID, ID, BookingStatus.COMPLETED);
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
