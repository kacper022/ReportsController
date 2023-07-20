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
import pl.reportsController.users.UserEntity;
import pl.reportsController.users.UserRepository;
import pl.reportsController.users.UserRole;

import java.text.DateFormat;
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

        //Creating test users
        val kacper = new UserEntity("Kacper", "Kuczminski", "admin", PasswordHashing.HashPassword("admin"), "kcpr543" +
                "@gmail.com", UserRole.ADMINISTRATOR,
                                    1);
        val testowy = new UserEntity("User", "Userowski", "user", PasswordHashing.HashPassword("user"), "user@user" +
                ".pl", UserRole.CUSTOMER, 1);

        //Create address
        List<AddressEntity> listaAdresow = new ArrayList<>();
        val ulica = new AddressEntity("Morelowa", "12", "1", "Warszawa", "00-001");
        val ulica1 = new AddressEntity("Czekoladowa", "22", "", "Warszawa", "00-002");
        val ulica2 = new AddressEntity("Zielona", "2", "10", "Zielona Góra", "66-002");
        val ulica3 = new AddressEntity("Cerwona", "1", "1", "Toruń", " 87-100");
        listaAdresow.add(ulica);
        listaAdresow.add(ulica1);
        listaAdresow.add(ulica2);
        listaAdresow.add(ulica3);

        //Creating simple customer
        val janusz = new CustomerEntity();
        janusz.setFirstName("Janusz");
        janusz.setLastName("Nowak");
        janusz.getCustomerAddresses().add(ulica);
        //ulica.getCustomerHomeAddress().add(janusz);

        //Creating test reports
        val strojenietv = new ReportEntity();
        strojenietv.setName("Ustawienie TV");
        strojenietv.setDescription("Klient prosi o ustawienie programow TV");
        strojenietv.getProblemsRepotredByCustomer().add(janusz);
        Date date = new Date();
        String df = DateFormat.getDateTimeInstance().format(date);
        strojenietv.setCreateDate(date);
        strojenietv.setUpdateDate(date);

        //Add report to user
        kacper.getReportEntities().add(strojenietv);
        testowy.getReportEntities().add(strojenietv);

        //Add report to customer
        janusz.getCustomerReports().add(strojenietv);

        //Add user to report
        strojenietv.getUsersRealisingReport().add(kacper);
        strojenietv.getUsersRealisingReport().add(testowy);
        strojenietv.getProblemsRepotredByCustomer().add(janusz);

        //Save all repositories
        userRepository.save(kacper);
        userRepository.save(testowy);

        for (var adres : listaAdresow) {
            addressRepository.save(adres);
        }

        customerRepository.save(janusz);
        reportRepository.save(strojenietv);

        System.out.println("\n\n==========================================");
        System.out.println("=============  STARTED  ==================");
        System.out.println("==========================================");

    }
}
