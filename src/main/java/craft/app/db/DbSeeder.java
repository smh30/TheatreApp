package craft.app.db;

import craft.app.models.Project;
import craft.app.models.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Adds dummy users and project to the database on startup
 *
 * This component will only execute (and get instantiated) if the
 * property craft.db.recreate is set to true in the
 * application.properties file
 */
@Component
@ConditionalOnProperty(name = "craft.db.recreate", havingValue = "true")
public class DbSeeder implements CommandLineRunner {
    
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    
    /**
     * Constructor, takes user and project repositories and password encoder as parameter so
     * these will be available in the class
     * @param projectRepository
     * @param userRepository
     * @param passwordEncoder
     */
    public DbSeeder(ProjectRepository projectRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Runs when the app is started up, deletes anything that may exist in the repositories and
     * adds two dummy projects and users to the repositories
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        this.projectRepository.deleteAll();
        this.userRepository.deleteAll();
        
        this.projectRepository.save(new Project("Childrens' gloves", "knitting", "Yellow gloves " +
                "for my neice",
                new User("kim", passwordEncoder.encode("kim123"), "kim@mail.com")));
        this.projectRepository.save(new Project("Summer dress", "sewing", "Vintage-style dress for everyday " +
                "wear", new User("bob", passwordEncoder.encode("bob123"), "bob@mail.com")));

    }
}
