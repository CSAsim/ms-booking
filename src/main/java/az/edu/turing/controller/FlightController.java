package az.edu.turing.controller;

import az.edu.turing.model.dto.FlightDetailDto;
import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.model.enums.FlightStatus;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import az.edu.turing.model.request.flight.UpdateFlightRequest;
import az.edu.turing.model.request.flightDetails.CreateFlightDetailRequest;
import az.edu.turing.model.request.flightDetails.UpdateFlightDetailRequest;
import az.edu.turing.service.FlightDetailService;
import az.edu.turing.service.FlightService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightService flightService;
    private final FlightDetailService flightDetailsService;


    @GetMapping
    public ResponseEntity<Page<FlightDto>> getAll(
            @RequestParam(required = false) String departure,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) LocalDateTime departureTime,
            @RequestParam(required = false) LocalDateTime arrivalTime,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<FlightDto> flights = flightService.findAll(departure, destination, departureTime, arrivalTime, pageable);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Page<FlightDto>> getAllIn24Hours(@PageableDefault(size = 10) Pageable pageable) {
        Page<FlightDto> flights = flightService.findAllIn24Hours(pageable);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDto> getById(@PathVariable Long id) {
        FlightDto flightDto = flightService.findById(id);
        return ResponseEntity.ok(flightDto);
    }

    @PostMapping
    public ResponseEntity<FlightDto> create(@RequestHeader Long id, @Valid @RequestBody CreateFlightRequest createFlightRequest) {
        FlightDto flightDto = flightService.create(id, createFlightRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(flightDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightDto> update(@RequestHeader Long userId, @PathVariable Long id,
                                            @Valid @RequestBody UpdateFlightRequest updateFlightRequest) {
        FlightDto updatedFlight = flightService.update(userId, id, updateFlightRequest);
        return ResponseEntity.ok(updatedFlight);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FlightDto> updateFlightStatus(@RequestHeader Long userId, @PathVariable Long id,
                                                        @NotNull @RequestParam FlightStatus flightStatus) {
        FlightDto updatedFlight = flightService.updateFlightStatus(userId, id, flightStatus);
        return ResponseEntity.ok(updatedFlight);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader Long userId, @PathVariable Long id) {
        flightService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    // Flight Details endpoints

    @GetMapping("/{flightId}/details")
    public ResponseEntity<FlightDetailDto> getDetailsByFlightId(@PathVariable("flightId") Long flightId) {
        FlightDetailDto flightDetails = flightDetailsService.findByFlightId(flightId);
        return ResponseEntity.ok(flightDetails);
    }

    @PostMapping("/{flightId}/details")
    public ResponseEntity<FlightDetailDto> createDetails(
            @PathVariable("flightId") Long flightId,
            @RequestParam(value = "userId") Long userId,
            @Valid @RequestBody CreateFlightDetailRequest request) {
        request.setFlightId(flightId);
        FlightDetailDto createdDetails = flightDetailsService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDetails);
    }

    @PutMapping("/{flightId}/details/{id}")
    public ResponseEntity<FlightDetailDto> updateDetails(
            @RequestParam(value = "userId") Long userId,
            @PathVariable("flightId") Long flightId,
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateFlightDetailRequest request) {
        request.setFlightId(flightId);
        FlightDetailDto updatedDetails = flightDetailsService.updateByFlightId(userId, id, request);
        return ResponseEntity.ok(updatedDetails);
    }

    @DeleteMapping("/{flightId}/details/{id}")
    public ResponseEntity<Void> deleteDetails(
            @RequestParam(value = "userId") Long userId,
            @PathVariable("flightId") Long flightId,
            @PathVariable("id") Long id) {
        flightDetailsService.deleteByFlightId(userId, flightId);
        return ResponseEntity.noContent().build();
    }
}
