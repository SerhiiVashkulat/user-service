package ua.vahskulat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ua.vahskulat.dto.request.UserCreateRequestDTO;
import ua.vahskulat.dto.response.UserResponseDto;
import ua.vahskulat.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id",ignore = true)
    User toUser(UserCreateRequestDTO userDto);
    UserResponseDto toDTO(User user);
    List<UserResponseDto> toListDTO(List<User> listUsers);

}
