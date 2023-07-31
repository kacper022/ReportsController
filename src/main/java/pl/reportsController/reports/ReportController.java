package pl.reportsController.reports;

import jakarta.persistence.PostUpdate;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pl.reportsController.addresses.AddressRepository;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

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

    @GetMapping("/getAll")
    public Iterable<ReportEntity> getAllReports(){
        return reportRepository.findAll();
    }

    @GetMapping("/getById")
    public String getReportById(HttpServletResponse response, @RequestBody ReportEntity re){
        if(reportRepository.findById(re.getId()) == null){
            return "Brak adresu w bazie";
        }
        return reportRepository.findById(re.getId()).toString();
    }

    @GetMapping("/getByName")
    public Iterable<ReportEntity> getRepotrsByName(HttpServletResponse response, @RequestBody ReportEntity re){
        return reportRepository.findByNameContainingIgnoreCase(re.getName());
    }

    @PostMapping("/createNewReport")
    public void createNewReport(HttpServletResponse response, @RequestBody ReportEntity re) throws URISyntaxException {
        ReportEntity savedReportEntity = reportRepository.save(re);
        System.out.println("Created new report");
    }

    @DeleteMapping
    public void deleteReportById(HttpServletResponse response, @RequestBody ReportEntity re){
        if(!reportRepository.existsById(re.getId())){
            throw new RuntimeException();
        } else {
            System.out.println("Deleted report: "+reportRepository.findById(re.getId()));
            reportRepository.deleteById(re.getId());
        }
    }
    @Transactional
    @PatchMapping("/{reportId}/update-photo")
    public ResponseEntity<Void> updateReportPhoto(@PathVariable Long reportId, MultipartHttpServletRequest request) throws IOException {
        MultipartFile photo = request.getFile("photo");
        Optional<ReportEntity> reportEntityOptional = reportRepository.findById(reportId);

        if (!reportEntityOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Zwróć status 404 gdy raport nie istnieje
        } else {
            ReportEntity re = reportEntityOptional.get();
            System.out.println(photo.getName() + " " + photo.getContentType());
            reportRepository.updateReportPhoto(photo.getBytes(), re.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Zwróć status 204 gdy aktualizacja przebiegła pomyślnie
        }
    }
}
