package com.internproject.internproject.security;

import com.internproject.internproject.entity.User;
import com.internproject.internproject.service.CompanyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final CompanyService companyService;

    public CustomAuthenticationSuccessHandler(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();

        User theUser;

        try {
            theUser = companyService.findUserByUsername(username);
        } catch (Exception e) {
            throw new ServletException(e);
        }

        // Store the user in the session
        HttpSession session = request.getSession();
        session.setAttribute("user", theUser);

        // Redirect to the main page
        response.sendRedirect("/main-page/home");
    }
}
