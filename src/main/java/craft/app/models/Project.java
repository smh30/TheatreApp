package craft.app.models;

//import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
public class Project {
    
    @Id
    private UUID listingId;
    
    private String title;
    private String description;
    private UUID creator;
    private String type;
    private Date dateListed;
    //todo an "urgency" variable. enum?
    //todo add a photo
    
    
    
    public Project() {
        this.listingId = UUID.randomUUID();
        this.dateListed = new Date();
    }
    
    public Project(String title, String type, String description, String clientName,
                   String clientLocation,
                   String clientEmail) {
        this();
        this.title = title;
        this.type = type;
        this.description = description;
        User projectCreator = new User(clientName, clientEmail, clientLocation);
        this.creator = projectCreator.getUserId();
        
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
    
    public User getCreator() {
        //get the user with that id and return it;
        return null;
    }
    
    public String getType() {
        return type;
    }
}
