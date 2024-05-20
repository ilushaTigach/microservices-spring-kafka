package org.telatenko.userservicedomain.mappers;

import org.mapstruct.Mapper;
import org.telatenko.userserviceapi.dto.UserDto;
import org.telatenko.userservicedomain.models.User;
import java.util.List;

@Mapper
public interface UserMapper {

    List<UserDto> toDto(List<User> users);

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
