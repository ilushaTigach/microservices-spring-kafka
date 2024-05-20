package org.telatenko.userservicedomain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telatenko.userservicedomain.models.User;
import java.util.UUID;

@Repository
public interface UserRepositories extends JpaRepository<User, UUID> {

    @Modifying
    @Transactional
    @Query(value = """ 
            UPDATE users
            SET name = COALESCE(NULLIF(?2, ''), name),
                email = COALESCE(NULLIF(?3, ''), email)
                WHERE id = ?1""", nativeQuery = true)
    void updateUser(UUID id, String name, String email);
}
