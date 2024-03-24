package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.model.User;
import io.mountblue.blogapplication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }

    @GetMapping("/register")
    public String showRegistration(Model model){
        User user = new User();
        model.addAttribute("user", user);
        System.out.println("in register");
        return "register";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user, Model model){
        if(userService.isUserExist(user.getUsername())){
            model.addAttribute("error", "Username already exist. Please use different username");
            return "register";
        } else {
            userService.saveUser(user);
        }
        return "redirect:/login";
    }


}
