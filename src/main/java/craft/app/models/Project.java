package craft.app.models;

//import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Project {
    
    @Id
    @GeneratedValue
    private long projectID;
    
    private String title;
    private String description;
    //todo change this into a long???? maybe
    //private UUID creator;
    private String type;
    private Date dateListed;
    
    @Lob
    private byte[] projectImage;
    
    
    //https://stackoverflow.com/questions/2302802/object-references-an-unsaved-transient-instance
    // -save-the-transient-instance-be
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="creatorID")
    private User creator;
    //todo an "urgency" variable. enum?
    //todo add a photo
    
    
    
    public Project() {
        
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
        this.creator = projectCreator;
        
        
    }
    
    public long getProjectID() {
        return projectID;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public User getCreator() {
        //get the user with that id and return it;
        return this.creator;
    }
    
    public String getType() {
        return type;
    }
    
    public Date getDateListed() {
        return dateListed;
    }
}
