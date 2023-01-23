package pl.reportsController.bootstrap;

import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.reportsController.addresses.AddressEntity;
import pl.reportsController.reports.ReportEntity;
import pl.reportsController.reports.ReportRepository;
import pl.reportsController.users.UserEntity;
import pl.reportsController.users.UserRepository;

import java.util.Set;

@Component
public class BootstrapData implements CommandLineRunner {
    private ReportRepository reportRepository;
    private UserRepository userRepository;

    public BootstrapData(ReportRepository reportRepository, UserRepository userRepository){
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception{

        //Creating test users
        val kacper = new UserEntity();
        kacper.setFirstName("Kacper");
        kacper.setLastName("Kacper");

        val testowy = new UserEntity();
        testowy.setFirstName("Uzytkownik");
        testowy.setLastName("Testowy");

        //Creating test reports
        val strojenietv = new ReportEntity();
        strojenietv.setName("Ustawienie TV");
        strojenietv.setDescription("Klient prosi o ustawienie programow TV");


        //Add report to user
        kacper.getReportEntities().add(strojenietv);
        testowy.getReportEntities().add(strojenietv);

        //Add user to report
        strojenietv.getUsersRealisingReport().add(kacper);
        strojenietv.getUsersRealisingReport().add(testowy);

        //Save all repositories
        userRepository.save(kacper);
        userRepository.save(testowy);
        reportRepository.save(strojenietv);

        System.out.println("\n\n==========================================");
        System.out.println("=============  STARTED  ==================");
        System.out.println("==========================================");

    }
}
