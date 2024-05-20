package org.telatenko.userservicedomain.services;

import org.junit.jupiter.api.Test;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.telatenko.userserviceapi.dto.UserDto;
import org.telatenko.userservicedomain.models.User;
import org.telatenko.userservicedomain.repositories.UserRepositories;
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
class UserServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepositories userRepositories;

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
        User user = new User(UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"), "name", "email", UUID.fromString("4f30f1c3-e70d-42a0-a3d3-58a5c2d50d03"));
        userRepositories.save(user);

        MvcResult result = mockMvc.perform(get("/api/v1/user/{id}", user.getId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        ;

        String responseBody = result.getResponse().getContentAsString();
        UserDto responseUserDto = new ObjectMapper().readValue(responseBody, UserDto.class);

        assertEquals(user.getId(), responseUserDto.getId());
        assertEquals(user.getName(), responseUserDto.getName());
        assertEquals(user.getEmail(), responseUserDto.getEmail());
        assertEquals(user.getAddressId(), responseUserDto.getAddressId());
    }

    @Test
    @SneakyThrows
    void saveUser() {
        UserDto userDto = new UserDto(null, "name", "email", UUID.fromString("5f30f1c3-e70d-42a0-a3d3-58a5c2d50d02"));

        MvcResult result = mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        UserDto savedUserDto = new ObjectMapper().readValue(responseBody, UserDto.class);
        UUID savedId = savedUserDto.getId();

        Optional<User> savedUser = userRepositories.findById(savedId);
        assertTrue(savedUser.isPresent());
        assertEquals(userDto.getName(), savedUser.get().getName());
        assertEquals(userDto.getEmail(), savedUser.get().getEmail());
        assertEquals(userDto.getAddressId(), savedUser.get().getAddressId());
    }

    @Test
    @SneakyThrows
    void deleteUser() {
        User user = new User(UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"), "name", "email", UUID.fromString("4f30f1c3-e70d-42a0-a3d3-58a5c2d50d03"));
        userRepositories.save(user);

        mockMvc.perform(delete("/api/v1/user/{id}", user.getId()))
                .andExpect(status().is2xxSuccessful());

        Optional<User> deletedUser = userRepositories.findById(user.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    @SneakyThrows
    void updateUser() {
        User user = userRepositories.save(new User(null, "name", "email", UUID.fromString("4f30f1c3-e70d-42a0-a3d3-58a5c2d50d03")));

        UserDto userDto = new UserDto(user.getId(), "nameNew", "emailNew", UUID.randomUUID());

        MvcResult result = mockMvc.perform(patch("/api/v1/user/{id}", user.getId())
                        .content(asJsonString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        UserDto savedUserDto = new ObjectMapper().readValue(responseBody, UserDto.class);
        UUID savedId = savedUserDto.getId();

        Optional<User> updatedUser = userRepositories.findById(savedId);
        assertTrue(updatedUser.isPresent());
        assertEquals(userDto.getName(), updatedUser.get().getName());
        assertEquals(userDto.getEmail(), updatedUser.get().getEmail());
    }
}