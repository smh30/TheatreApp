package craft.app.api;

import craft.app.db.UserRepository;
import craft.app.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@CrossOrigin
public class UserController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    //todo remove this in production (or lock to admin)
    @GetMapping
public List<User> users(){
return userRepository.findAll();
}

@PostMapping
    public User newUser(@RequestBody User newUser) {
        newUser.setPassword(encodePassword(newUser.getPassword()));
        this.userRepository.save(newUser);
        
        return newUser;
        //should this return userviewmodel??
}

private String encodePassword(String rawPassword){
        return passwordEncoder.encode(rawPassword);
}
    
    @GetMapping(value="/byUsername/{username}")
    public User userByUsername(@PathVariable String username){
        return userRepository.findByUsername(username);
    }
}
