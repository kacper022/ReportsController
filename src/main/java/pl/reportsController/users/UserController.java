package pl.reportsController.users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.reportsController.emailer.EmailService;
import pl.reportsController.passwords.PasswordHashing;
import pl.reportsController.roles.ERole;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    private final String DOMAIN = "localhost";
    private final String PATH = "/";
    private final int COOKIE_LIFE_TIME =7 * 24 * 60 * 60;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserController(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @GetMapping
    public Iterable<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public UserEntity getUserById(
            @PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping("/login")
    public String loginUser(HttpServletResponse response,
                            @RequestBody UserEntity userEntity) {
        String hashedPassword = userEntity.getPassword();

        if (userRepository.findByLogin(userEntity.getLogin()) == null) {
            System.out.println("Error login (wrong username): " + userEntity.getLogin());
            return "Błędny login";
        }

        if (!userRepository.findPasswordByLogin(userEntity.getLogin()).equals(hashedPassword)) {
            System.out.println("Error login (wrong password): " + userEntity.getLogin());
            return "Błędne hasło";
        }
        Long userId = userRepository.findIdByUsernameAndPassword(userEntity.getLogin(), hashedPassword);
        UserEntity user = userRepository.getById(userId);
        System.out.println(userEntity.getLogin() + " has been logged in to service!");

        Cookie isLoggedCookie = new Cookie("isLogged", "true");
        isLoggedCookie.setMaxAge(COOKIE_LIFE_TIME);
        isLoggedCookie.setDomain(DOMAIN);
        isLoggedCookie.setPath(PATH);
        response.addCookie(isLoggedCookie);

        Cookie userIdCookie = new Cookie("userId", userId.toString());
        userIdCookie.setMaxAge(COOKIE_LIFE_TIME);
        userIdCookie.setDomain(DOMAIN);
        userIdCookie.setPath(PATH);
        response.addCookie(userIdCookie);

        Cookie oldUserRoleCookie = new Cookie("userRole", "");
        oldUserRoleCookie.setMaxAge(0);
        oldUserRoleCookie.setPath("/");
        response.addCookie(oldUserRoleCookie);

        Cookie userRoleCookie = new Cookie("userRole",
                                           user.getRoles().stream().max(Comparator.comparing(String::valueOf)).get().getRoleName().toString());
        userRoleCookie.setMaxAge(COOKIE_LIFE_TIME);
        userRoleCookie.setDomain(DOMAIN);
        userRoleCookie.setPath(PATH);
        response.addCookie(userRoleCookie);

        return "OK";
    }

    @PostMapping("/register")
    public String registerUser(HttpServletResponse response,
                               @RequestBody UserEntity ue) {

        UserEntity user = new UserEntity();
        user.setLogin(ue.getLogin());
        user.setPassword(ue.getPassword());
        user.setEmail(ue.getEmail());

        System.out.println(user.toString());
        if (userRepository.checkLoginExists(ue.getLogin()) != null) {
            return "Login jest już zajęty";
        }

        if (userRepository.checkEmailExists(ue.getEmail()) != null) {
            return "Email jest już zajęty";
        }

        userRepository.save(user);

        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("isLogged", "true");
        for (String name : cookies.keySet()) {
            Cookie cookie = new Cookie(name, cookies.get(name));
            response.addCookie(cookie);
        }
        response.setStatus(200);
        return "OK";
    }

    @DeleteMapping("delete={id}")
    public void deleteUserById(
            @RequestBody UserEntity ue,
            @PathVariable Long id) {
        if (ue.getRoles().equals(ERole.ADMINISTRATOR)) {
            return;
        }
        if (userRepository.existsById(id)) {
            throw new RuntimeException();
        } else {
            userRepository.deleteById(id);
        }
    }

    @PostMapping("/recoveryPassword")
    public ResponseEntity<String> passwordRecovery(HttpServletResponse response,
                                                   @RequestBody UserEntity ue) throws IOException {
        UserEntity user = new UserEntity();
        user.setEmail(ue.getEmail());
        Long idUser = userRepository.findIdByEmail(user.getEmail());

        System.out.println(user.toString());
        if (idUser != null && idUser > -1) {

            LocalDateTime lastResetDate = userRepository.getUserLastResetPasswordDate(idUser);
            DateTime currentTime = new DateTime();
            LocalDateTime currentLocalTime = currentTime.toLocalDateTime();
            boolean canUserResetPassword = true;
            if (lastResetDate != null) {
                canUserResetPassword = lastResetDate.plusMinutes(2).isBefore(currentLocalTime);
            }

            if (canUserResetPassword) {
                String randomPassword = PasswordHashing.generateRandomPasswordForUser();
                user.setPassword(randomPassword);

                userRepository.updateUserPassword(idUser, user.getEmail(), user.getPassword(), currentLocalTime);

                StringBuilder sb = new StringBuilder();
                String to = ue.getEmail();
                String subject = "Przypomnienie hasła";

                sb.append("Twoje tymczasowe hasło to: \n");
                sb.append(randomPassword);
                sb.append("\nPolecamy jak najszybszą zmianę");
                if (emailService.sendEmail(to, subject, sb.toString())) {
                    return ResponseEntity.ok("OK");
                }
            } else {
                return ResponseEntity.ok("Zmiana hasła zablokowana");
            }
        }
        return ResponseEntity.ok("Nie ma takiego uzytkownika");
    }
}
