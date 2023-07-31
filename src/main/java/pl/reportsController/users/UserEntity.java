package pl.reportsController.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.reportsController.addresses.AddressEntity;
import pl.reportsController.customers.CustomerEntity;
import pl.reportsController.passwords.PasswordHashing;
import pl.reportsController.reports.ReportEntity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Table(name = "`user`")
@Data
@Entity
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String password="";

    private String login="";

    private String email="";

    UserRole userRole;
    private int isUserActive;

    private Long customer_id;

    public void setPassword(String password) {
        this.password = PasswordHashing.HashPassword(password);
    }

    public UserEntity(String login, String password, String email, UserRole userRole,
                      int isUserActiv){
        this.login = login;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
        this.isUserActive = isUserActiv;
    }

    public UserEntity(String login, String password, String email){
        this.login = login;
        this.password = password;
        this.email = email;
        this.userRole = UserRole.CUSTOMER;
        this.isUserActive = 1;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "usersRealisingReport", fetch = FetchType.LAZY)
    private Set<ReportEntity> reportEntities = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity userEntity = (UserEntity) o;

        return Objects.equals(id, userEntity.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
