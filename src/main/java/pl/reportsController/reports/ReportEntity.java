package pl.reportsController.reports;

import jakarta.persistence.*;
import lombok.*;
import pl.reportsController.customers.CustomerEntity;
import pl.reportsController.users.UserEntity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Table(name = "`report`")
@Entity
@Data
@NoArgsConstructor
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String createDate;
    private String endDate;
    private String updateDate;
    private String name;
    private String description;

    @ManyToMany
    @JoinTable(name = "user_report", joinColumns = @JoinColumn(name = "report_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> usersRealisingReport = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "reportedProblems", joinColumns = @JoinColumn(name = "report_id"), inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private Set<CustomerEntity> problemsRepotredByCustomer = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportEntity reportEntity = (ReportEntity) o;

        return Objects.equals(id, reportEntity.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ReportEntity{" +
                "ID=" + id +
                ", createDate='" + createDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", usersRealisingReport=" + usersRealisingReport +
                '}';
    }
}
