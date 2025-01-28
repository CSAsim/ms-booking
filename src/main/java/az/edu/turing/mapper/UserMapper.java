package az.edu.turing.mapper;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.model.UserDto;
import az.edu.turing.model.request.user.CreateUserRequest;
import az.edu.turing.model.request.user.UpdateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(UserEntity user);

    UserEntity toEntity(UserDto userDto);

    UserEntity fromCreateRequest(CreateUserRequest createUserRequest);

    UserEntity fromUpdateRequest(UpdateUserRequest updateUserRequest, UserEntity existingUser);
}
