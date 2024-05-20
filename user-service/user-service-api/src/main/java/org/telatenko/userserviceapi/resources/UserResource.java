package org.telatenko.userserviceapi.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.telatenko.userserviceapi.dto.UserDto;
import java.util.List;
import java.util.UUID;

@Tag(name = "user resource", description = "Интерфейс взаимодействия с Users")
public interface UserResource {

    @Operation(summary = "Вызов всего списка жильцов")
    @GetMapping("/api/v1/users")
    List<UserDto> users();

    @Operation(summary = "Вызов жильца по id")
    @GetMapping("/api/v1/user/{id}")
    UserDto getUserById(@PathVariable("id") UUID id);

    @Operation(summary = "Создание нового жильца и добавление его в базу данных")
    @PostMapping("/api/v1/user")
    UserDto createUser(@RequestBody UserDto userDto);

    @Operation(summary = "Удаление жильца из базы данных по id")
    @DeleteMapping("/api/v1/user/{id}")
    void deleteUser(@PathVariable("id") UUID id);

    @Operation(summary = "Изменение параметров жильца")
    @PatchMapping("/api/v1/user/{id}")
    UserDto updateUser(@PathVariable("id") UUID id, @RequestBody UserDto userDto);
}
