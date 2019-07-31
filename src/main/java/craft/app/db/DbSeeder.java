package craft.app.db;

import craft.app.models.Listing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * This component will only execute (and get instantiated) if the
 * property noteit.db.recreate is set to true in the
 * application.properties file
 */

@Component
@ConditionalOnProperty(name = "craft.db.recreate", havingValue = "true")
public class DbSeeder implements CommandLineRunner {
    private ListingRepository listingRepository;
    
    public DbSeeder(ListingRepository listingRepository){
        this.listingRepository = listingRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Remove all existing entities
        this.listingRepository.deleteAll();
    
    
        // Save some default listings
        this.listingRepository.save(new Listing("Fairisle sweater", "knitting", "A beautiful sweater I saw on " +
                "Instagram", "Mona", "Wellington", "m@test.com"));
        this.listingRepository.save(new Listing("Childrens' gloves", "knitting", "Yellow gloves for my neice",
                "Barry", "Hamilton", "baz@test.com"));
        this.listingRepository.save(new Listing("Summer dress", "sewing", "Vintage-style dress for everyday " +
                "wear", "Kim", "Raglan", "kim@test.com"));
        
        
        
        System.out.println("Initialized database");
    }
}
