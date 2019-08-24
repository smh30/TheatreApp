package craft.app.models;

public class UserViewModel {
    private Long userId;
    
    private String name;
    private String email;
    private String location;
    
    public UserViewModel(User user){
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.location = user.getLocation();
    }
    
    public Long getUserId() {
        return userId;
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
}
