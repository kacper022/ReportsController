package pl.reportsController.DataSelectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.reportsController.addresses.AddressEntity;
import pl.reportsController.addresses.AddressRepository;
import pl.reportsController.customers.CustomerRepository;
import pl.reportsController.reports.ReportRepository;
import pl.reportsController.reports.ReportStatus;
import pl.reportsController.roles.ERole;
import pl.reportsController.users.UserEntity;
import pl.reportsController.users.UserRepository;

@RestController
@RequestMapping("/selectors")
public class ControlsData {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public ControlsData(ReportRepository reportRepository, UserRepository userRepository, CustomerRepository customerRepository,
                        AddressRepository addressRepository, AddressRepository addressRepository1) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository1;
    }

    @PostMapping("/getUserRelisingReports")
    public ResponseEntity<String> getUserRelisingReports() {
        JSONArray arrayToReturn = new JSONArray();
        JSONObject element = new JSONObject();

        for (UserEntity u : userRepository.getAllUsers()) {
            if (u.getRoles().stream()
                    .anyMatch(role -> ERole.ADMINISTRATOR.equals(role.getRoleName()) || ERole.OFFICE.equals(role.getRoleName()))) {
                element.put("value", u.getIdUser());
                element.put("label", u.getCustomerEntity().getFirstName() + " " + u.getCustomerEntity().getLastName());
                arrayToReturn.put(element);
                element = new JSONObject();
            }
        }
        return new ResponseEntity<>(arrayToReturn.toString(), HttpStatus.OK);
    }

    @PostMapping("/getAllReportStatuses")
    public ResponseEntity<String> getAllReportStatuses() {
        JSONArray arrayToReturn = new JSONArray();
        JSONObject element = new JSONObject();
        ReportStatus[] rs = ReportStatus.values();
        int i = 0;
        for (ReportStatus r : rs) {
            element.put("label", r.name());
            element.put("value", r.name());
            arrayToReturn.put(element);
            element = new JSONObject();
        }
        return new ResponseEntity<>(arrayToReturn.toString(), HttpStatus.OK);
    }

    @PostMapping("/getAllAddresses")
    public ResponseEntity<String> getAllAddresses() {
        JSONArray arrayToReturn = new JSONArray();
        JSONObject element = new JSONObject();
        boolean load = true;
        int loopCounter = 5;

        while (load && loopCounter > 0) {
            try {
                for (AddressEntity address : addressRepository.getAllAddressesElements()) {
                    element.put("label", address.getFullAddressString());
                    element.put("value", address.getId());
                    arrayToReturn.put(element);
                    element = new JSONObject();
                    load = false;
                }
            } catch (JpaSystemException e) {
                // Dodane ponieważ czasami zbyt szybko ładują się dane i nie zostają poprawnie zwrócone do frontendu
                loopCounter--;
            }
        }

        return new ResponseEntity<>(arrayToReturn.toString(), HttpStatus.OK);
    }

    @PostMapping("/getAdminReportChart")
    public ResponseEntity<String> getChartForAdmin() {
        JSONArray arrayToReturn = new JSONArray();
        JSONObject object = new JSONObject();

        JSONArray namesArray = new JSONArray();
        JSONArray finishedArray = new JSONArray();
        JSONArray realisingArray = new JSONArray();

        for (UserEntity u : userRepository.findAll()) {
            if (u.getRoles().stream()
                    .anyMatch(role -> ERole.OFFICE.equals(role.getRoleName()))) {
                namesArray.put(u.getCustomerEntity().getFirstName() + u.getCustomerEntity().getLastName());
                finishedArray.put(reportRepository.getAllTechnicReportByStatus(ReportStatus.DONE, u.getIdUser()));
                realisingArray.put(
                        reportRepository.getAllTechnicReportByStatus(ReportStatus.IN_ANALYZE,
                                                                     u.getIdUser()) + reportRepository.getAllTechnicReportByStatus(
                                ReportStatus.IN_PROGRESS, u.getIdUser()));
            }
        }
        object.put("names", namesArray);
        object.put("finished", finishedArray);
        object.put("realising", realisingArray);


        return new ResponseEntity<>(object.toString(), HttpStatus.OK);

    }
}
