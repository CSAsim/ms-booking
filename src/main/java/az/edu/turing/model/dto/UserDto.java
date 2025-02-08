package az.edu.turing.model.dto;

import az.edu.turing.model.enums.UserRole;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private UserRole userRole;
}
