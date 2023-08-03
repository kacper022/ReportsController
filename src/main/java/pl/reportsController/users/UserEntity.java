package pl.reportsController.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.reportsController.passwords.PasswordHashing;
import pl.reportsController.reports.ReportEntity;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Table(name = "`user`")
@Data
@Entity
@NoArgsConstructor
public class UserEntity {

    UserRole userRole;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password = "";
    private String login = "";
    private String email = "";
    private int isUserActive = 0;

    private Long customer_id = 0L;

    private Date createDate = null;
    @JsonIgnore
    @ManyToMany(mappedBy = "usersRealisingReport", fetch = FetchType.LAZY)
    private Set<ReportEntity> reportEntities = new HashSet<>();

    public UserEntity(String login, String password, String email, UserRole userRole,
                      int isUserActive) {
        setLogin(login);
        this.password = password;
        setEmail(email);
        setUserRole(userRole);
        setIsUserActive(isUserActive);
        setCreateDate(new Date());
    }

    public UserEntity(String login, String password, String email) {
        setLogin(login);
        this.password = password;
        setEmail(email);
        setUserRole(UserRole.CUSTOMER);
        setIsUserActive(1);
        setCreateDate(new Date());
    }

    public void setPassword(String password) {
        this.password = PasswordHashing.HashPassword(password);
    }

    public String getHashedPassword(String password) {
        return PasswordHashing.HashPassword(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserEntity userEntity = (UserEntity) o;

        return Objects.equals(id, userEntity.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
