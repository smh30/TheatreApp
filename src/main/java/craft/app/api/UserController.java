package craft.app.api;

import craft.app.db.UserRepository;
import craft.app.models.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@CrossOrigin
public class UserController {
    private UserRepository userRepository;
    
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
//    @PostMapping
//    public User UserValidation(@RequestParam String username, @RequestParam String password){
//        if (userRepository.findOne())
//        return validatedUser;
//    }
}
