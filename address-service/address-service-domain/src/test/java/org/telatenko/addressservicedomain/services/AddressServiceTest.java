package org.telatenko.addressservicedomain.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.telatenko.addressservicedomain.models.Address;
import org.telatenko.addressservicedomain.repositories.AddressRepositories;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepositories addressRepositories;

    @InjectMocks
    private AddressService addressService;

    @Test
    void addresses() {
        List<Address> addresses = Arrays.asList(new Address(), new Address());
        when(addressRepositories.findAll()).thenReturn(addresses);

        List<Address> result = addressService.addresses();

        assertEquals(addresses, result);
        verify(addressRepositories, times(1)).findAll();
    }

    @Test
    void getById() {
        UUID id = UUID.randomUUID();
        Address address = new Address();
        when(addressRepositories.findById(id)).thenReturn(Optional.of(address));

        Address result = addressService.getById(id);

        assertEquals(address, result);
        verify(addressRepositories, times(1)).findById(id);
    }

    @Test
    void saveAddress() {
        Address address = new Address();
        when(addressRepositories.save(address)).thenReturn(address);

        Address result = addressService.saveAddress(address);

        assertEquals(address, result);
        verify(addressRepositories, times(1)).save(address);
    }

    @Test
    void deleteAddress() {
        UUID id = UUID.randomUUID();

        addressService.deleteAddress(id);

        verify(addressRepositories, times(1)).deleteById(id);
    }

    @Test
    void updateAddress() {
        UUID id = UUID.randomUUID();
        String street = "Test Street";
        String city = "Test City";
        String zipCode = "12345";
        Address address = new Address();
        when(addressRepositories.findById(id)).thenReturn(Optional.of(address));

        Address result = addressService.updateAddress(id, street, city, zipCode);

        assertEquals(address, result);
        verify(addressRepositories, times(1)).updateAddress(id, street, city, zipCode);
        verify(addressRepositories, times(1)).findById(id);
    }
}