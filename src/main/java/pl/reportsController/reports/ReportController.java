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

    public ReportController(ReportRepository reportRepository, AddressRepository addressRepository, UserRepository userRepository) {
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
        String relisingUserName = report.getUsersRealisingReport() != null ?
                userRepository.findById(report.getUsersRealisingReport()).get().getCustomerEntity()
                        .getFirstName() + " " + userRepository.findById(report.getUsersRealisingReport()).get().getCustomerEntity()
                        .getLastName() : "";
        element.put("id", report.getId());
        element.put("name", report.getName());
        element.put("description", report.getDescription());
        element.put("reportStatus", report.getReportStatus());
        element.put("createDate", report.getCreateDate());
        element.put("endDate", report.getEndDate());
        element.put("modificationDate", report.getUpdateDate());
        element.put("userRealisingReport", relisingUserName);
        element.put("reportImg", report.getReportPhoto());
        JSONArray jarray = new JSONArray();
        JSONObject userRealisingJSON = new JSONObject();
        userRealisingJSON.put("value", report.getUsersRealisingReport());
        userRealisingJSON.put("label",
                              report.getUsersRealisingReport() != null ?
                                      userRepository.findById(report.getUsersRealisingReport()).get().getCustomerEntity()
                                              .getFirstName() + " " + userRepository.findById(report.getUsersRealisingReport()).get().getCustomerEntity()
                                              .getLastName() : "");
        jarray.put(userRealisingJSON);
        element.put("defaultUserRealising", jarray);

        return new ResponseEntity<>(element.toString(), HttpStatus.OK);
    }

    @GetMapping("/getByName")
    public Iterable<ReportEntity> getReportsByName(HttpServletResponse response,
                                                   @RequestBody ReportEntity re) {
        return reportRepository.findByNameContainingIgnoreCase(re.getName());
    }

    @Transactional
    @PostMapping("/createNewReport")
    public ResponseEntity<String> createNewReport(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("clientId") Long clientId,
            @RequestParam(value = "reportPhoto", required = false) MultipartFile reportPhoto) throws URISyntaxException, IOException {

        ReportEntity report = new ReportEntity();
        report.setName(name);
        report.setDescription(description);
        report.setClientId(clientId);
        report.setCreateDate(new Date());
        report.setUpdateDate(new Date());
        report.setReportStatus(ReportStatus.NEW);

        if (reportPhoto != null) {
            String imageBase64 = Base64.getEncoder().encodeToString(reportPhoto.getBytes());
            report.setReportPhoto(imageBase64);
        }

        reportRepository.save(report);
        System.out.println("Dodano usterkę: " + report.getId());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/updateReport")
    public ResponseEntity<String> updateReport(
            @RequestParam(name = "idUser", required = false) Long idUser,
            @RequestParam(name = "idReport", required = false) Long idReport,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "clientId", required = false) Long clientId,
            @RequestParam(name = "reportPhoto", required = false) MultipartFile reportPhoto,
            @RequestParam(name = "idUserRelisingReport", required = false) Long idUserRelisingReport,
            @RequestParam(name = "reportStatus", required = false) ReportStatus reportStatus) throws URISyntaxException,
            IOException {

        ReportEntity report = new ReportEntity();
        report.setId(idReport);
        report.setName(name);
        report.setDescription(description);
        report.setUpdateDate(new Date());

        if (clientId != null) {
            report.setClientId(clientId);
        }

        if (reportPhoto != null) {
            String imageBase64 = Base64.getEncoder().encodeToString(reportPhoto.getBytes());
            report.setReportPhoto(imageBase64);
        }

        if (idUser != null ) {
            if (idUserRelisingReport != null) {
                report.setUsersRealisingReport(idUserRelisingReport);
            }
            if (reportStatus != null) {
                report.setReportStatus(reportStatus);
            }
            if(report.getReportPhoto() != null) {
                reportRepository.updateReportByReportIdAndClientIdAndUserRealising(report.getId(), report.getName(),
                                                                                   report.getDescription(),
                                                                                   report.getReportPhoto(), report.getUpdateDate(),
                                                                                   report.getUsersRealisingReport(),
                                                                                   report.getReportStatus());
            } else {
                reportRepository.updateReportByReportIdAndClientIdAndUserRealisingWithoutPhoto(report.getId(), report.getName(),
                                                                                   report.getDescription(),report.getUpdateDate(),
                                                                                   report.getUsersRealisingReport(),
                                                                                   report.getReportStatus());
            }
        } else {
            reportRepository.updateReportByReportIdAndClientId(report.getId(), report.getClientId(), report.getName(),
                                                               report.getDescription(),
                                                               report.getReportPhoto(), report.getUpdateDate());
        }
        System.out.println("Zaaktualizowano usterkę: " + report.getId());

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
                                                    @RequestParam("idUser") Long idUser,
                                                    @RequestParam("isLogged") boolean isLogged,
                                                    @RequestParam("advReq") boolean advReq
                                                   ) {

        if (idUser < 0 || !isLogged) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        UserEntity user = userRepository.getById(idUser);

        JSONArray object = new JSONArray();
        JSONObject element = new JSONObject();
        Set<RoleEntity> userRoles = userRepository.findRolesByUserId(user.getIdUser());
        if (userRoles.stream().anyMatch(role -> ERole.ADMINISTRATOR.equals(role.getRoleName())) && advReq) {
            Iterable<ReportEntity> reports = reportRepository.findAllOrderByUpdateDateDesc();
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
        } else if (userRoles.stream()
                .anyMatch(role -> ERole.CUSTOMER.equals(role.getRoleName()) || ERole.ADMINISTRATOR.equals(role.getRoleName()))) {
            Iterable<ReportEntity> reports = reportRepository.findAllByClientIdOrderByUpdateDateDesc(user.getIdUser());
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

    @PostMapping("/getChartData")
    public ResponseEntity<String> getChartCounter(){
        JSONObject element = new JSONObject();

        element.put("done", reportRepository.getReportsByStatus(ReportStatus.DONE));
        element.put("inProgress",
                    reportRepository.getReportsByStatus(ReportStatus.IN_PROGRESS)+reportRepository.getReportsByStatus(ReportStatus.IN_ANALYZE));
        element.put("new", reportRepository.getReportsByStatus(ReportStatus.NEW));
        element.put("canceled", reportRepository.getReportsByStatus(ReportStatus.CANCELED));

        return new ResponseEntity<>(element.toString(), HttpStatus.OK);
    }
}
