package com.example.pf;

import com.example.pf.dto.UserDto;
import com.example.pf.entities.Pharmacie;
import com.example.pf.entities.Role;
import com.example.pf.entities.User;
import com.example.pf.service.CustomUserDetails;
import com.example.pf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserDetailsService userDetailsService;



    private UserService userService;
    public UserController(UserService userService) {

        this.userService = userService;
    }


    @GetMapping("/pharm")
    public String prof(Model model,Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail" , userDetails);
        return "pharmacien";
    }
    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail" , userDetails);
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, UserDto userDto) {
        model.addAttribute("user", userDto);
        return "login";
    }


    @GetMapping("/register")
    public String register(Model model, UserDto userDto) {

        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register")
    public String registerSave(@ModelAttribute("user") UserDto userDto, Model model) {
        User user = userService.findByUsername(userDto.getUsername());
        if (user != null) {
            model.addAttribute("userexist", user);
            return "register";

        }
        userService.save(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/check")
    public String role(Authentication authentication) {
        CustomUserDetails user  = (CustomUserDetails)authentication.getPrincipal();
        boolean isAdmin = false;
         String role =user.getRole().toString();
        System.out.println(role);
            if(role.equals("ADMIN")){
                isAdmin = true;
            }

        if(isAdmin){
            return "redirect:/admin";
        }else{
            return "redirect:/pharmacien/show";
        }
    }

}
