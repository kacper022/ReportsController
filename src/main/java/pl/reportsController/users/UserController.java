package pl.reportsController.users;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.manager.HTMLManagerServlet;
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

    @PostMapping("/login")
    public String loginUser(HttpServletResponse response, @RequestBody UserEntity userEntity) {
        String hashedPassword = userEntity.getPassword();

        if(userRepository.findByLogin(userEntity.getLogin()) == null ){
            System.out.println("Error login (wrong username): "+userEntity.getLogin());
            return "Błędny login";
        }

        if(!userRepository.findPasswordByLogin(userEntity.getLogin()).equals(hashedPassword)){
            System.out.println("Error login (wrong password): "+userEntity.getPassword());
            return "Błędne hasło";
        }
        Long userId = userRepository.findIdByUsernameAndPassword(userEntity.getLogin(), hashedPassword);
        System.out.println(userEntity.getLogin() + " has been logged in to service!");
        Cookie cookie = new Cookie("userId", ""+userId);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return "OK";
    }

    @PostMapping("/register")
    public String registerUser(HttpServletResponse response, @RequestBody UserEntity ue ){

        UserEntity user = new UserEntity(ue.getLogin(), ue.getPassword(), ue.getEmail());

        if(userRepository.checkLoginExists(ue.getLogin()) != null){
            return "Login jest już zajęty";
        }

        if(userRepository.checkEmailExists(ue.getEmail()) != null){
            return "Email jest już zajęty";
        }

         userRepository.save(ue);

        Cookie cookie = new Cookie("idUser", ""+ue.getId());
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
