package craft.app.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import craft.app.models.LoginViewModel;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

/**
 * Handles getting the data from login requests into a format which Spring Security can use to
 * perform the authentication, and creates a JWT based on the outcome of the authentication
 * <p>
 * Based on tutorial found at:
 * https://www.youtube.com/watch?v=oVpFJr-Z35A&list=PLVApX3evDwJ1d0lKKHssPQvzv2Ao3e__Q&index=29
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    /**
     * A built in class which is required for the function of the
     * UsernamePasswordAuthenticationFilter
     */
    private AuthenticationManager authenticationManager;
    
    /**
     * Constructor - inject the required AuthenticationManager dependency here
     *
     * @param authenticationManager
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    
    /**
     * Overrides the Spring Security attemptAuthentication method to tell it how to handle our
     * particular credentials
     * <p>
     * Fetches the credentials from the login attempt, creates a LoginViewModel with them, then
     * creates an authentication token with those details which is passed to the Spring Security
     * builtin authenticate() method to perform the authentication against the database.
     * <p>
     * Automatically triggered when we issue POST request to /login, no need to set it up as an
     * endpoint
     *
     * @param request  The HTTP request sent to the /login method, containing a username and password
     * @param response An empty HTTP response object
     * @return An Authentication object, which is then handled by the successfulAuthentication
     * method
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        
        //Grab credentials and map them to loginviewmodel
        LoginViewModel credentials = null;
        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(),
                    LoginViewModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //Create login token - this is not the JWT but a token used internally by Spring Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword(),
                new ArrayList<>());
        
        //Authenticate user using Spring Security
        Authentication auth = authenticationManager.authenticate(authenticationToken);
        return auth;
        
    }
    
    /**
     * Overrrides the Spring Security successfulAuthentication method to tell it how to create
     * the JWT in the way which we want
     * <p>
     * This method will be called if the authentication in the attemptAuthentication method was
     * successful. It will create a UserPrincipal from the data contained in the authentication
     * result, and then use the username together with the properties declared in the
     * JwtProperties class to construct a JWT which is returned to the client
     *
     * @param request    The HTTP request containing the username and password
     * @param response   An empty HTTP response object
     * @param chain
     * @param authResult The result of the authentication attempt in the previous method
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
       
       /* Grab Principal (a Spring Security thing) from the authentication result and use it to
         create a UserPrincipal object*/
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
        
       /* Create JWT Token with the relevant username as the subject, the expiry date set to a
         certain time in the future based on the EXPIRATION_TIME set in the JwtProperties, and
         signed using the HMAC512 algorithm and our SECRET as set in JwtProperties*/
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .sign(HMAC512(JwtProperties.SECRET.getBytes()));
        
        
        /*Add the created JWT to Json object in the format
        token: BEARER (jwt)
        expiresIn: (expiration time)*/
        JsonObject authResponse = new JsonObject();
        authResponse.addProperty("token", JwtProperties.TOKEN_PREFIX + token);
        authResponse.addProperty("expiresIn", JwtProperties.EXPIRATION_TIME);
        
        
        /*Add the JSON object representing the JWT to the Http Response so it can be returned to
         the client*/
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(authResponse.toString());
        
        
    }
}
