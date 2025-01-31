package az.edu.turing.controller;

import az.edu.turing.exception.NotFoundException;
import az.edu.turing.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static az.edu.turing.common.UserTestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void getAll_ShouldReturnAllUsers() throws Exception {
        when(userService.findAll(any())).thenReturn(USER_DTO_PAGE);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO_PAGE)));

        verify(userService).findAll(any());
    }

    @Test
    void getAllByFlightId_ShouldReturnUsers() throws Exception {
        when(userService.findAllByFlightId(any(), any())).thenReturn(USER_DTO_PAGE);

        mockMvc.perform(get("/api/v1/users")
                        .param("flightId", String.valueOf(USER_ID)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO_PAGE)));

        verify(userService).findAllByFlightId(any(), any());
    }

    @Test
    void getById_ShouldReturnUser() throws Exception {
        when(userService.findById(USER_ID)).thenReturn(USER_DTO);

        mockMvc.perform(get("/api/v1/users/{id}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO)));
    }

    @Test
    void getById_ShouldReturnNotFound() throws Exception {
        when(userService.findById(NON_EXISTENT_USER_ID)).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/api/v1/users/{id}", NON_EXISTENT_USER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByEmail_ShouldReturnUser() throws Exception {
        when(userService.findByEmail(EMAIL)).thenReturn(USER_DTO);

        mockMvc.perform(get("/api/v1/users/email")
                        .param("email", EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO)));
    }

    @Test
    void getByEmail_ShouldReturnNotFound() throws Exception {
        when(userService.findByEmail(EMAIL)).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/api/v1/users/email")
                        .param("email", EMAIL))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_ShouldCreateUser() throws Exception {
        when(userService.create(USER_ID, CREATE_USER_REQUEST)).thenReturn(USER_DTO);

        mockMvc.perform(post("/api/v1/users")
                        .header("userId", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CREATE_USER_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO)));
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
    }

    @Test
    void delete_ShouldDeleteUser() throws Exception {
        doNothing().when(userService).delete(USER_ID, USER_ID);

        mockMvc.perform(delete("/api/v1/users/{id}", USER_ID)
                        .header("userId", USER_ID))
                .andExpect(status().isNoContent());
    }
}
