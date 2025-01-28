package az.edu.turing.model.request.user;

import az.edu.turing.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$")
    private String password;

    private UserRole userRole;
}