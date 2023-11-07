package DataSelectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.reportsController.customers.CustomerRepository;
import pl.reportsController.reports.ReportController;
import pl.reportsController.reports.ReportRepository;
import pl.reportsController.users.UserRepository;

@RestController
@RequestMapping("/selectors")
public class ControlsData {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final  CustomerRepository customerRepository;

    public ControlsData(ReportRepository reportRepository, UserRepository userRepository, CustomerRepository customerRepository,
                        ){

        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }
    @PostMapping("/getUserRelisingReports")
    public ResponseEntity<JSONArray> getUserRelisingReports(){
        JSONArray arrayToReturn = new JSONArray();
        JSONObject element = new JSONObject();


        return new ResponseEntity<>(arrayToReturn, HttpStatus.OK);
    }
}
