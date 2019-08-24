package craft.app.models;

public class UserViewModel {
    private Long userId;
    
    private String username;
    private String email;
   // private String location;
    
    public UserViewModel(User user){
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
       // this.location = user.getLocation();
    }
    
    public Long getUserId() {
        return userId;
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
}
