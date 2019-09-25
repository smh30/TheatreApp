package craft.app;

import craft.app.db.UserRepository;
import craft.app.security.JwtAuthenticationFilter;
import craft.app.security.JwtAuthorisationFilter;
import craft.app.security.UserPrincipalDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * Sets up Spring Security for the app.
 * <p>
 * With thanks to RomanianCoder's Youtube series "Spring Boot Security"
 */
@Configuration
@EnableWebSecurity
@CrossOrigin
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    /**
     * Required to allow Spring Security to access the user details
     */
    private UserPrincipalDetailsService userPrincipalDetailsService;
    
    /**
     * Required to allow Spring Security to access the User repository to pass it through to the
     * AuthorisationFilter
     */
    private UserRepository userRepository;
    
    
    public WebSecurityConfig(UserPrincipalDetailsService userPrincipalDetailsService,
                             UserRepository userRepository) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
        this.userRepository = userRepository;
        
    }
    
    
    /**
     * this "Configure" defines a datasource for the users, in this case an
     * authenticationProvider as declared further down in this same class. Could also be
     * in-memory user information or other sources
     *
     * @param auth A dependency required by Spring Security
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(authenticationProvider());
    }
    
    
    /**
     * This "Configure" authorises http requests / protects resources as specified
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //allow cross-origin requests so that the Angular app can contact the Spring Boot app
        http.cors().and()
                .csrf().disable()
                
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                
                //add the authentication and authorisation filters that we have set up to the Spring
                // Security pipeline
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorisationFilter(authenticationManager(),
                        userRepository))
                
                //define which API endpoints will be publicly available and which will require
                // authentication. Could also lock endpoints to certain user roles here. With
                // thanks to https://stackoverflow.com/questions/18399433/spring-security-java-config-how-to-add-the-method-type
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers(HttpMethod.DELETE, "/projects/**").authenticated()
                .antMatchers(HttpMethod.POST, "/projects/**").authenticated()
                .antMatchers(HttpMethod.GET, "/projects").permitAll()
                .antMatchers(HttpMethod.GET, "/users/byUsername/**").permitAll()
                .antMatchers(HttpMethod.GET, "/users/**").authenticated();
        
        
    }
    
    
    /**
     * Sets up the app to receive authentication details from the database via the
     * UserPrincipalDetailsService
     *
     * @return
     */
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);
        
        return daoAuthenticationProvider;
    }
    
    
    /**
     * Required for CORS to work
     *
     * @return
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }
    
    /**
     * Defines the encryption to use when storing/retrieving passwords in the database
     *
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
