package com.internproject.internproject.security;

import com.internproject.internproject.service.CompanyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
public class ProjectSecurity {

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(CompanyService companyService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(companyService);
        return daoAuthenticationProvider;
    }

//    @Bean
//    UserDetailsService userDetailsService(DataSource dataSource) {
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//
//        userDetailsManager.setUsersByUsernameQuery("select user_id, pw, active from members where user_id=?");
//        userDetailsManager.setAuthoritiesByUsernameQuery("select user_id, role from roles where user_id=?");
//
//        return userDetailsManager;
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationSuccessHandler custom) throws Exception {

        http.authorizeHttpRequests(authorize->
                authorize
                        .requestMatchers("/create-account").permitAll()
                        .requestMatchers("/creatingAccount").permitAll()
                        .requestMatchers("/showLoginPage").permitAll()
                        .anyRequest().authenticated()
        ).formLogin(form ->
                form
                        .loginPage("/showLoginPage")
                        .loginProcessingUrl("/authenticateTheUser")
                        .successHandler(custom)
                        .permitAll()

        ).logout(logout -> logout.permitAll()
        );
        return http.build();
    }

}
