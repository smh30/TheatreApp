package craft.app.db;

import craft.app.models.Project;
import craft.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * The repository which is used to persist the Users which have a Long as ID into the database.
 * Most methods are inherited from JpaRepository (adding, deleting, findAll etc)
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find the User with the given username, returns the User object
     * @param username The username to search for
     * @return A User object matching the given username
     */
    User findByUsername(String username);
    
    
}
