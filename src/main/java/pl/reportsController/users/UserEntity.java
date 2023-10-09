package pl.reportsController.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;
import pl.reportsController.passwords.PasswordHashing;
import pl.reportsController.reports.ReportEntity;
import pl.reportsController.roles.RoleEntity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`user`")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    private String password;
    private String login;
    private String email;
    private boolean isUserActive;
    private Long customer_id;
    private LocalDateTime createDate;
    private LocalDateTime lastPasswordReset;

    @Lob
    @Column(name = "userAvatar", length = 1000)
    private byte[] userAvatar;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "usersRealisingReport", fetch = FetchType.LAZY)
    private Set<ReportEntity> reportEntities = new HashSet<>();

    public void addRole(RoleEntity role) {
        this.roles.add(role);
    }

    public void removeRole(RoleEntity role) {
        this.roles.remove(role);
    }

    public void setPassword(String password) {
        this.password = PasswordHashing.HashPassword(password);
    }

    public String getHashedPassword(String password) {
        return PasswordHashing.HashPassword(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(idUser, that.idUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser);
    }
}
