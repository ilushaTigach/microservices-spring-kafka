package org.telatenko.addressservicedomain.mappers;


import org.mapstruct.Mapper;
import org.telatenko.addressserviceapi.dto.AddressDto;
import org.telatenko.addressservicedomain.models.Address;
import java.util.List;


@Mapper
public interface AddressMapper {

    List<AddressDto> toDtos(List<Address> addresses);

    AddressDto toDto(Address address);

    Address toEntity(AddressDto addressDto);
}
