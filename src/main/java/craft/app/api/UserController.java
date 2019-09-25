package craft.app.api;

//import craft.app.db.CheckUsername;
import craft.app.db.UserRepository;
import craft.app.models.User;
import craft.app.models.UserViewModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controls the API endpoints related to Projects.
 * <p>
 * Has a general url format of /users
 */
@RestController
@RequestMapping(value = "/users")
@CrossOrigin
public class UserController {
    
    /**
     * An instance of the UserRepository to allow access to the database
     */
    private UserRepository userRepository;
    /**
     * An instance of the PasswordEncoder to allow passwords to be encoded before storage
     */
    private PasswordEncoder passwordEncoder;
    
    /**
     * Constructor, takes the user repository and password encoder as parameters so they will be
     * available to the class
     *
     * @param passwordEncoder
     * @param userRepository
     */
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    //todo remove this in production (or lock to admin)
//    @GetMapping
//public List<User> users(){
//return userRepository.findAll();
//}
    
    /**
     * Encodes a new user's password and then saves the user into the repository
     *
     * Available with a Post request to /users
     *
     * @return A UserViewModel representing the new user
     */
    @PostMapping
    public UserViewModel newUser(@RequestBody User newUser) {
        
        newUser.setPassword(encodePassword(newUser.getPassword()));
        this.userRepository.save(newUser);
        
        return new UserViewModel(newUser);
    }
    
    /**
     * Encodes the provided password using the passwordEncoder
     * @param rawPassword The user's provided password
     * @return The encoded password
     */
    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    
    //todo is this method needed??
    /**
     * Checks if a given username exists in the database already - used when a new user registers
     * to make sure their username is unique
     * @param username The username to be checked
     * @return A CheckUsername object with the given username, or null
     */
//    @GetMapping(value = "/checkUsername/{username}")
//    public CheckUsername checkUsername(@PathVariable String username) {
//        return userRepository.findAllByUsername(username);
//    }
    
    
    /**
     * Gets the information for the user with a given username
     * @param username The username of the User to be searched
     * @return A UserViewModel with the details of the selected user
     */
    @GetMapping(value = "/byUsername/{username}")
    public UserViewModel userByUsername(@PathVariable String username) {
        User found = userRepository.findByUsername(username);
        if (found != null) {
            return new UserViewModel(found);
        }
        return new UserViewModel(new User ("", "", ""));
    }
}
