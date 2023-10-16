package pl.reportsController.reports;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends CrudRepository<ReportEntity, Long> {

    Iterable<ReportEntity> findByNameContainingIgnoreCase(String name);
    @Modifying
    @Query("UPDATE ReportEntity SET reportPhoto = :photo WHERE id= :reportId")
    void updateReportPhoto(@Param("photo")byte[] photo, @Param("reportId")Long id);

    @Query("UPDATE ReportEntity re SET re.reportStatus = ?2 WHERE re.id = ?1")
    void updateReportStatus(Long idReport, ReportStatus status);
}
