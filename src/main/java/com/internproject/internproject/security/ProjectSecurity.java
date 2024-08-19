package com.internproject.internproject.security;

import com.internproject.internproject.service.CompanyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class ProjectSecurity {

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(CompanyService companyService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(companyService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationSuccessHandler customSuccessHandler) throws Exception {

        http.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/create-account", "/creatingAccount", "/showLoginPage").permitAll()
                        .anyRequest().authenticated()
        ).formLogin(form ->
                form
                        .loginPage("/showLoginPage")
                        .loginProcessingUrl("/authenticateTheUser")
                        .successHandler(customSuccessHandler)
                        .permitAll()
        ).logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/showLoginPage?logout")
                .permitAll()
        );
        return http.build();
    }
}
