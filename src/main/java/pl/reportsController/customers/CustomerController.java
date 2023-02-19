package pl.reportsController.customers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.reportsController.addresses.AddressEntity;
import pl.reportsController.reports.ReportRepository;

import java.util.Set;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final ReportRepository reportRepository;

    public CustomerController(CustomerRepository customerRepository,
                              ReportRepository reportRepository) {
        this.customerRepository = customerRepository;
        this.reportRepository = reportRepository;
    }

    @GetMapping
    public Iterable<CustomerEntity> getAllCustomers(){
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public CustomerEntity getCustomerById(@PathVariable Long id){
        return customerRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCustomer(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("address") String idAddressEntity
    ){
        CustomerEntity customerEntity = new CustomerEntity(firstName, lastName, Long.parseLong(idAddressEntity));
        return new ResponseEntity<>("Customer added to database", HttpStatus.OK);
    }

    @DeleteMapping("/delete={id}")
    public void deleteCustomerById(@PathVariable long id){
        if(!customerRepository.existsById(id)){
            throw new RuntimeException();
        } else {
            customerRepository.deleteById(id);
        }
    }

}