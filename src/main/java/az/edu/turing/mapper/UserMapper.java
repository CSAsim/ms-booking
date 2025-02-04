package az.edu.turing.mapper;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.model.dto.UserDto;
import az.edu.turing.model.request.user.CreateUserRequest;
import az.edu.turing.model.request.user.UpdateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(UserEntity user);

    List<UserDto> toDto(List<UserEntity> entity);

    default Page<UserDto> toDto(Page<UserEntity> bookingEntities) {
        return bookingEntities.map(this::toDto);
    }

    UserEntity toEntity(UserDto userDto);

    UserEntity toEntity(CreateUserRequest request);

    UserEntity toEntity(UpdateUserRequest request);
}
