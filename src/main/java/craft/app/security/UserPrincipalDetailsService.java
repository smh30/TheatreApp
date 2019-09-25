package craft.app.security;

import craft.app.db.UserRepository;
import craft.app.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Required by Spring Security
 */
@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    
    /**
     * An instance of user repository to allow db access
     */
    private UserRepository userRepository;
    
    /**
     * Constructor, injects dependency on user repository to allow db access
     * @param userRepository
     */
    public UserPrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Overrides Spring Security method to allow Security methods to find a user in the db by
     * username when performing login
     *
     * @param username The username to search the db for
     * @return A userPrincipal object representing the user
     * @throws UsernameNotFoundException If the username does not exist in the database, throws
     * this error which will be handled by Spring Security
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        return userPrincipal;
    }
}
