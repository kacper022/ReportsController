package pl.reportsController.bootstrap;

import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.reportsController.addresses.AddressEntity;
import pl.reportsController.addresses.AddressRepository;
import pl.reportsController.customers.CustomerEntity;
import pl.reportsController.customers.CustomerRepository;
import pl.reportsController.passwords.PasswordHashing;
import pl.reportsController.reports.ReportEntity;
import pl.reportsController.reports.ReportRepository;
import pl.reportsController.reports.ReportStatus;
import pl.reportsController.users.UserEntity;
import pl.reportsController.users.UserRepository;
import pl.reportsController.users.UserRole;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public BootstrapData(ReportRepository reportRepository, UserRepository userRepository,
                         CustomerRepository customerRepository, AddressRepository addressRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        List<UserEntity> users = new ArrayList();
        //Creating test users
        val kacper = new UserEntity("admin", PasswordHashing.HashPassword("admin"), "kcpr543" +
                "@gmail.com", UserRole.ADMINISTRATOR,
                                    1);
        users.add(kacper);
        val user = new UserEntity("user", PasswordHashing.HashPassword("user"), "user@user" +
                ".pl", UserRole.CUSTOMER, 1);
        users.add(user);
        userRepository.saveAll(users);


        //Create address
        List<AddressEntity> listaAdresow = new ArrayList<>();
        val ulica = new AddressEntity("Morelowa", "12", "1", "Warszawa", "00-001");
        val ulica1 = new AddressEntity("Czekoladowa", "22", "", "Warszawa", "00-002");
        val ulica2 = new AddressEntity("Zielona", "2", "10", "Zielona Góra", "66-002");
        val ulica3 = new AddressEntity("Cerwona", "1", "1", "Toruń", " 87-100");
        listaAdresow.addAll(List.of(new AddressEntity[]{ulica, ulica1, ulica2, ulica3}));
        addressRepository.saveAll(listaAdresow);

        //Creating simple customer
        val janusz = new CustomerEntity();
        janusz.setFirstName("Janusz");
        janusz.setLastName("Nowak");
        customerRepository.save(janusz);

        val usterka = new ReportEntity("Nazwa usterki", "Opis usterki", ReportStatus.NEW, user.getId(), kacper.getId(),
                                       new Date(), new Date());
        reportRepository.save(usterka);

        System.out.println("\n\n==========================================");
        System.out.println("=============  STARTED  ==================");
        System.out.println("==========================================");

    }
}
