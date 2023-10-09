package pl.reportsController.bootstrap;

import lombok.val;
import org.joda.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.reportsController.addresses.AddressEntity;
import pl.reportsController.addresses.AddressRepository;
import pl.reportsController.customers.CustomerEntity;
import pl.reportsController.customers.CustomerRepository;
import pl.reportsController.reports.ReportEntity;
import pl.reportsController.reports.ReportRepository;
import pl.reportsController.reports.ReportStatus;
import pl.reportsController.roles.ERole;
import pl.reportsController.roles.RoleEntity;
import pl.reportsController.roles.RoleRepository;
import pl.reportsController.users.UserEntity;
import pl.reportsController.users.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;

    public BootstrapData(ReportRepository reportRepository, UserRepository userRepository,
                         CustomerRepository customerRepository, AddressRepository addressRepository, RoleRepository roleRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        RoleEntity adminRole = new RoleEntity();
        adminRole.setRoleName(ERole.ADMINISTRATOR);
        roleRepository.save(adminRole);

        LocalDateTime ldt = new LocalDateTime();

        List<UserEntity> users = new ArrayList();
        //Creating test users
        val kacper = new UserEntity();
        kacper.setLogin("admin");
        kacper.setPassword("admin");
        kacper.setUserActive(true);
        kacper.setCreateDate(ldt);
        kacper.setEmail("kacper.kuczminski@email.pl");
        kacper.addRole(adminRole);
        users.add(kacper);

        val user = new UserEntity();
        RoleEntity customerRole = new RoleEntity();
        customerRole.setRoleName(ERole.CUSTOMER);
        roleRepository.save(customerRole);
        user.setLogin("user");
        user.setPassword("user");
        user.setEmail("user@user.pl");
        user.setUserActive(true);
        user.setCreateDate(ldt);
        user.addRole(customerRole);
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

        val usterka = new ReportEntity("Nazwa usterki", "Opis usterki", ReportStatus.NEW, user.getCustomer_id(), kacper.getCustomer_id(),
                                       new Date(), new Date());
        reportRepository.save(usterka);

        System.out.println("\n\n==========================================");
        System.out.println("=============  STARTED  ==================");
        System.out.println("==========================================");

    }
}
