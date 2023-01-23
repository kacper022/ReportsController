package pl.reportsController.addresses;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/addresses")
public class AddressController {
    private final AddressRepository addressRepository;

    public AddressController(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @GetMapping
    public Iterable<AddressEntity> getAllAddresses(){
        return addressRepository.findAll();
    }

    @GetMapping("/{id}")
    public AddressEntity getAddressById(@PathVariable Long id){
        return addressRepository.findById(id).orElseThrow(RuntimeException::new);
    }

}


