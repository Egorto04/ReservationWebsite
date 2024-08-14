package com.internproject.internproject.controller;


import com.internproject.internproject.entity.User;
import com.internproject.internproject.service.CompanyService;
import com.internproject.internproject.user.WebUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignInController {
    CompanyService companyService;

    @Autowired
    public SignInController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/create-account")
    public String createAccount(Model model) {
        WebUser wu = new WebUser();
        model.addAttribute("webUser", wu);
        return "create-account";
    }

    @PostMapping("/creatingAccount")
    public String createAccount(@Valid @ModelAttribute("webUser") WebUser theUser, BindingResult bindingResult) {
        if (companyService.findUserByUsername(theUser.getUsername()) != null) {
            bindingResult.rejectValue("username", "error.webUser", "An account already exists for this username.");
        }
        if (bindingResult.hasErrors()) {
            return "create-account";
        }

        User u = new User();
        u.setUsername(theUser.getUsername());
        u.setPassword("{noop}"+theUser.getPassword());
        u.setEmail(theUser.getEmail());
        u.setFirstName(theUser.getFirstName());
        u.setLastName(theUser.getLastName());
        u.setEnabled(true);
        companyService.saveUser(u);
        return "redirect:/showLoginPage";
    }

}
