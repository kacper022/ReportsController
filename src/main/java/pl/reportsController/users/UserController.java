package pl.reportsController.users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;



@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        System.out.println(userEntity.getLogin() + " has been logged in to service!");

        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("isLogged", "true");
        cookies.put("userId", userId.toString());
        Cookie cookie = null;
        for (String name : cookies.keySet()) {
            cookie = new Cookie(name, cookies.get(name));
            response.addCookie(cookie);
        }
        return "OK";
    }

    @PostMapping("/register")
    public String registerUser(HttpServletResponse response,
                               @RequestBody UserEntity ue) {

        UserEntity user = new UserEntity(ue.getLogin(), ue.getPassword(), ue.getEmail());

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
        cookies.put("userId", user.getId().toString());
        for (String name : cookies.keySet()) {
            response.addCookie(new Cookie(name, cookies.get(name)));
        }
        return "OK";
    }

    @DeleteMapping("delete={id}")
    public void deleteUserById(
            @RequestBody UserEntity ue,
            @PathVariable Long id) {
        if (ue.getUserRole() != UserRole.ADMINISTRATOR) {
            return;
        }
        if (userRepository.existsById(id)) {
            throw new RuntimeException();
        } else {
            userRepository.deleteById(id);
        }
    }
}
