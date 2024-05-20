package org.telatenko.userservicedomain.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.telatenko.addressserviceapi.dto.AddressDto;
import org.telatenko.addressserviceapi.resources.client.AddressClient;
import org.telatenko.userservicedomain.models.User;
import org.telatenko.userservicedomain.repositories.UserRepositories;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositories userRepositories;

    @Mock
    private AddressClient addressClient;

    @InjectMocks
    private UserService userService;

    @Test
    void users() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepositories.findAll()).thenReturn(users);

        List<User> result = userService.users();

        assertEquals(users, result);
        verify(userRepositories, times(1)).findAll();
    }

    @Test
    void getById() {
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userRepositories.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getById(id);

        assertEquals(user, result);
        verify(userRepositories, times(1)).findById(id);
    }

    @Test
    void testSaveUser_AddressFound() {
        User user = new User();
        user.setAddressId(UUID.randomUUID());
        when(addressClient.getAddressById(user.getAddressId())).thenReturn(new AddressDto(UUID.randomUUID(), "street", "city", "zipCode"));
        when(userRepositories.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        assertEquals(user, result);
        verify(addressClient, times(1)).getAddressById(user.getAddressId());
        verify(userRepositories, times(1)).save(user);
    }

    @Test
    void deleteUser() {
        UUID id = UUID.randomUUID();

        userService.deleteUser(id);

        verify(userRepositories, times(1)).deleteById(id);
    }

    @Test
    void updateUser() {
        UUID id = UUID.randomUUID();
        String name = "Test Name";
        String email = "test@example.com";
        User user = new User();
        when(userRepositories.findById(id)).thenReturn(Optional.of(user));

        User result = userService.updateUser(id, name, email);

        assertEquals(user, result);
        verify(userRepositories, times(1)).updateUser(id, name, email);
        verify(userRepositories, times(1)).findById(id);
    }
}