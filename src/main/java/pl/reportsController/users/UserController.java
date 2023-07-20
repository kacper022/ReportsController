package pl.reportsController.users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import pl.reportsController.passwords.PasswordHashing;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Iterable<UserEntity> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable Long id){
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/login")
    public String loginUser(HttpServletResponse response, @RequestParam String login, @RequestParam String password) {
        String hashedPassword = PasswordHashing.HashPassword(password);

        if(userRepository.findByLogin(login) == null ){
            return "Błędny login";
        }

        if(!userRepository.findPasswordByLogin(login).equals(hashedPassword)){
            return "Błędne hasło";
        }
        Long userId = userRepository.findIdByUsernameAndPassword(login, hashedPassword);
        Cookie cookie = new Cookie("userId", ""+userId);
        response.addCookie(cookie);

        return "OK";
    }


    @GetMapping("/register")
    public String loginUser(HttpServletResponse response, @RequestParam String firstName,
                             @RequestParam String lastName,
                          @RequestParam String login, @RequestParam String password, @RequestParam String email ){
        UserEntity newUser = new UserEntity(firstName, lastName, login, password, email);
        if(userRepository.checkLoginExists(login) != null){
            return "Login jest już zajęty";
        }

        if(userRepository.checkEmailExists(email) != null){
            return "Email jest już zajęty";
        }
        userRepository.save(newUser);

        Cookie cookie = new Cookie("idUser", ""+newUser.getId());
        response.addCookie(cookie);
        return "OK";
    }

    @DeleteMapping("delete={id}")
    public void deleteUserById(@PathVariable Long id){
        if(userRepository.existsById(id)){
            throw new RuntimeException();
        } else {
            userRepository.deleteById(id);
        }
    }
}
