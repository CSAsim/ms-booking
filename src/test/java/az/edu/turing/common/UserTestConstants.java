package az.edu.turing.common;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.model.UserDto;
import az.edu.turing.model.enums.UserRole;
import az.edu.turing.model.request.user.CreateUserRequest;
import az.edu.turing.model.request.user.UpdateUserRequest;
import org.springframework.data.domain.*;

import java.util.List;

public interface UserTestConstants {

    Long USER_ID = 1L;
    Long NON_EXISTENT_USER_ID = 999L;
    String NAME = "test_name";
    String SURNAME = "test_surname";
    String EMAIL = "test@example.com";
    String NEW_USER_EMAIL = "newuser@example.com";
    String PHONE_NUMBER = "+123456789";
    String PASSWORD = "securePassword123";
    UserRole USER_ROLE = UserRole.USER;
    boolean IS_DELETED = false;

    int PAGE = 1;
    int SIZE = 4;
    String SORT_BY = "id";

    Pageable PAGEABLE = PageRequest.of(PAGE, SIZE, Sort.by(SORT_BY));

    UserEntity USER_ENTITY = UserEntity.builder()
            .id(USER_ID)
            .name(NAME)
            .surname(SURNAME)
            .email(EMAIL)
            .phoneNumber(PHONE_NUMBER)
            .password(PASSWORD)
            .userRole(USER_ROLE)
            .isDeleted(IS_DELETED)
            .build();

    UserDto USER_DTO = UserDto.builder()
            .id(USER_ID)
            .name(NAME)
            .surname(SURNAME)
            .email(EMAIL)
            .phoneNumber(PHONE_NUMBER)
            .userRole(USER_ROLE)
            .build();

    CreateUserRequest CREATE_USER_REQUEST = CreateUserRequest.builder()
            .name(NAME)
            .surname(SURNAME)
            .email(EMAIL)
            .phoneNumber(PHONE_NUMBER)
            .password(PASSWORD)
            .userRole(USER_ROLE)
            .build();

    UpdateUserRequest UPDATE_USER_REQUEST = UpdateUserRequest.builder()
            .name("updated_name")
            .surname("updated_surname")
            .phoneNumber("+987654321")
            .build();

    Page<UserEntity> USER_ENTITY_PAGE = new PageImpl<>(List.of(USER_ENTITY), PAGEABLE, 1);
    Page<UserDto> USER_DTO_PAGE = new PageImpl<>(List.of(USER_DTO), PAGEABLE, 1);
}
