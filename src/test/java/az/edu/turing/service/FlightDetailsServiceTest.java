//package az.edu.turing.service;
//
//import az.edu.turing.domain.repository.FlightDetailsRepository;
//import az.edu.turing.domain.repository.FlightRepository;
//import az.edu.turing.domain.repository.UserRepository;
//import az.edu.turing.exception.NotFoundException;
//import az.edu.turing.mapper.FlightDetailsMapper;
//import az.edu.turing.model.dto.FlightDetailDto;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//
//import java.util.List;
//import java.util.Optional;
//
//import static az.edu.turing.common.FlightDetailsConstants.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.times;
//
//@ExtendWith(MockitoExtension.class)
//class FlightDetailsServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private FlightRepository flightRepository;
//
//    @Mock
//    private FlightDetailsRepository flightDetailsRepository;
//
//    @Spy
//    private FlightDetailsMapper mapper;
//
//    @InjectMocks
//    private FlightDetailService flightDetailsService;
//
//    @Test
//    void findAll_Should_ReturnSuccess() {
//        given(flightDetailsRepository.findAll(PAGEABLE)).willReturn(FLIGHT_DETAILS_ENTITY_PAGE);
//        given(mapper.toDto(FLIGHT_DETAILS_ENTITY_PAGE)).willReturn(FLIGHT_DETAILS_DTO_PAGE);
//
//        Page<FlightDetailDto> result = flightDetailsService.findAll(PAGEABLE);
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        assertEquals(List.of(FLIGHT_DETAILS_DTO), result.getContent());
//
//        then(flightDetailsRepository).should(times(1)).findAll(PAGEABLE);
//    }
//
//    @Test
//    void findByFlightId_ShouldReturn_Success() {
//        given(flightDetailsRepository.findByFlightId(FLIGHT_ID)).willReturn(Optional.of(FLIGHT_DETAILS_ENTITY));
//        given(mapper.toDto(FLIGHT_DETAILS_ENTITY)).willReturn(FLIGHT_DETAILS_DTO);
//
//        FlightDetailDto result = flightDetailsService.findByFlightId(FLIGHT_ID);
//
//        assertNotNull(result);
//        assertEquals(FLIGHT_DETAILS_DTO, result);
//
//        then(flightDetailsRepository).should(times(1)).findByFlightId(FLIGHT_ID);
//    }
//
//    @Test
//    void findAllByFlightId_Should_ThrowNotFoundException_When_FlightIdNotFound() {
//        given(flightDetailsRepository.findByFlightId(FLIGHT_ID)).willReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(NotFoundException.class,
//                () -> flightDetailsService.findByFlightId(FLIGHT_ID));
//
//        assertEquals("Flight not found for id: " + FLIGHT_ID, exception.getMessage());
//
//        then(flightDetailsRepository).should(times(1)).findByFlightId(FLIGHT_ID);
//    }
//
//    @Test
//    void crateFlightDetails_Should_ReturnSuccess() {
//        given(userRepository.existsById(USER_ID)).willReturn(true);
//        given(flightRepository.existsById(FLIGHT_ID)).willReturn(true);
//        given(flightDetailsRepository.save(FLIGHT_DETAILS_ENTITY)).willReturn(FLIGHT_DETAILS_ENTITY);
//        given(mapper.toEntity(CREATE_FLIGHT_DETAILS_REQUEST)).willReturn(FLIGHT_DETAILS_ENTITY);
//        given(mapper.toDto(FLIGHT_DETAILS_ENTITY)).willReturn(FLIGHT_DETAILS_DTO);
//
//        FlightDetailDto result = flightDetailsService.create(USER_ID, CREATE_FLIGHT_DETAILS_REQUEST);
//
//        assertNotNull(result);
//        assertEquals(FLIGHT_DETAILS_DTO, result);
//
//        then(userRepository).should(times(1)).existsById(USER_ID);
//        then(flightRepository).should(times(1)).existsById(FLIGHT_ID);
//        then(flightDetailsRepository).should(times(1)).save(FLIGHT_DETAILS_ENTITY);
//    }
//
//    @Test
//    void crateFlightDetails_Should_ThrowNotFoundException_When_UserIdNotFound() {
//        given(userRepository.existsById(USER_ID)).willReturn(false);
//
//        NotFoundException exception = assertThrows(NotFoundException.class,
//                () -> flightDetailsService.create(USER_ID, CREATE_FLIGHT_DETAILS_REQUEST));
//
//        assertEquals("User not found for id: " + USER_ID, exception.getMessage());
//
//        then(userRepository).should(times(1)).existsById(USER_ID);
//        then(flightRepository).should(never()).existsById(FLIGHT_ID);
//        then(flightDetailsRepository).should(never()).save(FLIGHT_DETAILS_ENTITY);
//    }
//
//    @Test
//    void crateFlightDetails_Should_ThrowNotFoundException_When_FlightIdNotFound() {
//        given(userRepository.existsById(USER_ID)).willReturn(true);
//        given(flightRepository.existsById(FLIGHT_ID)).willReturn(false);
//
//        NotFoundException exception = assertThrows(NotFoundException.class,
//                () -> flightDetailsService.create(USER_ID, CREATE_FLIGHT_DETAILS_REQUEST));
//
//        assertEquals("Flight not found for id: " + FLIGHT_ID, exception.getMessage());
//
//        then(userRepository).should(times(1)).existsById(USER_ID);
//        then(flightRepository).should(times(1)).existsById(FLIGHT_ID);
//        then(flightDetailsRepository).should(never()).save(FLIGHT_DETAILS_ENTITY);
//    }
//
//    @Test
//    void updateFlightDetails_Should_ReturnSuccess() {
//        given(flightRepository.existsById(FLIGHT_ID)).willReturn(true);
//        given(userRepository.existsById(USER_ID)).willReturn(true);
//        given(flightDetailsRepository.existsById(ID)).willReturn(true);
//        given(mapper.toEntity(UPDATE_FLIGHT_DETAILS_REQUEST)).willReturn(FLIGHT_DETAILS_ENTITY);
//        given(mapper.toDto(FLIGHT_DETAILS_ENTITY)).willReturn(FLIGHT_DETAILS_DTO);
//        given(flightDetailsRepository.save(FLIGHT_DETAILS_ENTITY)).willReturn(FLIGHT_DETAILS_ENTITY);
//
//        FlightDetailDto result = flightDetailsService.updateByFlightId(USER_ID, ID, UPDATE_FLIGHT_DETAILS_REQUEST);
//
//        assertNotNull(result);
//        assertEquals(FLIGHT_DETAILS_DTO, result);
//
//        then(userRepository).should(times(1)).existsById(USER_ID);
//        then(flightDetailsRepository).should(times(1)).existsById(ID);
//        then(flightDetailsRepository).should(times(1)).save(FLIGHT_DETAILS_ENTITY);
//    }
//
//    @Test
//    void updateFlightDetails_Should_ThrowNotFoundException_When_UserIdNotFound () {
//        given(userRepository.existsById(USER_ID)).willReturn(false);
//
//        NotFoundException exception = assertThrows(NotFoundException.class,
//                () -> flightDetailsService.updateByFlightId(USER_ID, ID, UPDATE_FLIGHT_DETAILS_REQUEST));
//
//        assertEquals("User not found for id: " + USER_ID, exception.getMessage());
//
//        then(userRepository).should(times(1)).existsById(USER_ID);
//        then(flightDetailsRepository).should(never()).existsById(ID);
//        then(flightDetailsRepository).should(never()).save(FLIGHT_DETAILS_ENTITY);
//    }
//
//    @Test
//    void deleteByFlightId_Should_ReturnSuccess() {
//        given(userRepository.existsById(USER_ID)).willReturn(true);
//        given(flightRepository.existsById(FLIGHT_ID)).willReturn(true);
//
//        flightDetailsService.deleteByFlightId(USER_ID, FLIGHT_ID);
//
//        then(userRepository).should(times(1)).existsById(USER_ID);
//        then(flightRepository).should(times(1)).existsById(FLIGHT_ID);
//        then(flightDetailsRepository).should(times(1)).deleteByFlightId(FLIGHT_ID);
//    }
//
//    @Test
//    void deleteByFlightId_Should_ThrowNotFoundException_When_FlightDetailsIdNotFound() {
//        given(userRepository.existsById(USER_ID)).willReturn(true);
//        given(flightRepository.existsById(FLIGHT_ID)).willReturn(false);
//
//        NotFoundException exception = assertThrows(NotFoundException.class,
//                () -> flightDetailsService.deleteByFlightId(USER_ID, FLIGHT_ID));
//
//        assertEquals("Flight not found for id: " + FLIGHT_ID, exception.getMessage());
//
//        then(userRepository).should(times(1)).existsById(USER_ID);
//        then(flightRepository).should(times(1)).existsById(FLIGHT_ID);
//        then(flightDetailsRepository).should(never()).deleteById(ID);
//    }
//
//}
