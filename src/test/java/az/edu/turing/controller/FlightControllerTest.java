package az.edu.turing.controller;

import az.edu.turing.exception.NotFoundException;
import az.edu.turing.model.enums.FlightStatus;
import az.edu.turing.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


import static az.edu.turing.common.FlightTestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FlightService flightService;

    @Test
    void getAll_ShouldReturnAllFlights() throws Exception {
        when(flightService.findAll(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(FLIGHT_DTO)));

        mockMvc.perform(get("/api/v1/flights"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new PageImpl<>(List.of(FLIGHT_DTO)))));

        verify(flightService).findAll(any(), any(), any(), any(), any());
    }

    @Test
    void getAllIn24Hours_ShouldReturnUpcomingFlights() throws Exception {
        when(flightService.findAllIn24Hours(any())).thenReturn(new PageImpl<>(List.of(FLIGHT_DTO)));

        mockMvc.perform(get("/api/v1/flights/upcoming"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new PageImpl<>(List.of(FLIGHT_DTO)))));
    }

    @Test
    void getById_ShouldReturnFlight() throws Exception {
        when(flightService.findById(ID)).thenReturn(FLIGHT_DTO);

        mockMvc.perform(get("/api/v1/flights/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(FLIGHT_DTO)));
    }

    @Test
    void getById_ShouldReturnNotFound() throws Exception {
        when(flightService.findById(ID)).thenThrow(new NotFoundException("Flight not found"));

        mockMvc.perform(get("/api/v1/flights/{id}", ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByFlightNumber_ShouldReturnNotFound() throws Exception {
        when(flightService.findByFlightNumber(FLIGHT_NUMBER)).thenThrow(new NotFoundException("Flight not found"));

        mockMvc.perform(get("/api/v1/flights/number/{flightNumber}", FLIGHT_NUMBER))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_ShouldCreateFlight() throws Exception {
        when(flightService.create(USER_ID, CREATE_FLIGHT_REQUEST)).thenReturn(FLIGHT_DTO);

        mockMvc.perform(post("/api/v1/flights")
                        .header("id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CREATE_FLIGHT_REQUEST)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(FLIGHT_DTO)));
    }

    @Test
    void update_ShouldUpdateFlight() throws Exception {
        when(flightService.update(USER_ID, ID, UPDATE_FLIGHT_REQUEST)).thenReturn(FLIGHT_DTO);

        mockMvc.perform(put("/api/v1/flights/{id}", ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UPDATE_FLIGHT_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(FLIGHT_DTO)));
    }

    @Test
    void updateFlightStatus_ShouldUpdateStatus() throws Exception {
        when(flightService.updateFlightStatus(USER_ID, ID, FlightStatus.CANCELLED)).thenReturn(FLIGHT_DTO);

        mockMvc.perform(patch("/api/v1/flights/{id}/status", ID)
                        .header("userId", USER_ID)
                        .param("flightStatus", FlightStatus.CANCELLED.name()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(FLIGHT_DTO)));
    }

    @Test
    void delete_ShouldDeleteFlight() throws Exception {
        doNothing().when(flightService).delete(USER_ID, ID);

        mockMvc.perform(delete("/api/v1/flights/{id}", ID)
                        .header("userId", USER_ID))
                .andExpect(status().isNoContent());
    }
}
