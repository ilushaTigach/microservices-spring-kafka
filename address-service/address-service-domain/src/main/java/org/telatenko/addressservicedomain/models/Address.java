package org.telatenko.addressservicedomain.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.UUID;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Address {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "zipcode")
    private String zipCode;
}
