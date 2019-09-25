package craft.app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import craft.app.db.UserRepository;
import craft.app.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 * Overrides the Spring Security BasicAuthenticationFilter. Checks that http requests have a
 * valid JWT attached where required before allowing access to resources.
 *
 * Based on a tutorial found at: /https://www.youtube.com/watch?v=xrxWc7TG0zA&list=PLVApX3evDwJ1d0lKKHssPQvzv2Ao3e__Q&index=30
 *
 */
public class JwtAuthorisationFilter extends BasicAuthenticationFilter {
    
    /**
     * An instance of the user repository so that the list of users can be accessed
     */
    private UserRepository userRepository;
    
    /**
     * Constructor, injects dependencies for AuthenticationManager which is required by the
     * superclass, and userrepository to allow db access
     * @param authenticationManager
     * @param userRepository Allows access to the database
     */
    public JwtAuthorisationFilter(AuthenticationManager authenticationManager,
                                  UserRepository userRepository){
        super(authenticationManager);
        this.userRepository = userRepository;
    }
    
    
    /**
     * Checks if a http request has a JWT attached - if so, get the details from it and pass them
     * through to Spring Security
     *
     * @param request
     * @param response
     * @param chain The chain of filters which are built into Spring Security
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //Read the authorization header, where the jwt token should be
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        
        //If header does not start with BEARER or is null delegate to Spring Security and exit
        //i.e. if a user is not logged in then skip checking the JWT and continue onwards with
        // the built in security methods
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }
        
        //if header is present, try grab user principal for the user and perform
        // authorisation
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        //continue filter exectuion
        chain.doFilter(request, response);
    }
    
    
    /**
     * Extracts the username from the JWT, then finds that user in the database and returns a
     * Spring Security token which includes the roles/permissions of that user (in this app,
     * roles and permissions are not implemented so just returns that the user was authenticated)
     * @param request The http request which was sent to the API
     * @return
     */
    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request){
        String token = request.getHeader(JwtProperties.HEADER_STRING);
        
        //if a token exists on the http request (it should always as this method is called after
        // checking for a token)
        if (token != null){
            //parse the token using the same algorithm and secret used to encode it,
            // validate/verify it, extract the username
            String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(token.replace(JwtProperties.TOKEN_PREFIX, ""))
                    .getSubject();
            
            //Search in the db for user with token subject username
            //if so, grab user details and create spring auth token
            if (username != null){
                User user = userRepository.findByUsername(username);
                UserPrincipal principal = new UserPrincipal(user);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, principal.getAuthorities());
                return auth;
            }
            return null;
        }
        return null;
    }
}
