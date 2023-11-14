package pl.reportsController.addresses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.reportsController.customers.CustomerEntity;
import pl.reportsController.customers.CustomerRepository;
import pl.reportsController.reports.ReportEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "`address`")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String city;
    private String zipCode;
    private String street;
    private String streetNumber;
    private String apartmentNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
    private CustomerEntity customer;


    @OneToMany(mappedBy = "addressEntity", cascade = CascadeType.ALL)
    private List<ReportEntity> reportEntity;

    public AddressEntity(String street, String streetNumber){
        this.street = street;
        this.streetNumber = streetNumber;
    }

    public AddressEntity(String street, String streetNumber,String apartmentNumber, String city, String zipCode){
        this.street = street;
        this.streetNumber = streetNumber;
        this.apartmentNumber = apartmentNumber;
        this.city = city;
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "AddressEntity{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", apartmentNumber='" + apartmentNumber + '\'' +
                '}';
    }

    public String getFullAddressString(){
        StringBuilder sb = new StringBuilder();
        sb.append(street);
        sb.append(" "+streetNumber);
        if(apartmentNumber!= null){
            sb.append("/"+apartmentNumber);
        }
        sb.append(" "+city);
        sb.append(" "+zipCode);
        return sb.toString();
    }
}
