package pl.reportsController.reports;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.reportsController.addresses.AddressRepository;
import pl.reportsController.roles.ERole;
import pl.reportsController.roles.RoleEntity;
import pl.reportsController.users.UserEntity;
import pl.reportsController.users.UserRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportRepository reportRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public ReportController(ReportRepository reportRepository,
                            AddressRepository addressRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/getAll")
    public Iterable<ReportEntity> getAllReports() {
        Iterable<ReportEntity> allReports = reportRepository.findAll();
        return allReports;
    }

    @PostMapping("/getById")
    public ResponseEntity<String> getReportById(HttpServletResponse response,
                                                @RequestBody ReportEntity re) {

        if (reportRepository.findById(re.getId()) == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        JSONObject element = new JSONObject();
        ReportEntity report = reportRepository.findReportById(re.getId());
        element.put("id", report.getId());
        element.put("name", report.getName());
        element.put("description", report.getDescription());
        element.put("reportStatus", report.getReportStatus());
        element.put("createDate", report.getCreateDate());
        element.put("endDate", report.getEndDate());
        element.put("modificationDate", report.getUpdateDate());
        element.put("userRealisingReport", report.getUsersRealisingReport());
        element.put("reportImg" , report.getReportPhoto());

        return new ResponseEntity<>(element.toString(), HttpStatus.OK);
    }

    @GetMapping("/getByName")
    public Iterable<ReportEntity> getReportsByName(HttpServletResponse response,
                                                   @RequestBody ReportEntity re) {
        return reportRepository.findByNameContainingIgnoreCase(re.getName());
    }
    @Transactional
    @PostMapping("/createNewReport")
    public ResponseEntity<String> createNewReport(@RequestParam("name") String name,
                                                  @RequestParam("description") String description,
                                                  @RequestParam("clientId") Long clientId,
                                                  @RequestParam("reportPhoto") MultipartFile reportPhoto) throws URISyntaxException, IOException {

        ReportEntity report = new ReportEntity();
        report.setName(name);
        report.setDescription(description);
        report.setClientId(clientId);
        report.setCreateDate(new Date());
        report.setUpdateDate(new Date());
        report.setReportStatus(ReportStatus.NEW);

        if (!reportPhoto.isEmpty()) {
            if (reportPhoto != null) {
                String imageBase64 = Base64.getEncoder().encodeToString(reportPhoto.getBytes());
                report.setReportPhoto(imageBase64);
            }
        }

        reportRepository.save(report);
        System.out.println("Dodano usterkę: " + report.getId());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public void deleteReportById(HttpServletResponse response,
                                 @RequestBody ReportEntity re) {
        if (!reportRepository.existsById(re.getId())) {
            throw new RuntimeException();
        } else {
            System.out.println("Deleted report: " + reportRepository.findById(re.getId()));
            reportRepository.deleteById(re.getId());
        }
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<String> updateReportStatus(HttpServletResponse response,
                                                     @RequestBody ReportEntity report) throws IOException {
        boolean updateReport = false;
        if (report.getId() < 0) {
            response.sendError(406, "Blad podczas zapisu danych");
            System.out.println("Bledne id zgloszenia");
        }

        try {
            ReportStatus.valueOf(String.valueOf(report.getReportStatus()));
            updateReport = true;
        } catch (IllegalArgumentException ex) {
            System.out.println("Nie ma takiego statusu na liscie stanow usterki");
        }

        if (updateReport) {
            reportRepository.updateReportStatus(report.getId(), report.getReportStatus());
            response.setStatus(200);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/getAllUserReports")
    public ResponseEntity<String> getAllUserReports(HttpServletResponse response,
                                                    @RequestBody UserEntity user) {

        if (user.getIdUser() == null || !user.getIsLogged().equals("true")) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        JSONArray object = new JSONArray();
        JSONObject element = new JSONObject();
        Set<RoleEntity> userRoles = userRepository.findRolesByUserId(user.getIdUser());
        if (userRoles.stream().anyMatch(role -> ERole.ADMINISTRATOR.equals(role.getRoleName()))) {
            // TODO: rzeczy dla admina
            return new ResponseEntity<>(object.toString(), HttpStatus.OK);
        } else if (userRoles.stream().anyMatch(role -> ERole.CUSTOMER.equals(role.getRoleName()))) {
            Iterable<ReportEntity> reports = reportRepository.findAllByClientId(user.getIdUser());
            for (ReportEntity r : reports) {
                element.put("id", r.getId());
                element.put("name", r.getName());
                element.put("description", r.getDescription());
                element.put("reportStatus", r.getReportStatus());
                element.put("createDate", r.getCreateDate());
                element.put("endDate", r.getEndDate());
                element.put("modificationDate", r.getUpdateDate());
                object.put(element);
                element = new JSONObject();
            }
            return new ResponseEntity<>(object.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
