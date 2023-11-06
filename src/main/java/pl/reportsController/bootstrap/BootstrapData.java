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
import java.util.Random;

@Component
public class BootstrapData implements CommandLineRunner {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public BootstrapData(ReportRepository reportRepository, UserRepository userRepository,
                         CustomerRepository customerRepository, AddressRepository addressRepository, RoleRepository roleRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public void run(String... args) throws Exception {


        LocalDateTime ldt = new LocalDateTime();

        List<UserEntity> users = new ArrayList();
        //Creating test users
        val kacper = new UserEntity();
        kacper.setLogin("admin");
        kacper.setPassword("admin");
        kacper.setUserActive(true);
        kacper.setCreateDate(ldt);
        kacper.setEmail("kacper.kuczminski@email.pl");
        RoleEntity adminRole = new RoleEntity();
        adminRole.setRoleName(ERole.ADMINISTRATOR);
        kacper.addRole(adminRole);
        users.add(kacper);

        val user = new UserEntity();
        user.setLogin("user");
        user.setPassword("user");
        user.setEmail("user@user.pl");
        user.setUserActive(true);
        user.setCreateDate(ldt);
        RoleEntity customerRole = new RoleEntity();
        customerRole.setRoleName(ERole.CUSTOMER);
        user.addRole(customerRole);
        users.add(user);
        userRepository.saveAll(users);


        //Create customer profile
        List<CustomerEntity> customers = new ArrayList();
        CustomerEntity customerProfileKacper = new CustomerEntity();
        customerProfileKacper.setLastName("Kuczmin");
        customerProfileKacper.setFirstName("Kacper");
        customers.add(customerProfileKacper);
        kacper.setCustomerEntity(customerProfileKacper);
        customerProfileKacper.setUserEntity(kacper);

        CustomerEntity customerProfileUser = new CustomerEntity();
        customerProfileUser.setFirstName("USER");
        customerProfileUser.setLastName("USEROWSKI");
        customers.add(customerProfileUser);
        user.setCustomerEntity(customerProfileUser);
        customerProfileUser.setUserEntity(user);

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
        customers.add(janusz);

//        customerProfileUser.setAddresses(listaAdresow);

        customerRepository.saveAll(customers);

        ReportStatus randomStatus = ReportStatus.values()[new Random().nextInt(ReportStatus.values().length)];

        List<ReportEntity> reportEntityList = new ArrayList<ReportEntity>();
        for (int i = 0; i < 10; i++) {
            val usterka = new ReportEntity("Nazwa usterki" + i, "Opis usterki" + i, randomStatus, user.getIdUser(),
                                           kacper.getIdUser(),
                                           new Date(), new Date());
            reportEntityList.add(usterka);
            randomStatus = ReportStatus.values()[new Random().nextInt(ReportStatus.values().length)];
        }


        reportRepository.saveAll(reportEntityList);

        System.out.println("\n\n==========================================");
        System.out.println("=============  STARTED  ==================");
        System.out.println("==========================================");

    }
}
