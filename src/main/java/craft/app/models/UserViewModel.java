package craft.app.models;

/**
 * A ViewModel for the User class, which is returned by the API when http calls are made. This
 * avoids returning User objects which include the password.
 */
public class UserViewModel {
    
    private Long userId;
    private String username;
    private String email;
    
    /**
     * Create a new UserViewModel from an existing User
     * @param user
     */
    public UserViewModel(User user){
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
       
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
    

}
