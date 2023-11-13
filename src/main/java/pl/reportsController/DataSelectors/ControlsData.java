package pl.reportsController.DataSelectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.reportsController.customers.CustomerRepository;
import pl.reportsController.reports.ReportRepository;
import pl.reportsController.reports.ReportStatus;
import pl.reportsController.roles.ERole;
import pl.reportsController.users.UserEntity;
import pl.reportsController.users.UserRepository;

import javax.management.relation.Role;

@RestController
@RequestMapping("/selectors")
public class ControlsData {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final  CustomerRepository customerRepository;

    public ControlsData(ReportRepository reportRepository, UserRepository userRepository, CustomerRepository customerRepository){
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }
    @PostMapping("/getUserRelisingReports")
    public ResponseEntity<String> getUserRelisingReports(){
        JSONArray arrayToReturn = new JSONArray();
        JSONObject element = new JSONObject();

        for(UserEntity u : userRepository.findAll()){
            if(u.getRoles().stream().anyMatch(role -> ERole.ADMINISTRATOR.equals(role.getRoleName()))) {
                element.put("value", u.getIdUser());
                element.put("label",u.getCustomerEntity().getFirstName()+" "+ u.getCustomerEntity().getLastName());
                arrayToReturn.put(element);
                element = new JSONObject();
            }
        }
        return new ResponseEntity<>(arrayToReturn.toString(), HttpStatus.OK);
    }

    @PostMapping("/getAllReportStatuses")
    public ResponseEntity<String> getAllReportStatuses(){
        JSONArray arrayToReturn = new JSONArray();
        JSONObject element = new JSONObject();
        ReportStatus[] rs = ReportStatus.values();
        int i =0;
        for(ReportStatus r : rs){
            element.put("label", r.name());
            element.put("value", r.name());
            arrayToReturn.put(element);
            element = new JSONObject();
        }
        return new ResponseEntity<>(arrayToReturn.toString(), HttpStatus.OK);
    }
}
