package craft.app.models;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long userId;

    private String name;
    private String email;
    private String location;
    
    
    
    //https://stackoverflow.com/questions/4011472/mappedby-reference-an-unknown-target-entity-property
    //https://stackoverflow.com/questions/13027214/what-is-the-meaning-of-the-cascadetype-all-for-a-manytoone-jpa-association
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Project> projects;

    //just these for now: will add methods later

    public User(String name, String email, String location) {
        this();
        this.name = name;
        this.email = email;
        this.location = location;
    }

    public User() {
    
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
    
    public long getUserId(){
        return userId;
    }
    

}
