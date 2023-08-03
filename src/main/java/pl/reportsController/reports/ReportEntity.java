package pl.reportsController.reports;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

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
    private byte[] reportPhoto;


    public ReportEntity(String name, String description, Long clientId, Date createDate, Date updateDate) {
        this.name = name;
        this.description = description;
        this.clientId = clientId;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public ReportEntity(String name, String description, Long clientId, Long usersRealisingReport, Date createDate, Date updateDate) {
        this.name = name;
        this.description = description;
        this.clientId = clientId;
        this.usersRealisingReport = usersRealisingReport;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public ReportEntity(String name, String description, ReportsStatus reportsStatus, Long clientId, Long usersRealisingReport,
                        Date createDate, Date updateDate) {
        setName(name);
        setDescription(description);
        setClientId(clientId);
        setReportsStatus(reportsStatus);
        setUsersRealisingReport(usersRealisingReport);
        setCreateDate(createDate);
        setUpdateDate(updateDate);
    }

    public ReportEntity(String name, String description, ReportsStatus reportsStatus) {
        this.name = name;
        this.description = description;
        this.reportsStatus = reportsStatus;
    }

    public ReportEntity(String name, String description, ReportsStatus reportsStatus, byte[] reportPhoto) {
        this.name = name;
        this.description = description;
        this.reportsStatus = reportsStatus;
        this.reportPhoto = reportPhoto;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReportEntity reportEntity = (ReportEntity) o;

        return Objects.equals(id, reportEntity.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ReportEntity{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + ", clientId=" + clientId + ", usersRealisingReport=" + usersRealisingReport + ", reportsStatus=" + reportsStatus + ", createDate=" + createDate + ", endDate=" + endDate + ", updateDate=" + updateDate + ", reportPhoto=" + Arrays.toString(
                reportPhoto) + '}';
    }
}
