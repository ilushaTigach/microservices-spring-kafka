package org.telatenko.addressserviceapi.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.telatenko.addressserviceapi.dto.AddressDto;
import java.util.List;
import java.util.UUID;

@Tag(name = "address resource", description = "Интерфейс взаимодействия с Addresses")
public interface AddressResource {

    @Operation(summary = "Вызов всего списка адресов")
    @GetMapping("/api/v1/addresses")
    List<AddressDto> addresses();

    @Operation(summary = "Вызов адреса по id")
    @GetMapping("/api/v1/address/{id}")
    AddressDto getAddressById(@PathVariable("id") UUID id);

    @Operation(summary = "Создание нового адреса и добавление его в базу данных")
    @PostMapping("/api/v1/address")
    AddressDto createAddress(@RequestBody AddressDto addressDto);

    @Operation(summary = "Удаление адреса из базы данных по id")
    @DeleteMapping("/api/v1/address/{id}")
    void deleteAddress(@PathVariable("id") UUID id);

    @Operation(summary = "Изменение параметров адреса")
    @PatchMapping("/api/v1/address/{id}")
    AddressDto updateAddress(@PathVariable("id") UUID id, @RequestBody AddressDto addressDto);
}
