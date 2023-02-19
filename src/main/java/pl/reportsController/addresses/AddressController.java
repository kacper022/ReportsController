package pl.reportsController.addresses;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.reportsController.reports.ReportRepository;

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

    @GetMapping("/{id}")
    public AddressEntity getAddressById(@PathVariable Long id){
        return addressRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAddress(
            @RequestParam("street") String street,
            @RequestParam("streetNumber") String streetNumber,
            @RequestParam("apartmentNumber") String apartmentNumber,
            @RequestParam("city") String city,
            @RequestParam("zipCode") String zipCode)
    {
        AddressEntity addressEntity = new AddressEntity(street, streetNumber, apartmentNumber, city, zipCode);
        addressRepository.save(addressEntity);
        return new ResponseEntity<>("Address added to database", HttpStatus.OK);
    }

    @DeleteMapping("/delete={id}")
    public void deleteAddressById(@PathVariable Long id){
        if(!addressRepository.existsById(id)){
            throw new RuntimeException();
        } else {
            addressRepository.deleteById(id);
        }
    }

}


