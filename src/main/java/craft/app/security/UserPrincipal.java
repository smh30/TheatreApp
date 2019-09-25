package craft.app.security;

import craft.app.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * A class which represents the aspects of the User relevant to Spring security, required by the
 * authentication and authorisation processes. In this case, just a wrapper on the User class
 * since roles/permissions/expired/locked accounts etc are not implemented.
 */
public class UserPrincipal implements UserDetails {
    
    /**
     * The User represented by this UserPrincipal
     */
    private User user;
    
    /**
     * Constructor, takes in a User and creates a corresponding UserPrincipal so that Spring
     * Security can interact with it
     * @param user
     */
    public UserPrincipal(User user){
        this.user =user;
    }
    
    @Override
    public String getPassword() {
        return this.user.getPassword();
    }
    
    @Override
    public String getUsername() {
        return this.user.getUsername();
    }
    
    //-------------------------------------------------------------------------------------------
    // Methods below this line are not relevant to this app but have to be implemented, so all
    // just return default values
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //not using Roles/Permissions in this app so...
        return null;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}
