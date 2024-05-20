package org.telatenko.addressservicedomain.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.telatenko.addressserviceapi.dto.AddressDto;
import org.telatenko.addressserviceapi.resources.AddressResource;
import org.telatenko.addressservicedomain.mappers.AddressMapper;
import org.telatenko.addressservicedomain.models.Address;
import org.telatenko.addressservicedomain.services.AddressService;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AddressController implements AddressResource {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    public List<AddressDto> addresses() {
        List<Address> address = addressService.addresses();
        return addressMapper.toDtos(address);
    }

    public AddressDto getAddressById(@PathVariable("id") UUID id) {
        Address address = addressService.getById(id);
        AddressDto addressDto = addressMapper.toDto(address);
        return addressDto;
    }

    public AddressDto createAddress(@RequestBody AddressDto addressDto) {
        Address address = addressMapper.toEntity(addressDto);
        return addressMapper.toDto(addressService.saveAddress(address));
    }

    public void deleteAddress(@PathVariable("id") UUID id) {
        addressService.deleteAddress(id);
    }

    public AddressDto updateAddress(@PathVariable("id") UUID id, @RequestBody AddressDto addressDto) {
        Address address = addressService.updateAddress(id, addressDto.getStreet(), addressDto.getCity(),
                addressDto.getZipCode());
        return addressMapper.toDto(address);
    }
}
