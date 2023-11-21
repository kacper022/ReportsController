package pl.reportsController.reportsHistory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.reportsController.reports.ReportStatus;

import java.util.Date;
import java.util.Objects;

@Table(name = "`reportHistory`")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private long reportChangeUserId;
    private String reportDescription;
    private String reportName;
    private Date reportChangeDate;
    private long reportId;
    private ReportStatus reportStatus;
    private String clientReportImage;
    private String technicReportImage;

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
