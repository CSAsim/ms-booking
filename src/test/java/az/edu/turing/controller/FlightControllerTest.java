package az.edu.turing.controller;

import az.edu.turing.exception.NotFoundException;
import az.edu.turing.model.enums.FlightStatus;
import az.edu.turing.service.FlightService;
import az.edu.turing.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static az.edu.turing.common.FlightDetailsConstants.CREATE_FLIGHT_DETAILS_REQUEST;
import static az.edu.turing.common.FlightDetailsConstants.FLIGHT_DETAILS_DTO;
import static az.edu.turing.common.FlightDetailsConstants.FLIGHT_ID;
import static az.edu.turing.common.FlightDetailsConstants.UPDATE_FLIGHT_DETAILS_REQUEST;
import static az.edu.turing.common.FlightDetailsConstants.USER_ID;
import static az.edu.turing.common.FlightTestConstants.CREATE_FLIGHT_REQUEST;
import static az.edu.turing.common.FlightTestConstants.FLIGHT_DTO;
import static az.edu.turing.common.FlightTestConstants.FLIGHT_DTO_PAGE;
import static az.edu.turing.common.FlightTestConstants.ID;
import static az.edu.turing.common.FlightTestConstants.UPDATE_FLIGHT_REQUEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
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
        given(flightService.findAll(any(), any(), any(), any(), any())).willReturn(FLIGHT_DTO_PAGE);

        String jsonResponse = TestUtil.json("flight-response.json");

        mockMvc.perform(get("/api/v1/flights"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).findAll(any(), any(), any(), any(), any());
    }

    @Test
    void getAllIn24Hours_ShouldReturnUpcomingFlights() throws Exception {
        String jsonResponse = TestUtil.json("flight-response.json");
        given(flightService.findAllIn24Hours(any())).willReturn(FLIGHT_DTO_PAGE);

        mockMvc.perform(get("/api/v1/flights/upcoming"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).findAllIn24Hours(any());
    }

    @Test
    void getById_ShouldReturnFlight() throws Exception {
        given(flightService.findById(any())).willReturn(FLIGHT_DTO);

        var jsonResponse = TestUtil.json("flight-dto.json");
        mockMvc.perform(get("/api/v1/flights/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).findById(any());
    }

    @Test
    void getById_ShouldReturnNotFound() throws Exception {
        given(flightService.findById(ID)).willThrow(new NotFoundException("Flight not found"));

        mockMvc.perform(get("/api/v1/flights/{id}", ID))
                .andExpect(status().isNotFound());

        then(flightService).should(times(1)).findById(any());
    }

    @Test
    void create_ShouldCreateFlight() throws Exception {
        given(flightService.create(USER_ID, CREATE_FLIGHT_REQUEST)).willReturn(FLIGHT_DTO);

        var jsonResponse = TestUtil.json("create-flight-request.json");

        mockMvc.perform(post("/api/v1/flights")
                        .header("id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CREATE_FLIGHT_REQUEST)))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).create(USER_ID, CREATE_FLIGHT_REQUEST);
    }

    @Test
    void update_ShouldUpdateFlight() throws Exception {
        when(flightService.update(USER_ID, ID, UPDATE_FLIGHT_REQUEST)).thenReturn(FLIGHT_DTO);

        var jsonResponse = TestUtil.json("update-flight-request.json");

        mockMvc.perform(put("/api/v1/flights/{id}", ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UPDATE_FLIGHT_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).update(USER_ID, ID, UPDATE_FLIGHT_REQUEST);
    }

    @Test
    void updateFlightStatus_ShouldUpdateStatus() throws Exception {
        FLIGHT_DTO.setFlightStatus(FlightStatus.CANCELLED);
        given(flightService.updateFlightStatus(USER_ID, ID, FlightStatus.CANCELLED)).willReturn(FLIGHT_DTO);

        var jsonResponse = TestUtil.json("update-flight-dto.json");

        mockMvc.perform(patch("/api/v1/flights/{id}/status", ID)
                        .header("userId", USER_ID)
                        .param("flightStatus", FlightStatus.CANCELLED.name()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1))
                .updateFlightStatus(USER_ID, ID, FlightStatus.CANCELLED);
    }

    @Test
    void delete_ShouldDeleteFlight() throws Exception {
        doNothing().when(flightService).delete(USER_ID, ID);

        mockMvc.perform(delete("/api/v1/flights/{id}", ID)
                        .header("userId", USER_ID))
                .andExpect(status().isNoContent());

        then(flightService).should(times(1)).delete(USER_ID, ID);
    }

    @Test
    void getDetailsByFlightId_ShouldReturnFlightDetails() throws Exception {
        given(flightService.findFlightDetailById(ID)).willReturn(FLIGHT_DETAILS_DTO);

        var jsonResponse = TestUtil.json("flight-detail-dto.json");

        mockMvc.perform(get("/api/v1/flights/{flightId}/details", ID))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).findFlightDetailById(ID);
    }

    @Test
    void createDetails_ShouldCreateFlightDetails() throws Exception {
        given(flightService.createFlightDetail(USER_ID, CREATE_FLIGHT_DETAILS_REQUEST)).willReturn(FLIGHT_DETAILS_DTO);

        var jsonResponse = TestUtil.json("create-flight-detail.json");

        mockMvc.perform(post("/api/v1/flights/{flightId}/details", ID)
                        .param("userId", USER_ID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CREATE_FLIGHT_DETAILS_REQUEST)))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).createFlightDetail(USER_ID,
                CREATE_FLIGHT_DETAILS_REQUEST);
    }

    @Test
    void updateDetails_ShouldUpdateFlightDetails() throws Exception {
        given(flightService.updateFlightDetail(USER_ID, ID, UPDATE_FLIGHT_DETAILS_REQUEST))
                .willReturn(FLIGHT_DETAILS_DTO);

        var jsonResponse = TestUtil.json("update-flight-detail.json");

        mockMvc.perform(put("/api/v1/flights/{flightId}/details/{id}", FLIGHT_ID, ID)
                        .param("userId", USER_ID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UPDATE_FLIGHT_DETAILS_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).updateFlightDetail(USER_ID, ID,
                UPDATE_FLIGHT_DETAILS_REQUEST);
    }

    @Test
    void deleteDetail_ShouldDeleteFlightDetail() throws Exception {
        doNothing().when(flightService).deleteFlightDetails(USER_ID, ID);

        mockMvc.perform(delete("/api/v1/flights/{flightId}/details/{id}", FLIGHT_ID, ID)
                        .param("userId", USER_ID.toString()))
                .andExpect(status().isNoContent());

        then(flightService).should(times(1)).deleteFlightDetails(USER_ID, FLIGHT_ID);
    }
}
