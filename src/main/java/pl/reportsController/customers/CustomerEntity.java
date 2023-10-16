package pl.reportsController.customers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.reportsController.addresses.AddressEntity;
import pl.reportsController.addresses.AddressRepository;
import pl.reportsController.reports.ReportEntity;
import pl.reportsController.users.UserEntity;

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
    private String firstName="";
    private String lastName="";

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<AddressEntity> addresses;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "idUser")
    private UserEntity userEntity;


    public CustomerEntity(String firstName, String lastName, UserEntity userEntity) {
       this.firstName = firstName;
       this.lastName = lastName;
       this.userEntity=userEntity;
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
