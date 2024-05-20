package org.telatenko.userservicedomain.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telatenko.addressserviceapi.resources.client.AddressClient;
import org.telatenko.userservicedomain.kafka.Producer;
import org.telatenko.userservicedomain.models.User;
import org.telatenko.userservicedomain.repositories.UserRepositories;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepositories userRepositories;
    private final AddressClient addressClient;
    private final Producer producer;

    public List<User> users() {
        log.info("Retrieving all users");
        return userRepositories.findAll();
    }

    public User getById(UUID id) {
        log.info("Retrieving user with id: {}", id);
        return userRepositories.findById(id).orElseThrow();
    }


    public User saveUser(User user) {
        log.info("Saving user: {}", user);
        try {
            var address = addressClient.getAddressById(user.getAddressId());
            if (address != null) {
                producer.sendMessage(user);
                return userRepositories.save(user);
            } else {
                log.error("Address not found for user: {}", user);
                throw new RuntimeException("Address not found");
            }
        } catch (FeignException e) {
            if (e.status() == 500) {
                log.error("Internal Server Error while saving user: {}", user, e);
                throw new RuntimeException("Internal Server Error");
            } else {
                log.error("Feign error while saving user: {}", user, e);
                throw new RuntimeException("Feign error: " + e.getMessage());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(UUID id) {
        log.info("Deleting user with id: {}", id);
        userRepositories.deleteById(id);
    }

    public User updateUser(UUID id, String name, String email) {
        log.info("Updating user with id: {}, name: {}, email: {}", id, name, email);
        userRepositories.updateUser(id, name, email);
        return userRepositories.findById(id).orElseThrow();
    }
}