package pl.reportsController.customers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.reportsController.addresses.AddressEntity;
import pl.reportsController.addresses.AddressRepository;
import pl.reportsController.reports.ReportEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "`customer`")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private Long id_user;


    public CustomerEntity(String firstName, String lastName, Long id_user) {
        setFirstName(firstName);
        setLastName(lastName);
        setId_user(id_user);
    }

    public CustomerEntity(String firstName, String lastName, long idAddressEntity) {
        AddressRepository addressRepository = null;
        String city = addressRepository.findById(idAddressEntity).toString();
    }

    @Override
    public String toString() {
        return "CustomerEntity{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
