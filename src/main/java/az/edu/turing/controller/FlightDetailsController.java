package az.edu.turing.controller;

import az.edu.turing.model.FlightDetailsDto;
import az.edu.turing.model.request.flightDetails.CreateFlightDetailsRequest;
import az.edu.turing.model.request.flightDetails.UpdateFlightDetailsRequest;
import az.edu.turing.service.FlightDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flight/details")
public class FlightDetailsController {

    private final FlightDetailsService flightDetailsService;

    @GetMapping
    public ResponseEntity<Page<FlightDetailsDto>> getAll(
            @PageableDefault(size = 10)Pageable pageable) {
        return ResponseEntity.ok(flightDetailsService.findAll(pageable));
    }

    @GetMapping("/{flightId}/details")
    public ResponseEntity<FlightDetailsDto> getByFlightId(@PathVariable("flightId") Long flightId) {
        return ResponseEntity.ok(flightDetailsService.findByFlightId(flightId));
    }

    @PostMapping
    public ResponseEntity<FlightDetailsDto> create(@RequestParam(value = "userId") Long userId,
                                                   @Valid CreateFlightDetailsRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flightDetailsService.create(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightDetailsDto> update(@RequestParam(value = "userId") Long userId,
                                                   @PathVariable("id") Long id,
                                                   @Valid UpdateFlightDetailsRequest request) {
        return ResponseEntity.ok(flightDetailsService.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestParam(value = "userId") Long userId,
                                       @RequestParam(value = "flightId", required = false) Long flightId,
                                       @PathVariable("id") Long id) {
        if(flightId != null) {
            flightDetailsService.deleteByFlightId(userId, flightId);
        } else {
            flightDetailsService.delete(userId, id);
        }
        return ResponseEntity.noContent().build();
    }
}
