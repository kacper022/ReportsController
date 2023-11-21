package pl.reportsController.reportsHistory;

import org.springframework.data.repository.CrudRepository;
import pl.reportsController.reports.ReportEntity;

public interface ReportHistoryRepository  extends CrudRepository<ReportEntity, Long> {
}
