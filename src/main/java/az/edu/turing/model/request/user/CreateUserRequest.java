package az.edu.turing.model.request.user;

import az.edu.turing.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    private String surname;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    private String phoneNumber;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotNull(message = "User role must be specified")
    private UserRole userRole;
}