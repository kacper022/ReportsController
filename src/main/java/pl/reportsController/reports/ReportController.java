package pl.reportsController.reports;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportRepository reportRepository;

    public ReportController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @GetMapping
    public Iterable<ReportEntity> getAllReports(){
        return reportRepository.findAll();
    }

    @GetMapping("/{id}")
    public ReportEntity getReportById(@PathVariable Long id){
        return reportRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
