package org.telatenko.addressservicedomain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telatenko.addressservicedomain.models.Address;
import org.telatenko.addressservicedomain.repositories.AddressRepositories;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepositories addressRepositories;

    public List<Address> addresses() {
        log.info("Retrieving all addresses");
        return addressRepositories.findAll();
    }

    public Address getById(UUID id) {
        log.info("Retrieving address with id: {}", id);
        return addressRepositories.findById(id).orElseThrow();
    }

    public Address saveAddress(Address address) {
        log.info("Saving address: {}", address);
        return addressRepositories.save(address);
    }

    public void deleteAddress(UUID id) {
        log.info("Deleting address with id: {}", id);
        addressRepositories.deleteById(id);
    }

    public Address updateAddress(UUID id, String street, String city, String zipCode) {
        log.info("Updating address with id: {}, street: {}, city: {}, zipCode{}", id, street, city, zipCode);
        addressRepositories.updateAddress(id, street, city, zipCode);
        return addressRepositories.findById(id).orElseThrow();
    }
}
