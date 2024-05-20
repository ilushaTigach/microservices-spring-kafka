package org.telatenko.addressservicedomain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telatenko.addressservicedomain.models.Address;
import java.util.UUID;

@Repository
public interface AddressRepositories extends JpaRepository<Address, UUID> {

    @Modifying
    @Transactional
    @Query(value = """ 
            UPDATE addresses
            SET street = COALESCE(NULLIF(?2, ''), street),
                city = COALESCE(NULLIF(?3, ''), city),
                zipCode = COALESCE(NULLIF(?4, ''), zipCode)
                WHERE id = ?1""", nativeQuery = true)
    void updateAddress(UUID id, String street, String city, String zipCode);
}
