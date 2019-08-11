package craft.app.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class User {

    @Id
    private UUID userId;

    private String name;
    private String email;
    private String location;

    //just these for now: will add methods later

    public User(String name, String email, String location) {
        this();
        this.name = name;
        this.email = email;
        this.location = location;
    }

    public User() {
        this.userId = UUID.randomUUID();
    }
    
    
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }
    
    public UUID getUserId(){
        return userId;
    }
    

}
