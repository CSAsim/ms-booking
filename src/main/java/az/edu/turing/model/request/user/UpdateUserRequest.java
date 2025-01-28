package az.edu.turing.model.request.user;

import az.edu.turing.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {

    private String name;

    private String surname;

    @Email(message = "Invalid email format")
    private String email;

    private String phoneNumber;

    private String password;

    private UserRole userRole;
}