package az.edu.turing.controller;

import az.edu.turing.exception.NotFoundException;
import az.edu.turing.model.enums.FlightStatus;
import az.edu.turing.service.FlightService;
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
import static az.edu.turing.common.FlightTestConstants.BASE_URL;
import static az.edu.turing.common.FlightTestConstants.CREATE_FLIGHT_REQUEST;
import static az.edu.turing.common.FlightTestConstants.FLIGHT_DTO;
import static az.edu.turing.common.FlightTestConstants.FLIGHT_DTO_PAGE;
import static az.edu.turing.common.FlightTestConstants.ID;
import static az.edu.turing.common.FlightTestConstants.UPDATE_FLIGHT_REQUEST;
import static az.edu.turing.common.JsonFiles.CREATE_FLIGHT_DETAIL_REQUEST_JSON;
import static az.edu.turing.common.JsonFiles.CREATE_FLIGHT_REQUEST_JSON;
import static az.edu.turing.common.JsonFiles.FLIGHT_DETAIL_DTO_JSON;
import static az.edu.turing.common.JsonFiles.FLIGHT_DTO_JSON;
import static az.edu.turing.common.JsonFiles.UPDATE_FLIGHT_DETAIL_REQUEST_JSON;
import static az.edu.turing.common.JsonFiles.UPDATE_FLIGHT_REQUEST_JSON;
import static az.edu.turing.common.TestUtil.json;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        given(flightService.findAll(any(),any(),any(),any(),any())).willReturn(FLIGHT_DTO_PAGE);

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        then(flightService).should(times(1)).findAll(any(),any(),any(),any(),any());
    }

    @Test
    void getAllIn24Hours_ShouldReturnUpcomingFlights() throws Exception {
        given(flightService.findAllIn24Hours(any())).willReturn(FLIGHT_DTO_PAGE);

        mockMvc.perform(get(BASE_URL + "/upcoming")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        then(flightService).should(times(1)).findAllIn24Hours(any());
    }

    @Test
    void getById_ShouldReturnFlight() throws Exception {
        given(flightService.findById(any())).willReturn(FLIGHT_DTO);

        var jsonResponse = json(FLIGHT_DTO_JSON);

        mockMvc.perform(get(BASE_URL + "/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(json(FLIGHT_DTO_JSON)))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).findById(ID);
    }

    @Test
    void getById_ShouldReturnNotFound() throws Exception {
        given(flightService.findById(ID)).willThrow(new NotFoundException("Flight not found"));

        mockMvc.perform(get(BASE_URL + "/{id}", ID))
                .andExpect(status().isNotFound());

        then(flightService).should(times(1)).findById(any());
    }

    @Test
    void create_ShouldCreateFlight() throws Exception {
        given(flightService.create(USER_ID, CREATE_FLIGHT_REQUEST)).willReturn(FLIGHT_DTO);

        var jsonResponse = json(FLIGHT_DTO_JSON);

        mockMvc.perform(post(BASE_URL)
                        .header("id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(CREATE_FLIGHT_REQUEST_JSON)))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).create(USER_ID, CREATE_FLIGHT_REQUEST);
    }

    @Test
    void update_ShouldUpdateFlight() throws Exception {
        when(flightService.update(USER_ID, ID, UPDATE_FLIGHT_REQUEST)).thenReturn(FLIGHT_DTO);

        var jsonResponse = json(FLIGHT_DTO_JSON);

        mockMvc.perform(put(BASE_URL + "/{id}", ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(UPDATE_FLIGHT_REQUEST_JSON)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).update(USER_ID, ID, UPDATE_FLIGHT_REQUEST);
    }

    @Test
    void updateFlightStatus_ShouldUpdateStatus() throws Exception {
        given(flightService.updateFlightStatus(USER_ID, ID, FlightStatus.COMPLETED)).willReturn(FLIGHT_DTO);

        var jsonResponse = json(FLIGHT_DTO_JSON);

        mockMvc.perform(patch(BASE_URL + "/{id}/status", ID)
                        .header("userId", USER_ID)
                        .param("flightStatus", FlightStatus.COMPLETED.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(FlightStatus.COMPLETED)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1))
                .updateFlightStatus(USER_ID, ID, FlightStatus.COMPLETED);
    }

    @Test
    void delete_ShouldDeleteFlight() throws Exception {
        doNothing().when(flightService).delete(USER_ID, ID);

        mockMvc.perform(delete(BASE_URL + "/{id}", ID)
                        .header("userId", USER_ID))
                .andExpect(status().isNoContent());

        then(flightService).should(times(1)).delete(USER_ID, ID);
    }

    @Test
    void getDetailsByFlightId_ShouldReturnFlightDetails() throws Exception {
        given(flightService.findFlightDetailById(ID)).willReturn(FLIGHT_DETAILS_DTO);

        var jsonResponse = json(FLIGHT_DETAIL_DTO_JSON);

        mockMvc.perform(get(BASE_URL + "/{flightId}/details", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).findFlightDetailById(ID);
    }

    @Test
    void createDetails_ShouldCreateFlightDetails() throws Exception {
        given(flightService.createFlightDetail(USER_ID, CREATE_FLIGHT_DETAILS_REQUEST)).willReturn(FLIGHT_DETAILS_DTO);

        var jsonResponse = json(FLIGHT_DETAIL_DTO_JSON);

        mockMvc.perform(post(BASE_URL + "/{flightId}/details", ID)
                        .param("userId", USER_ID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(CREATE_FLIGHT_DETAIL_REQUEST_JSON)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).createFlightDetail(USER_ID,
                CREATE_FLIGHT_DETAILS_REQUEST);
    }

    @Test
    void updateDetails_ShouldUpdateFlightDetails() throws Exception {
        given(flightService.updateFlightDetail(USER_ID, ID, UPDATE_FLIGHT_DETAILS_REQUEST))
                .willReturn(FLIGHT_DETAILS_DTO);

        var jsonResponse = json(FLIGHT_DETAIL_DTO_JSON);

        mockMvc.perform(put(BASE_URL + "/{flightId}/details/{id}", FLIGHT_ID, ID)
                        .param("userId", USER_ID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(UPDATE_FLIGHT_DETAIL_REQUEST_JSON)))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(flightService).should(times(1)).updateFlightDetail(USER_ID, ID,
                UPDATE_FLIGHT_DETAILS_REQUEST);
    }

    @Test
    void deleteDetail_ShouldDeleteFlightDetail() throws Exception {
        doNothing().when(flightService).deleteFlightDetails(USER_ID, ID);

        mockMvc.perform(delete(BASE_URL + "/{flightId}/details/{id}", FLIGHT_ID, ID)
                        .param("userId", USER_ID.toString()))
                .andExpect(status().isNoContent());

        then(flightService).should(times(1)).deleteFlightDetails(USER_ID, FLIGHT_ID);
    }
}
