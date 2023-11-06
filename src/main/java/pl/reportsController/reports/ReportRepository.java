package pl.reportsController.reports;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface ReportRepository extends CrudRepository<ReportEntity, Long> {

    Iterable<ReportEntity> findByNameContainingIgnoreCase(String name);
    @Modifying
    @Query("UPDATE ReportEntity SET reportPhoto = :photo WHERE id= :reportId")
    void updateReportPhoto(@Param("photo")byte[] photo, @Param("reportId")Long id);

    @Query("UPDATE ReportEntity re SET re.reportStatus = ?2 WHERE re.id = ?1")
    void updateReportStatus(Long idReport, ReportStatus status);

    Iterable<ReportEntity> findAllByClientId(long client_id);

    @Query("SELECT re FROM ReportEntity re WHERE re.id=?1")
    ReportEntity findReportById(long reportId);
    @Modifying
    @Transactional
    @Query("update ReportEntity re set re.name = ?3, re.description = ?4, re.reportPhoto = ?5, re.updateDate=?6 where re.id = ?1 and re" +
            ".clientId = ?2")
    void updateReportByReportIdAndClientId(long reportId, long idCustomer, String name, String desc, String img, Date dt);

}
