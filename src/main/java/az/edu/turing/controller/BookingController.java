package az.edu.turing.controller;

import az.edu.turing.model.BookingDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.model.request.booking.CreateBookingRequest;
import az.edu.turing.model.request.booking.UpdateBookingRequest;
import az.edu.turing.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAll(
            @RequestParam(value = "passengerId", required = false) Long passengerId) {
        if (passengerId != null) {
            return ResponseEntity.ok(bookingService.findAllByPassengerId(passengerId));
        }
        return ResponseEntity.ok(bookingService.findAll());
    }

    @GetMapping("/{flightId}")
    public ResponseEntity<List<BookingDto>> getAllByFlightId(@PathVariable("flightId") long flightId) {
        return ResponseEntity.ok(bookingService.findAllByFlightId(flightId));
    }

    @PostMapping
    public ResponseEntity<BookingDto> create(@RequestHeader long userId,
                                             @RequestBody @Valid CreateBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.createBooking(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDto> update(@RequestHeader Long userId,
                                             @PathVariable("id") Long bookingId,
                                             @RequestBody @Valid UpdateBookingRequest request) {
        return ResponseEntity.ok(bookingService.updateBooking(userId, bookingId, request));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> updateStatus(@RequestHeader Long userId,
                                                   @PathVariable("bookingId") Long bookingId,
                                                   @RequestBody @NotNull StatusMessage statusMessage) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(userId, bookingId, statusMessage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader Long userId,
                                       @PathVariable("id") Long bookingId) {
        bookingService.deleteBooking(userId, bookingId);
        return ResponseEntity.noContent().build();
    }
}
