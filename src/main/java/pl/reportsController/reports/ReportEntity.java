package pl.reportsController.reports;

import jakarta.persistence.*;
import lombok.*;
import pl.reportsController.customers.CustomerEntity;
import pl.reportsController.users.UserEntity;

import java.util.Date;
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
    private String name;
    private String description;
    private Long clientId;
    private Long usersRealisingReport;

    private ReportsStatus reportsStatus;
    private Date createDate;
    private Date endDate;
    private Date updateDate;

    @Column(nullable = true)
    private String reportPhoto;


    public ReportEntity(String name, String description, Long clientId, Date createDate, Date updateDate){
        this.name = name;
        this.description = description;
        this.clientId = clientId;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
    public ReportEntity(String name, String description, Long clientId, Long usersRealisingReport, Date createDate,
                        Date updateDate){
        this.name = name;
        this.description = description;
        this.clientId = clientId;
        this.usersRealisingReport = usersRealisingReport;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public ReportEntity(String name, String description, ReportsStatus reportsStatus, Long clientId,
                        Long usersRealisingReport,
                        Date createDate,
                        Date updateDate){
        setName(name);
        setDescription(description);
        setClientId(clientId);
        setReportsStatus(reportsStatus);
        setUsersRealisingReport(usersRealisingReport);
        setCreateDate(createDate);
        setUpdateDate(updateDate);
    }


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
