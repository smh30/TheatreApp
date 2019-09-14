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
 * This component will only execute (and get instantiated) if the
 * property noteit.db.recreate is set to true in the
 * application.properties file
 */

@Component
@ConditionalOnProperty(name = "craft.db.recreate", havingValue = "true")
public class DbSeeder implements CommandLineRunner {
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public DbSeeder(ProjectRepository projectRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Remove all existing entities
        this.projectRepository.deleteAll();
        this.userRepository.deleteAll();
        
        
        

        User bob = new User("bob", passwordEncoder.encode("bob123"), "bob@mail.com");
        User sally = new User("sally", passwordEncoder.encode("sally123"), "sally@mail.com");
        List<User> users = Arrays.asList(bob, sally);
        
        users = this.userRepository.saveAll(users);
    
        // Save some default listings
//        this.projectRepository.save(new Project("Fairisle sweater", "knitting", "A beautiful " +
//                "sweater I saw on " +
//                "Instagram", users.get(0).getUsername()));
        this.projectRepository.save(new Project("Childrens' gloves", "knitting", "Yellow gloves " +
                "for my neice",
                new User("kim", passwordEncoder.encode("kim123"), "kim@mail.com")));
//        this.projectRepository.save(new Project("Summer dress", "sewing", "Vintage-style dress for everyday " +
//                "wear", users.get(0).getUsername()));


        System.out.println("Initialized database");
    }
}
