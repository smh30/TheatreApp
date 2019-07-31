package craft.app.models;

//import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
public class Listing {
    
    @Id
    private UUID listingId;
    
    private String title;
    private String description;
   // private Client client;
    private String type;
    private Date dateListed;
    //todo an "urgency" variable. enum?
    //todo add a photo
    
    
    
    public Listing() {
        this.listingId = UUID.randomUUID();
        this.dateListed = new Date();
    }
    
    public Listing(String title, String type, String description, String clientName,
                   String clientLocation,
                   String clientEmail) {
        this();
        this.title = title;
        this.type = type;
        this.description = description;
        //this.client = new Client(clientName, clientEmail, clientLocation);
        
    }
    
    public UUID getListingId() {
        return listingId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
//    public Client getClient() {
//        return client;
//    }
    
    public String getType() {
        return type;
    }
}
