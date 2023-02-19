package pl.reportsController.reports;

import org.springframework.web.bind.annotation.*;
import pl.reportsController.addresses.AddressRepository;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportRepository reportRepository;
    private final AddressRepository addressRepository;

    public ReportController(ReportRepository reportRepository,
                            AddressRepository addressRepository) {
        this.reportRepository = reportRepository;
        this.addressRepository = addressRepository;
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

    @DeleteMapping("/delete={id}")
    public void deleteReportById(@PathVariable Long id){
        if(!reportRepository.existsById(id)){
            throw new RuntimeException();
        } else {
            reportRepository.deleteById(id);
        }
    }
}
