package pl.reportsController.reportsHistory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.reportsController.users.UserRepository;

@RestController
@RequestMapping("/reportsHisotry")
public class ReportHistoryController {

    private final ReportHistoryRepository reportHistoryRepository;
    private final UserRepository userRepository;

    public ReportHistoryController(ReportHistoryRepository reportHistoryRepository, UserRepository userRepository) {
        this.reportHistoryRepository = reportHistoryRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/getById")
    public ResponseEntity<String> getReportById(
            @RequestParam(value = "reportId", required = false) long reportId) {
        JSONArray arrayToReturn = new JSONArray();
        JSONObject historyObject = new JSONObject();
        int counter = 0;

        for (ReportHistoryEntity history : reportHistoryRepository.findAllOrderByUpdateDateDesc(reportId)) {
            String editor = (history.getEditorId() > 0 && userRepository.getById(history.getEditorId()) != null) ?
                    userRepository.getById(history.getEditorId()).getLogin() : "";

            String userRealisingReport =
                    (history.getUsersRealisingReport() != null && userRepository.getById(history.getUsersRealisingReport()) != null) ?
                            userRepository.getById(history.getUsersRealisingReport()).getLogin()
                            : "";

            historyObject.put("id", ++counter);
            historyObject.put("changeDate", history.getUpdateDate());
            historyObject.put("editor", editor);
            historyObject.put("name", history.getName());
            historyObject.put("description", history.getDescription());
            historyObject.put("userPhoto", history.getReportPhoto());
            historyObject.put("technicRealisingReport", userRealisingReport);
            historyObject.put("technicDescription", history.getTechnicDescription());
            historyObject.put("technicPhoto", history.getTechnicReportPhoto());

            arrayToReturn.put(historyObject);
            historyObject = new JSONObject();
        }
        return new ResponseEntity<>(arrayToReturn.toString(), HttpStatus.OK);
    }
}

