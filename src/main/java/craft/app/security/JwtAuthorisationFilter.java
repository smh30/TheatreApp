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


//todo figure out if this class is necessary, or is it only for
//cases with user roles??
//https://www.youtube.com/watch?v=xrxWc7TG0zA&list=PLVApX3evDwJ1d0lKKHssPQvzv2Ao3e__Q&index=30
public class JwtAuthorisationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;
    
    public JwtAuthorisationFilter(AuthenticationManager authenticationManager,
                                  
                                  UserRepository userRepository){
        super(authenticationManager);
        this.userRepository = userRepository;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //Read the auth header, where the jwt token should be
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        
        //If header does not contain BEARER or is null delegate to Spring impl and exit
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }
        
        //if header is present, try grab user principal from db and authenticate
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        //continue filter exectuion
        chain.doFilter(request, response);
    }
    
    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request){
        String token = request.getHeader(JwtProperties.HEADER_STRING);
        if (token != null){
            //parse the token and validate it
            String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(token.replace(JwtProperties.TOKEN_PREFIX, ""))
                    .getSubject();
            
            //Search in th db for user with toekn subject username
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
