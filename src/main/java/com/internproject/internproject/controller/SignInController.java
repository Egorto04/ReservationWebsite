package com.internproject.internproject.controller;


import com.internproject.internproject.entity.User;
import com.internproject.internproject.service.CompanyService;
import com.internproject.internproject.user.WebUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignInController {
    CompanyService companyService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SignInController(CompanyService companyService) {
        this.companyService = companyService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/create-account")
    public String createAccount(Model model) {
        WebUser wu = new WebUser();
        model.addAttribute("webUser", wu);
        return "create-account";
    }

    @PostMapping("/creatingAccount")
    public String createAccount(@Valid @ModelAttribute("webUser") WebUser theUser, RedirectAttributes redirectAttributes, @RequestParam("password") String password,  @RequestParam("confirmPassword") String confirmPassword, BindingResult bindingResult) {
        if (companyService.findUserByUsername(theUser.getUsername()) != null) {
            bindingResult.rejectValue("username", "error.webUser", "An account already exists for this username.");
        }
        if (!password.equals(confirmPassword))
        {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/create-account";
        }
        User u = new User();
        u.setUsername(theUser.getUsername());
        u.setPassword(passwordEncoder.encode(theUser.getPassword()));
        u.setEmail(theUser.getEmail());
        u.setFirstName(theUser.getFirstName());
        u.setLastName(theUser.getLastName());
        u.setEnabled(true);
        companyService.saveUser(u);
        return "redirect:/showLoginPage";
    }

}
