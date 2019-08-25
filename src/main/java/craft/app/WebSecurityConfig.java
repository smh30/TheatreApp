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
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private UserPrincipalDetailsService userPrincipalDetailsService;
    private UserRepository userRepository;
 
    
    public WebSecurityConfig(UserPrincipalDetailsService userPrincipalDetailsService,
                             UserRepository userRepository) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
        this.userRepository = userRepository;
       
    }
    
    //this "configure" defines a datasource for the users
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
    auth
        .authenticationProvider(authenticationProvider());
    }

   //this one authorises the requests/protects the resources
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                //well aware that disabling this is a security issue, but gotta move on
                
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorisationFilter(authenticationManager(),
                        userRepository))
                
                
                .authorizeRequests()
                //https://stackoverflow.com/questions/18399433/spring-security-java-config-how-to-add-the-method-type
                .antMatchers("/login").permitAll()
                .antMatchers(HttpMethod.DELETE,"/projects/**").authenticated()
                .antMatchers(HttpMethod.POST,"/projects").authenticated()
                .antMatchers(HttpMethod.GET,"/projects").permitAll()
                .antMatchers(HttpMethod.GET, "/users").authenticated();
                
    }
    
    
    
    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);
        
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
