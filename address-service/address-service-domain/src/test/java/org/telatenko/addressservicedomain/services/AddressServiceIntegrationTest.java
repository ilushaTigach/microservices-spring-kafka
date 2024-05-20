package org.telatenko.addressservicedomain.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.telatenko.addressserviceapi.dto.AddressDto;
import org.telatenko.addressservicedomain.models.Address;
import org.telatenko.addressservicedomain.repositories.AddressRepositories;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class AddressServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressRepositories addressRepositories;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16"
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgres::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgres::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgres::getPassword);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @SneakyThrows
    void getById() {
        Address address = new Address(UUID.fromString("5f30f1c3-e70d-42a0-a3d3-58a5c2d50d02"), "street", "city", "zipCode");
        addressRepositories.save(address);

        MvcResult result = mockMvc.perform(get("/api/v1/address/{id}", address.getId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AddressDto responseAddressDto = new ObjectMapper().readValue(responseBody, AddressDto.class);

        assertEquals(address.getId(), responseAddressDto.getId());
        assertEquals(address.getStreet(), responseAddressDto.getStreet());
        assertEquals(address.getCity(), responseAddressDto.getCity());
        assertEquals(address.getZipCode(), responseAddressDto.getZipCode());
    }

    @Test
    @SneakyThrows
    void saveAddress() {
        AddressDto addressDto = new AddressDto(null, "street", "city", "zipCode");

        MvcResult result = mockMvc.perform(post("/api/v1/address")
                        .content(asJsonString(addressDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AddressDto savedAddressDto = new ObjectMapper().readValue(responseBody, AddressDto.class);
        UUID savedId = savedAddressDto.getId();

        Optional<Address> savedAddress = addressRepositories.findById(savedId);
        assertTrue(savedAddress.isPresent());
        assertEquals(addressDto.getStreet(), savedAddress.get().getStreet());
        assertEquals(addressDto.getCity(), savedAddress.get().getCity());
        assertEquals(addressDto.getZipCode(), savedAddress.get().getZipCode());
    }


    @Test
    @SneakyThrows
    void deleteAddress() {
        Address address = new Address(UUID.fromString("5f30f1c3-e70d-42a0-a3d3-58a5c2d50d02"), "street", "city", "zipCode");
        addressRepositories.save(address);

        mockMvc.perform(delete("/api/v1/address/{id}", address.getId()))
                .andExpect(status().is2xxSuccessful());

        Optional<Address> deletedAddress = addressRepositories.findById(address.getId());
        assertFalse(deletedAddress.isPresent());
    }

    @Test
    @SneakyThrows
    void updateAddress() {
        Address address = addressRepositories.save(new Address(null, "street", "city", "zipCode"));

        AddressDto addressDto = new AddressDto(address.getId(), "streetNew", "cityNew", "zipCodeNew");

        MvcResult result = mockMvc.perform(patch("/api/v1/address/{id}", address.getId())
                        .content(asJsonString(addressDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AddressDto savedAddressDto = new ObjectMapper().readValue(responseBody, AddressDto.class);
        UUID savedId = savedAddressDto.getId();

        Optional<Address> updatedAddress = addressRepositories.findById(savedId);
        assertTrue(updatedAddress.isPresent());
        assertEquals(addressDto.getStreet(), updatedAddress.get().getStreet());
        assertEquals(addressDto.getCity(), updatedAddress.get().getCity());
        assertEquals(addressDto.getZipCode(), updatedAddress.get().getZipCode());
    }
}