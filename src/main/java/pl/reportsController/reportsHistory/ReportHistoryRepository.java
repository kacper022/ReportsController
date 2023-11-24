package pl.reportsController.reportsHistory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReportHistoryRepository  extends CrudRepository<ReportHistoryEntity, Long> {
    @Query("SELECT h FROM ReportHistoryEntity h where h.idReport = ?1 ORDER BY h.updateDate DESC")
    public Iterable<ReportHistoryEntity> findAllOrderByUpdateDateDesc(long idReport);
}
