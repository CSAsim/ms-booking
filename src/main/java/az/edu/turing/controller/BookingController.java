package az.edu.turing.controller;

import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.dto.PageableResponseDto;
import az.edu.turing.model.enums.BookingStatus;
import az.edu.turing.model.request.booking.CreateBookingRequest;
import az.edu.turing.model.request.booking.UpdateBookingRequest;
import az.edu.turing.service.BookingService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<PageableResponseDto<BookingDto>> getAll(@RequestHeader Long userId,
                                                                  @PageableDefault(size = 10) Pageable pageable) {
        Page<BookingDto> bookingPage = bookingService.findAll(userId, pageable);
        return ResponseEntity.ok(PageableResponseDto.of(bookingPage.getContent(),
                bookingPage.getPageable().getPageNumber(),
                bookingPage.getPageable().getPageSize(),
                bookingPage.getTotalElements(),
                bookingPage.getTotalPages()));
    }

    @GetMapping("/passenger/{id}")
    public ResponseEntity<PageableResponseDto<BookingDto>> getByPassengerId(@PathVariable("id") Long passengerId,
                                                                            @PageableDefault(size = 10) Pageable pageable) {
        Page<BookingDto> bookingPage = bookingService.findAllByPassengerId(passengerId, pageable);
        return ResponseEntity.ok(PageableResponseDto.of(bookingPage.getContent(),
                bookingPage.getPageable().getPageNumber(),
                bookingPage.getPageable().getPageSize(),
                bookingPage.getTotalElements(),
                bookingPage.getTotalPages()));

    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<PageableResponseDto<BookingDto>> getAllByFlightId(@RequestHeader Long userId,
                                                                            @PathVariable("flightId") Long flightId,
                                                                            @PageableDefault(size = 10) Pageable pageable) {
        Page<BookingDto> bookingPage = bookingService.findAllByFlightId(userId, flightId, pageable);
        return ResponseEntity.ok(PageableResponseDto.of(bookingPage.getContent(),
                bookingPage.getPageable().getPageNumber(),
                bookingPage.getPageable().getPageSize(),
                bookingPage.getTotalElements(),
                bookingPage.getTotalPages()));
    }

    @PostMapping
    public ResponseEntity<BookingDto> create(@RequestHeader Long userId,
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
                                                   @RequestParam @NotNull BookingStatus statusMessage) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(userId, bookingId, statusMessage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader Long userId,
                                       @PathVariable("id") Long bookingId) {
        bookingService.deleteBooking(userId, bookingId);
        return ResponseEntity.noContent().build();
    }
}
