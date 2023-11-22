package pl.reportsController.reports;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.reportsController.addresses.AddressEntity;

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
    @Column(columnDefinition = "TEXT")
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Long clientId;
    private Long usersRealisingReport;
    private ReportStatus reportStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressEntity addressEntity;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String reportPhoto;

    //Elementy zwiazane z technikiem
    @Column(columnDefinition = "TEXT")
    private String technicReportPhoto;
    @Column(columnDefinition = "TEXT")
    private String technicDescription;


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

    public ReportEntity(String name, String description, ReportStatus reportStatus, Long clientId, Long usersRealisingReport,
                        Date createDate, Date updateDate) {
        setName(name);
        setDescription(description);
        setClientId(clientId);
        setReportStatus(reportStatus);
        setUsersRealisingReport(usersRealisingReport);
        setCreateDate(createDate);
        setUpdateDate(updateDate);
    }

    public ReportEntity(String name, String description, ReportStatus reportStatus) {
        this.name = name;
        this.description = description;
        this.reportStatus = reportStatus;
    }

    public ReportEntity(String name, String description, ReportStatus reportStatus, String reportPhoto) {
        this.name = name;
        this.description = description;
        this.reportStatus = reportStatus;
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
        return "ReportEntity{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + ", clientId=" + clientId + ", usersRealisingReport=" + usersRealisingReport + ", reportStatus=" + reportStatus + ", createDate=" + createDate + ", endDate=" + endDate + ", updateDate=" + updateDate + ", reportPhoto=" +
                reportPhoto + '}';
    }
}
