package alex.ork.springboot.course.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // add a reference to our security data source
    @Autowired
    @Qualifier("securityDataSource")
    private DataSource securityDataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // use jdbc authentication
        auth.jdbcAuthentication().dataSource(securityDataSource);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/grammar/showForm*").hasRole("ADMIN")
                .antMatchers("/grammar/save*").hasRole("ADMIN")
                .antMatchers("/grammar/delete").hasRole("ADMIN")
                .antMatchers("/grammar/**").hasAnyRole("ANONYMOUS", "USER", "ADMIN")
                .antMatchers("/words/showForm*").hasRole("ADMIN")
                .antMatchers("/words/save*").hasRole("ADMIN")
                .antMatchers("/words/delete").hasRole("ADMIN")
                .antMatchers("/words/**").hasAnyRole("ANONYMOUS", "USER", "ADMIN")
                .antMatchers("/jrCourse").hasAnyRole("ANONYMOUS", "USER", "ADMIN")
                .antMatchers("/jlpt").hasAnyRole("ANONYMOUS", "USER", "ADMIN")
                .antMatchers("/users").hasRole("ADMIN")
                .antMatchers("/personal/**").hasRole("USER")
                .antMatchers("/resources/**").permitAll()
            .and()
            .formLogin()
                .loginPage("/showLoginPage")
                .loginProcessingUrl("/authenticateTheUser")
                .permitAll()
            .and()
            .logout().permitAll();
    }
}
