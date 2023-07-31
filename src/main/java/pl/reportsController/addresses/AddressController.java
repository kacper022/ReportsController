package pl.reportsController.addresses;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.reportsController.reports.ReportRepository;
import pl.reportsController.users.UserEntity;

@RestController
@RequestMapping("/addresses")
public class AddressController {
    private final AddressRepository addressRepository;
    private final ReportRepository reportRepository;

    public AddressController(AddressRepository addressRepository,
                             ReportRepository reportRepository) {
        this.addressRepository = addressRepository;
        this.reportRepository = reportRepository;
    }

    @GetMapping
    public Iterable<AddressEntity> getAllAddresses(){
        return addressRepository.findAll();
    }

    @GetMapping("/getAddress")
    public AddressEntity getAddressById(@RequestBody AddressEntity addressEntity){
        System.out.println(addressEntity.toString());
        return addressRepository.findById(addressEntity.getId()).orElseThrow(RuntimeException::new);
    }

    @PostMapping("/addAddressToDatabase")
    public ResponseEntity<String> addAddress(HttpServletResponse response, @RequestBody AddressEntity addressEntity)
    {
        AddressEntity newAddressEntity = new AddressEntity(addressEntity.getStreet(), addressEntity.getStreetNumber(), addressEntity.getApartmentNumber(),
                                                           addressEntity.getCity(), addressEntity.getZipCode());
        addressRepository.save(addressEntity);
        return new ResponseEntity<>("Address added to database", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public void deleteAddressById(HttpServletResponse response, @RequestBody AddressEntity addressEntity){
        if(!addressRepository.existsById(addressEntity.getId())){
            throw new RuntimeException();
        } else {
            addressRepository.deleteById(addressEntity.getId());
        }
    }

}


