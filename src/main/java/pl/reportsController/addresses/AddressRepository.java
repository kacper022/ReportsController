package pl.reportsController.addresses;

import jakarta.persistence.Lob;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {


    @Query("SELECT address FROM AddressEntity address WHERE address.id = ?1")
    AddressEntity getAddresById(long idAddress);
    @Lob
    @Query("SELECT a FROM AddressEntity a")
    Iterable<AddressEntity> getAllAddressesElements();
}
