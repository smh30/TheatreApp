package craft.app.db;

import craft.app.models.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID> {
//can add custom methods here if necessary
    List<Listing> findByType(String type);
    
    //List<Listing> findByLocation(String location);
}