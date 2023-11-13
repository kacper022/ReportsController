package pl.reportsController.reports;

import jakarta.persistence.OrderBy;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface ReportRepository extends CrudRepository<ReportEntity, Long> {

    Iterable<ReportEntity> findByNameContainingIgnoreCase(String name);
    @Query("SELECT r FROM ReportEntity r ORDER BY r.updateDate DESC")
    Iterable<ReportEntity> findAllOrderByUpdateDateDesc();
    @Modifying
    @Query("UPDATE ReportEntity SET reportPhoto = :photo WHERE id= :reportId")
    void updateReportPhoto(@Param("photo")byte[] photo, @Param("reportId")Long id);

    @Query("UPDATE ReportEntity re SET re.reportStatus = ?2 WHERE re.id = ?1")
    void updateReportStatus(Long idReport, ReportStatus status);

    @OrderBy
    Iterable<ReportEntity> findAllByClientIdOrderByUpdateDateDesc(long client_id);

    @Query("SELECT re FROM ReportEntity re WHERE re.id=?1")
    ReportEntity findReportById(long reportId);
    @Modifying
    @Transactional
    @Query("update ReportEntity re set re.name = ?3, re.description = ?4, re.reportPhoto = ?5, re.updateDate=?6 where re.id = ?1 and re" +
            ".clientId = ?2")
    void updateReportByReportIdAndClientId(long reportId, long idCustomer, String name, String desc, String img, Date dt);

    @Modifying
    @Transactional
    @Query("update ReportEntity re set re.name = ?2, re.description = ?3, re.reportPhoto = ?4, re.updateDate=?5," +
            " re.usersRealisingReport = ?6, re.reportStatus=?7 where re.id = ?1")
    void updateReportByReportIdAndClientIdAndUserRealising(long reportId, String name, String desc, String img, Date dt,
                                                           long idUserRealisingReport, ReportStatus reportStatus);

    @Modifying
    @Transactional
    @Query("update ReportEntity re set re.name = ?2, re.description = ?3,  re.updateDate=?4," +
            " re.usersRealisingReport = ?5, re.reportStatus=?6 where re.id = ?1")
    void updateReportByReportIdAndClientIdAndUserRealisingWithoutPhoto(long reportId, String name, String desc, Date dt,
                                                                       long idUserRealisingReport, ReportStatus reportStatus);
}
