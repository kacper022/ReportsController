package pl.reportsController.customers;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.reportsController.addresses.AddressEntity;
import pl.reportsController.reports.ReportEntity;

import java.util.HashSet;
import java.util.Set;

@Table(name = "'customer'")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(mappedBy = "customerHomeAddress")
    private Set<AddressEntity> customerAddresses = new HashSet<>();

    @ManyToMany(mappedBy = "problemsRepotredByCustomer")
    private Set<ReportEntity> customerReports = new HashSet<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String firstName;
    private String lastName;

}
