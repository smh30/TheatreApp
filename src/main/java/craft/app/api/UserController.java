package craft.app.api;

import craft.app.db.UserRepository;
import craft.app.models.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@CrossOrigin
public class UserController {
    private UserRepository userRepository;
    
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    //todo remove this in production (or lock to admin)
    @GetMapping
public List<User> users(){
return userRepository.findAll();
}


}
