package pl.reportsController.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    private String firstName="";
    private String lastName="";
    private String password="";

    private String login="";

    private String email="";

    UserRole userRole;
    private int isUserActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public Set<ReportEntity> getReportEntities() {
        return reportEntities;
    }

    public void setReportEntities(Set<ReportEntity> reportEntities) {
        this.reportEntities = reportEntities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PasswordHashing.HashPassword(password);
    }

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

    public int getIsUserActive() {
        return isUserActive;
    }

    public void setIsUserActive(int isUserActive) {
        this.isUserActive = isUserActive;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserEntity(String firstName, String lastName, String login, String password, String email, UserRole userRole,
                      int isUserActiv){
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
        this.isUserActive = isUserActiv;
    }

    /*

     */
    public UserEntity(String firstName, String lastName, String login, String password, String email){
        this.firstName = firstName;
        this.lastName = lastName;
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
