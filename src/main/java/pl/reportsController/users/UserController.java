package pl.reportsController.users;

import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("delete={id}")
    public void deleteUserById(@PathVariable Long id){
        if(userRepository.existsById(id)){
            throw new RuntimeException();
        } else {
            userRepository.deleteById(id);
        }
    }
}
