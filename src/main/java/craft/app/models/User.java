package craft.app.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue
    @Column(nullable = false, length = 10)
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
    private Set<Project> projects = new HashSet<Project>();

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
    
    public void setPassword(String password) {
        this.password = password;
    }
}
