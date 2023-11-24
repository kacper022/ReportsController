package pl.reportsController.reportsHistory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.reportsController.addresses.AddressEntity;
import pl.reportsController.reports.ReportEntity;
import pl.reportsController.reports.ReportStatus;

import java.util.Date;
import java.util.Objects;

@Table(name = "`reportHistory`")
@Entity
@Data
@AllArgsConstructor
public class ReportHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long editorId;
    private long idReport;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

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

    public ReportHistoryEntity(long editorId, Date updateDate, ReportEntity reportEntity){
        this.editorId = editorId;
        this.updateDate = updateDate;

        //Ustawienie history zmian w elemencie
        this.idReport = reportEntity.getId();
        this.name = reportEntity.getName();
        this.description = reportEntity.getDescription();
        this.clientId = reportEntity.getClientId();
        this.usersRealisingReport = reportEntity.getUsersRealisingReport();
        this.createDate = reportEntity.getCreateDate();
        this.reportStatus = reportEntity.getReportStatus();
        this.reportPhoto = reportEntity.getReportPhoto();
        this.technicReportPhoto = reportEntity.getTechnicReportPhoto();
        this.technicDescription = reportEntity.getTechnicDescription();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReportHistoryEntity that = (ReportHistoryEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
