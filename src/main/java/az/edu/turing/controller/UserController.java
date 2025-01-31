package az.edu.turing.controller;

import az.edu.turing.model.UserDto;
import az.edu.turing.model.request.user.CreateUserRequest;
import az.edu.turing.model.request.user.UpdateUserRequest;
import az.edu.turing.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAll(@RequestHeader Long userId,
                                                @RequestParam(required = false) Long flightId,
                                                @PageableDefault(size = 10) Pageable pageable) {
        if (flightId != null) {
            return ResponseEntity.ok(userService.findAllByFlightId(userId, flightId, pageable));
        }
        return ResponseEntity.ok(userService.findAll(userId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@RequestHeader Long userId, @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(userId, id));
    }

    @GetMapping("/email")
    public ResponseEntity<UserDto> getByEmail(@RequestHeader Long userId, @RequestParam String email) {
        return ResponseEntity.ok(userService.findByEmail(userId, email));
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestHeader Long userId, @Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.create(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@RequestHeader Long userId, @PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader Long userId, @PathVariable Long id) {
        userService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
