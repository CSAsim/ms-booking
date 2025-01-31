package az.edu.turing.controller;

import az.edu.turing.exception.NotFoundException;
import az.edu.turing.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static az.edu.turing.common.BookingTestConstants.USER_ID;
import static az.edu.turing.common.UserTestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void getAll_ShouldReturnAllUsers() throws Exception {
        when(userService.findAll(eq(USER_ID), any())).thenReturn(USER_DTO_PAGE);

        mockMvc.perform(get("/api/v1/users")
                        .header("userId", USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO_PAGE)));

        verify(userService).findAll(eq(USER_ID), any());
    }

    @Test
    void getAllByFlightId_ShouldReturnUsers() throws Exception {
        when(userService.findAllByFlightId(eq(USER_ID), any(), any())).thenReturn(USER_DTO_PAGE);

        mockMvc.perform(get("/api/v1/users")
                        .header("userId", USER_ID)
                        .param("flightId", String.valueOf(USER_ID)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO_PAGE)));

        verify(userService).findAllByFlightId(eq(USER_ID), any(), any());
    }

    @Test
    void getById_ShouldReturnUser() throws Exception {
        when(userService.findById(USER_ID, USER_ID)).thenReturn(USER_DTO);

        mockMvc.perform(get("/api/v1/users/{id}", USER_ID)
                        .header("userId", USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO)));
        verify(userService).findById(USER_ID, USER_ID);
    }

    @Test
    void getById_ShouldReturnNotFound() throws Exception {
        when(userService.findById(USER_ID, NON_EXISTENT_USER_ID)).thenThrow(new NotFoundException("User not found for id: " + USER_ID));

        mockMvc.perform(get("/api/v1/users/{id}", NON_EXISTENT_USER_ID)
                        .header("userId", USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("User not found for id: " + USER_ID));
        verify(userService).findById(USER_ID, NON_EXISTENT_USER_ID);
    }

    @Test
    void getByEmail_ShouldReturnUser() throws Exception {
        when(userService.findByEmail(USER_ID, EMAIL)).thenReturn(USER_DTO);

        mockMvc.perform(get("/api/v1/users/email")
                        .header("userId", USER_ID)
                        .param("email", EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO)));
        verify(userService).findByEmail(USER_ID, EMAIL);
    }

    @Test
    void getByEmail_ShouldReturnNotFound() throws Exception {
        when(userService.findByEmail(USER_ID, EMAIL)).thenThrow(new NotFoundException("User not found for email: " + EMAIL));

        mockMvc.perform(get("/api/v1/users/email")
                        .header("userId", USER_ID)
                        .param("email", EMAIL))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("User not found for email: " + EMAIL));
        verify(userService).findByEmail(USER_ID, EMAIL);
    }

    @Test
    void create_ShouldCreateUser() throws Exception {
        when(userService.create(USER_ID, CREATE_USER_REQUEST)).thenReturn(USER_DTO);

        mockMvc.perform(post("/api/v1/users")
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CREATE_USER_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO)))
                .andDo(print());
        verify(userService).create(USER_ID, CREATE_USER_REQUEST);
    }

    @Test
    void update_ShouldUpdateUser() throws Exception {
        when(userService.update(USER_ID, USER_ID, UPDATE_USER_REQUEST)).thenReturn(USER_DTO);

        mockMvc.perform(put("/api/v1/users/{id}", USER_ID)
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UPDATE_USER_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO)));
        verify(userService).update(USER_ID, USER_ID, UPDATE_USER_REQUEST);
    }

    @Test
    void delete_ShouldDeleteUser() throws Exception {
        doNothing().when(userService).delete(USER_ID, USER_ID);

        mockMvc.perform(delete("/api/v1/users/{id}", USER_ID)
                        .header("userId", USER_ID))
                .andExpect(status().isNoContent());
        verify(userService).delete(USER_ID, USER_ID);
    }
}
