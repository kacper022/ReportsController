package pl.reportsController.reports;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.reportsController.addresses.AddressEntity;
import pl.reportsController.addresses.AddressRepository;
import pl.reportsController.roles.ERole;
import pl.reportsController.roles.RoleEntity;
import pl.reportsController.users.UserEntity;
import pl.reportsController.users.UserRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportRepository reportRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public ReportController(ReportRepository reportRepository, AddressRepository addressRepository, UserRepository userRepository,
                            EntityManager entityManager) {
        this.reportRepository = reportRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
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
        String clientDataString = "";
        if (userRepository.getById(report.getClientId()) != null) {
            if (userRepository.getById(report.getClientId()).getCustomerEntity() != null) {
                clientDataString =
                        userRepository.getById(report.getClientId()).getCustomerEntity().getFirstName() + " " + userRepository.getById(
                                report.getClientId()).getCustomerEntity().getLastName();
            }
        }
        element.put("id", report.getId());
        element.put("name", report.getName());
        element.put("description", report.getDescription());
        element.put("reportStatus", report.getReportStatus());
        element.put("createDate", report.getCreateDate());
        element.put("endDate", report.getEndDate());
        element.put("modificationDate", report.getUpdateDate());
        element.put("userRealisingReport", relisingUserName);
        element.put("reportImg", report.getReportPhoto());
        element.put("address", report.getAddressEntity() == null ? "" : report.getAddressEntity().getFullAddressString());
        element.put("clientWhoAddReport", clientDataString);
        element.put("technicReportPhoto", report.getTechnicReportPhoto());
        element.put("technicDescription", report.getTechnicDescription());

        JSONArray jarray = new JSONArray();
        JSONObject userRealisingJSON = new JSONObject();
        userRealisingJSON.put("value", report.getUsersRealisingReport());
        userRealisingJSON.put("label",
                              report.getUsersRealisingReport() != null ?
                                      userRepository.findById(report.getUsersRealisingReport()).get().getCustomerEntity()
                                              .getFirstName() + " " + userRepository.findById(report.getUsersRealisingReport()).get()
                                              .getCustomerEntity()
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
            @RequestParam(value = "reportPhoto", required = false) MultipartFile reportPhoto,
            @RequestParam(value = "reportAddress", required = false) long idAddress) throws URISyntaxException, IOException {

        ReportEntity report = new ReportEntity();
        report.setName(name);
        report.setDescription(description);
        report.setClientId(clientId);
        report.setCreateDate(new Date());
        report.setUpdateDate(new Date());
        report.setReportStatus(ReportStatus.NOWE);
        if (idAddress > 0 && addressRepository.existsById(idAddress)) {
            try {
                AddressEntity address = addressRepository.getAddresById(idAddress);
                report.setAddressEntity(address);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        if (reportPhoto != null) {
            String imageBase64 = Base64.getEncoder().encodeToString(reportPhoto.getBytes());
            report.setReportPhoto(imageBase64);
        }

        reportRepository.save(report);
        System.out.println("Dodano usterkę: " + report.getId());

        return new ResponseEntity<>(HttpStatus.OK);
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
            @RequestParam(name = "reportStatus", required = false) ReportStatus reportStatus,
            @RequestParam(name = "reportTechnicPhoto", required = false) MultipartFile reportTechnicPhoto,
            @RequestParam(name = "technicDescription", required = false) String technicDescription) throws URISyntaxException,
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

        if (reportTechnicPhoto != null) {
            String imageBase64 = Base64.getEncoder().encodeToString(reportTechnicPhoto.getBytes());
            report.setTechnicReportPhoto(imageBase64);
        }

        if (idUser != null) {
            if (idUserRelisingReport != null) {
                report.setUsersRealisingReport(idUserRelisingReport);
            }
            if (reportStatus != null) {
                report.setReportStatus(reportStatus);
            }
            if (report.getReportPhoto() != null) {
                reportRepository.updateReportByReportIdAndClientIdAndUserRealisingWithUserPhoto(report.getId(), report.getName(),
                                                                                                report.getDescription(),
                                                                                                report.getReportPhoto(),
                                                                                                report.getUpdateDate(),
                                                                                                report.getUsersRealisingReport(),
                                                                                                report.getReportStatus());
            } else if (report.getReportStatus() != null) {
                if (report.getTechnicDescription() != null) {
                    reportRepository.updateReportByReportIdAndClientIdAndUserRealisingWithTechnicPhoto(report.getId(), report.getName(),
                                                                                                       report.getDescription(),
                                                                                                       report.getTechnicReportPhoto(),
                                                                                                       report.getUpdateDate(),
                                                                                                       report.getUsersRealisingReport(),
                                                                                                       report.getReportStatus());
                } else {
                    reportRepository.updateReportByReportIdAndClientIdAndUserRealisingWithTechnicPhotoAndDescription(report.getId(),
                                                                                                                     report.getName(),
                                                                                                                     report.getDescription(),
                                                                                                                     report.getTechnicReportPhoto(),
                                                                                                                     report.getUpdateDate(),
                                                                                                                     report.getUsersRealisingReport(),
                                                                                                                     report.getReportStatus(),
                                                                                                                     technicDescription);
                }
            } else {
                reportRepository.updateReportByReportIdAndClientIdAndUserRealisingWithoutPhoto(report.getId(), report.getName(),
                                                                                               report.getDescription(),
                                                                                               report.getUpdateDate(),
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


    @DeleteMapping("/deleteReport")
    public ResponseEntity<String> deleteReportById(HttpServletResponse response,
                                                   @RequestParam(value = "idReport", required = true) long idReport) {
        if (idReport > 0) {
            if (!reportRepository.existsById(idReport)) {
                throw new RuntimeException();
            } else {
                System.out.println("Deleted report: " + reportRepository.findById(idReport));
                reportRepository.deleteById(idReport);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
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
                                                    @RequestParam("advReq") boolean advReq,
                                                    @RequestParam(value = "filterParameters", required = false) JSONObject filters) {

        if (idUser < 0 || !isLogged) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        UserEntity user = userRepository.getById(idUser);


        JSONArray object = new JSONArray();
        JSONObject element = new JSONObject();
        Set<RoleEntity> userRoles = userRepository.findRolesByUserId(user.getIdUser());
        if ((userRoles.stream().anyMatch(role -> ERole.ADMINISTRATOR.equals(role.getRoleName())) || userRoles.stream()
                .anyMatch(role -> ERole.OFFICE.equals(role.getRoleName()))) && advReq) {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM report ");
            StringBuilder whereCriteria = new StringBuilder();
            int whereCounter = 0;
            for (String s : filters.keySet()) {
                if (filters.get(s) != null && filters.get(s) != "" && !filters.get(s).toString().equalsIgnoreCase("null")) {
                    switch (s) {
                        case "dateFrom":
                            if (!filters.get(s).toString().equalsIgnoreCase("undefined")) {
                                if (whereCounter > 0) {
                                    whereCriteria.append(" and ");
                                }
                                whereCriteria.append(
                                        " report.create_date <= '" + new Timestamp((Long.valueOf(filters.get("dateFrom").toString()))) +
                                                "' ");
                                whereCounter++;
                            }
                            break;
                        case "dateTo":
                            if (!filters.get(s).toString().equalsIgnoreCase("undefined")) {
                                if (whereCounter > 0) {
                                    whereCriteria.append(" and ");
                                }
                                whereCriteria.append(
                                        " report.create_date >= '" + new Timestamp((Long.valueOf(filters.get("dateTo").toString()))) + "'" +
                                                " ");
                                whereCounter++;
                            }
                            break;
                        case "technicRealisingReport":
                            if (!filters.get(s).toString().equalsIgnoreCase("")) {
                                if (whereCounter > 0) {
                                    whereCriteria.append(" and ");
                                }
                                whereCriteria.append(" report.users_realising_report = " + filters.get(s) + " ");

                                whereCounter++;
                            }
                            break;
                        case "reportStatus":
                            if (!filters.get(s).toString().equalsIgnoreCase("")) {
                                if (whereCounter > 0) {
                                    whereCriteria.append(" and ");
                                }
                                whereCriteria.append(
                                        " report.report_status = " + ReportStatus.valueOf(filters.get(s).toString()).ordinal() +
                                                " ");
                                whereCounter++;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            if (whereCounter > 0) {
                sb.append(" where " + whereCriteria);
            }
            sb.append(" ORDER BY report.update_date DESC");
            Iterable<ReportEntity> reports = entityManager.createNativeQuery(sb.toString(), ReportEntity.class).getResultList();

            for (ReportEntity r : reports) {
                element.put("id", r.getId());
                element.put("name", r.getName());
                element.put("description", r.getDescription());
                element.put("reportStatus", r.getReportStatus());
                element.put("createDate", r.getCreateDate());
                element.put("endDate", r.getEndDate());
                element.put("modificationDate", r.getUpdateDate());
                element.put("is_important", user.getIdUser() == r.getUsersRealisingReport());

                object.put(element);
                element = new JSONObject();
            }
            return new ResponseEntity<>(object.toString(), HttpStatus.OK);
        } else if (userRoles.stream()
                .anyMatch(role -> ERole.CUSTOMER.equals(role.getRoleName()) || ERole.ADMINISTRATOR.equals(role.getRoleName()))) {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM report ");
            StringBuilder whereCriteria = new StringBuilder();
            int whereCounter = 0;
            for (String s : filters.keySet()) {
                if (filters.get(s) != null && filters.get(s) != "" && !filters.get(s).toString().equalsIgnoreCase("null")) {
                    switch (s) {
                        case "dateFrom":
                            if (!filters.get(s).toString().equalsIgnoreCase("undefined")) {
                                if (whereCounter > 0) {
                                    whereCriteria.append(" and ");
                                }
                                whereCriteria.append(
                                        " report.create_date <= '" + new Timestamp((Long.valueOf(filters.get("dateFrom").toString()))) +
                                                "' ");
                                whereCounter++;
                            }
                            break;
                        case "dateTo":
                            if (!filters.get(s).toString().equalsIgnoreCase("undefined")) {
                                if (whereCounter > 0) {
                                    whereCriteria.append(" and ");
                                }
                                whereCriteria.append(
                                        " report.create_date >= '" + new Timestamp((Long.valueOf(filters.get("dateTo").toString()))) + "'" +
                                                " ");
                                whereCounter++;
                            }
                            break;
                        case "technicRealisingReport":
                            if (!filters.get(s).toString().equalsIgnoreCase("")) {
                                if (whereCounter > 0) {
                                    whereCriteria.append(" and ");
                                }
                                whereCriteria.append(" report.users_realising_report = " + filters.get(s) + " ");

                                whereCounter++;
                            }
                            break;
                        case "reportStatus":
                            if (!filters.get(s).toString().equalsIgnoreCase("")) {
                                if (whereCounter > 0) {
                                    whereCriteria.append(" and ");
                                }
                                whereCriteria.append(
                                        " report.report_status = " + ReportStatus.valueOf(filters.get(s).toString()).ordinal() +
                                                " ");
                                whereCounter++;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            if (whereCounter > 0) {
                sb.append(" where report.client_id = " + user.getIdUser() + " AND " + whereCriteria);
            } else {
                sb.append(" WHERE report.client_id = " + user.getIdUser());
            }
            sb.append(" ORDER BY report.update_date DESC");
            Iterable<ReportEntity> reports = entityManager.createNativeQuery(sb.toString(), ReportEntity.class).getResultList();
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
    public ResponseEntity<String> getChartCounter(
            @RequestParam(name = "idUser", required = false) long idUser,
            @RequestParam(name = "userRole", required = true) String roleName) {
        JSONObject element = new JSONObject();

        if (ERole.ADMINISTRATOR.name().equalsIgnoreCase(roleName)) {
            element.put("done", reportRepository.getReportsByStatus(ReportStatus.UKONCZONE));
            element.put("inProgress",
                        reportRepository.getReportsByStatus(ReportStatus.W_TRAKCIE_REALIZACJI) + reportRepository.getReportsByStatus(
                                ReportStatus.W_TRAKCIE_ANALIZY));
            element.put("new", reportRepository.getReportsByStatus(ReportStatus.NOWE));
            element.put("canceled", reportRepository.getReportsByStatus(ReportStatus.ANULOWANE));

        } else if (ERole.OFFICE.name().equalsIgnoreCase(roleName)) {
            element.put("done", reportRepository.getTechnicReportsByStatus(ReportStatus.UKONCZONE, idUser));
            element.put("inProgress",
                        reportRepository.getTechnicReportsByStatus(ReportStatus.W_TRAKCIE_REALIZACJI,
                                                                   idUser) + reportRepository.getTechnicReportsByStatus(
                                ReportStatus.W_TRAKCIE_ANALIZY, idUser));
            element.put("new", reportRepository.getTechnicReportsByStatus(ReportStatus.NOWE, idUser));
            element.put("canceled", reportRepository.getTechnicReportsByStatus(ReportStatus.ANULOWANE, idUser));

        } else if (ERole.CUSTOMER.name().equalsIgnoreCase(roleName)) {
            element.put("done", reportRepository.getCustomerReportsByStatus(ReportStatus.UKONCZONE, idUser));
            element.put("inProgress",
                        reportRepository.getCustomerReportsByStatus(ReportStatus.W_TRAKCIE_REALIZACJI, idUser) + reportRepository.getReportsByStatus(
                                ReportStatus.W_TRAKCIE_ANALIZY));
            element.put("new", reportRepository.getCustomerReportsByStatus(ReportStatus.NOWE, idUser));
            element.put("canceled", reportRepository.getCustomerReportsByStatus(ReportStatus.ANULOWANE, idUser));
        }

        return new ResponseEntity<>(element.toString(), HttpStatus.OK);
    }
}
