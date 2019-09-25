package craft.app.models;

/**
 * This ViewModel is used as part of the login process, where only the username and password are
 * required rather than any other characteristics of the User
 *
 * Recommended as part of the tutorial at https://www.youtube.com/watch?v=oVpFJr-Z35A&list=PLVApX3evDwJ1d0lKKHssPQvzv2Ao3e__Q&index=29
 */
public class LoginViewModel {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
