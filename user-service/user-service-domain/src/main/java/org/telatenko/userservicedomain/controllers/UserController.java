package org.telatenko.userservicedomain.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.telatenko.userserviceapi.dto.UserDto;
import org.telatenko.userserviceapi.resources.UserResource;
import org.telatenko.userservicedomain.mappers.UserMapper;
import org.telatenko.userservicedomain.models.User;
import org.telatenko.userservicedomain.services.UserService;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UserResource {

    private final UserService userService;
    private final UserMapper userMapper;

    public List<UserDto> users() {
        List<User> user = userService.users();
        return userMapper.toDto(user);
    }

    public UserDto getUserById(@PathVariable("id") UUID id) {
        User user = userService.getById(id);
        UserDto userDto = userMapper.toDto(user);
        return userDto;
    }

    public UserDto createUser(@RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userService.saveUser(user));
    }

    public void deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
    }

    public UserDto updateUser(@PathVariable("id") UUID id, @RequestBody UserDto userDto) {
        User user = userService.updateUser(id, userDto.getName(), userDto.getEmail());
        return userMapper.toDto(user);
    }
}
