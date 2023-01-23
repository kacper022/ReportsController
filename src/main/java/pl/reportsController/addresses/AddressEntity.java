package pl.reportsController.addresses;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.reportsController.customers.CustomerEntity;

import java.util.HashSet;
import java.util.Set;

@Table(name = "'address'")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable(name = "customerAddress", joinColumns = @JoinColumn(name = "address_id"), inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private Set<CustomerEntity> customerHomeAddress = new HashSet<>();


    private String city;
    private String zipCode;
    private String street;
    private String streetNumber;
    private String apartmentNumber;

    public AddressEntity(String street, String streetNumber){
        this.street = street;
        this.streetNumber = streetNumber;
    }

}
