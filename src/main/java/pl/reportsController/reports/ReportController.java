package pl.reportsController.reports;

import org.springframework.web.bind.annotation.*;
import java.net.URISyntaxException;

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

    @GetMapping("/name={name}")
    public Iterable<ReportEntity> getRepotrsByName(@PathVariable String name){
        return reportRepository.findByNameAllIgnoreCase(name);
    }

    @PostMapping
    public void createNewReport(@RequestBody ReportEntity reportEntity) throws URISyntaxException {
        ReportEntity savedReportEntity = reportRepository.save(reportEntity);
        System.out.println("Created new report");
    }

    @DeleteMapping("/{id}")
    public void deleteReportById(@PathVariable Long id){
        reportRepository.deleteById(id);
    }
}
