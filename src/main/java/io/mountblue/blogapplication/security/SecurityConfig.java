package io.mountblue.blogapplication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select username, password, enabled from users where username=?"
        );
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select username, role from roles where username=?"
        );
        return  jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/", "/search", "/filters",
                                        "/register", "/post/{postId}", "/saveUser").permitAll()
                                .requestMatchers("/**").hasRole("AUTHOR")
                                .requestMatchers("/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .formLogin(form ->
                        form.loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll())
                .logout(logout ->
                        logout.permitAll())
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/accessDenied")
                );
        return httpSecurity.build();
    }
}
