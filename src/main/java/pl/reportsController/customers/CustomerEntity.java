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

    @JsonIgnore
    @ManyToMany(mappedBy = "customerHomeAddress")
    private Set<AddressEntity> customerAddresses = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "problemsRepotredByCustomer")
    private Set<ReportEntity> customerReports = new HashSet<>();

    private String firstName;
    private String lastName;

    public CustomerEntity(String firstName, String lastName, Set<AddressEntity> customerAddresses) {
        this.customerAddresses = customerAddresses;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public CustomerEntity(String firstName, String lastName, long idAddressEntity) {
        AddressRepository addressRepository = null;
        String city = addressRepository.findById(idAddressEntity).toString();

    }

    @Override
    public String toString() {
        return "CustomerEntity{" +
                "id=" + id +
                ", customerAddresses=" + customerAddresses +
                ", customerReports=" + customerReports +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
