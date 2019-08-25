package craft.app.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;
    
    private String email;
    //private String location;
    
    //@Column(nullable = false)
    private String password;
    
    
    
    //https://stackoverflow.com/questions/4011472/mappedby-reference-an-unknown-target-entity-property
    //https://stackoverflow.com/questions/13027214/what-is-the-meaning-of-the-cascadetype-all-for-a-manytoone-jpa-association
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Project> projects;

    //just these for now: will add methods later

    public User(String username, String password, String email) {
        this();
        this.username = username;
        this.password = password;
        this.email = email;
        
    }

    protected User() {
    }
    
    
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

//    public String getLocation() {
//        return location;
//    }
    
    public Long getUserId(){
        return userId;
    }
    
    public String getPassword() {
        return password;
    }
}
