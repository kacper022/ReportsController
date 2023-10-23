package pl.reportsController.customers;

import jakarta.servlet.http.HttpServletResponse;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.reportsController.reports.ReportRepository;
import pl.reportsController.roles.ERole;
import pl.reportsController.roles.RoleEntity;
import pl.reportsController.users.UserEntity;
import pl.reportsController.users.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public CustomerController(CustomerRepository customerRepository,
                              ReportRepository reportRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public Iterable<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public CustomerEntity getCustomerById(
            @PathVariable Long id) {
        return customerRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCustomer(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("address") Long idUser
                                             ) {
        UserEntity userEntity = null;
        if (idUser > 0) {
            userEntity = userRepository.getById(idUser);
        }
        CustomerEntity customerEntity = new CustomerEntity(firstName, lastName, userEntity);
        return new ResponseEntity<>("Customer added to database", HttpStatus.OK);
    }

    @DeleteMapping("/remove/")
    public void deleteCustomerById(
            @PathVariable long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException();
        } else {
            customerRepository.deleteById(id);
        }
    }

    @PostMapping("/CustomerInfo")
    public ResponseEntity<String> getCustomerInfo(HttpServletResponse response,
                                                  @RequestBody UserEntity userEntity) {
        Set<RoleEntity> userRoles = userRepository.findRolesByUserId(userEntity.getIdUser());
        JSONObject dataToReturn = new JSONObject();
        if (userEntity.getIdUser() != null && userRoles != null) {
            for (RoleEntity role : userRoles) {
                switch (role.getRoleName()) {
                    case ADMINISTRATOR -> {
                        return new ResponseEntity<>(dataToReturn.put("role", ERole.ADMINISTRATOR).toString(), HttpStatus.OK);
                    }
                    case CUSTOMER -> {
                        return new ResponseEntity<>(dataToReturn.put("role", ERole.CUSTOMER).toString(), HttpStatus.OK);
                    }
                }
            }
        }
        return new ResponseEntity<>(dataToReturn.put("role", ERole.ADMINISTRATOR).toString(), HttpStatus.OK);
    }
    @Transactional
    @PostMapping("/GetSimpleUserData")
    public ResponseEntity<String> getCustomerInfoSimple(HttpServletResponse response,
                                                        @RequestBody UserEntity u) {
        JSONObject objToReturn = new JSONObject();

        Optional<UserEntity> userOptional = userRepository.findUserWithCustomerById(u.getIdUser());

        if (!userOptional.isPresent()) {
            // sytuacja gdy użytkownik o danym ID nie istnieje
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        UserEntity user = userOptional.get();

        objToReturn.put("userId", user.getIdUser());
        objToReturn.put("password", user.getPassword());
        objToReturn.put("role", user.getRoles().stream().findFirst().get().getRoleName().name());

        if (user.getCustomerEntity() != null) {
            objToReturn.put("name", user.getCustomerEntity().getFirstName());
            objToReturn.put("surname", user.getCustomerEntity().getLastName());
            objToReturn.put("address", user.getCustomerEntity().getAddresses());
        }

        objToReturn.put("accountCreateDate", user.getCreateDate().toString());
        objToReturn.put("login", user.getLogin());
        objToReturn.put("image", "tu bedzie obraz");

        return new ResponseEntity<>(objToReturn.toString(), HttpStatus.OK);
    }

}