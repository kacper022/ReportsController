package pl.reportsController.customers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.reportsController.reports.ReportRepository;
import pl.reportsController.roles.ERole;
import pl.reportsController.roles.RoleEntity;
import pl.reportsController.users.UserEntity;
import pl.reportsController.users.UserRepository;

import java.util.Set;

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
        CustomerEntity customerEntity = new CustomerEntity(firstName, lastName, idUser);
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

        if (userEntity.getIdUser() != null && userRoles != null) {
            for(RoleEntity role : userRoles){
                //TODO: tu dorobić sprawdzanie jaką role ma użytkownik w celu prawidłowego wyświetlania strony graficznej
            }
        }
        return null;
    }
}