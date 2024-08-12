package com.internproject.internproject.security;

import com.internproject.internproject.entity.User;
import com.internproject.internproject.service.CompanyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler  implements AuthenticationSuccessHandler {

    private CompanyService companyService;

    public CustomAuthenticationSuccessHandler(CompanyService companyService) {
        this.companyService = companyService;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("onAuthenticationSuccess");
        String username = authentication.getName();
        System.out.println("username: " + username);

        User theUser = null;

        try {
            theUser = companyService.findUserByUsername(username);
        }catch (Exception e){
            throw new ServletException(e);
        }

        System.out.println("theUser: " + theUser);

        HttpSession session = request.getSession();
        session.setAttribute("user", theUser);

        response.sendRedirect("/main-page/home");
    }
}
