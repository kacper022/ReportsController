package pl.reportsController.reportsHistory;

import org.springframework.data.repository.CrudRepository;

public interface ReportHistoryRepository  extends CrudRepository<ReportHistoryEntity, Long> {
}
