package craft.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //this "configure" defines a datasource for the users
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
    auth.inMemoryAuthentication()
            .withUser("admin").password(passwordEncoder().encode("admin123")).roles("ADMIN")
            .and()
            .withUser("bob").password(passwordEncoder().encode("bob123")).roles("USER")
            .and()
            .withUser("sally").password(passwordEncoder().encode("sally123")).roles("USER");

    }

    //this one authorises the requests
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors()
                //well aware that disabling this is a security issue, but gotta move on
                .and()
                .authorizeRequests()
                //https://stackoverflow.com/questions/18399433/spring-security-java-config-how-to-add-the-method-type
                //anyone at all can view all listings

                .antMatchers(HttpMethod.DELETE,"/projects/**").authenticated()
                //only logged in users can create
                .antMatchers(HttpMethod.POST,"/projects").authenticated()
                .antMatchers(HttpMethod.GET,"/projects").permitAll()
                .and()
                .httpBasic();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
