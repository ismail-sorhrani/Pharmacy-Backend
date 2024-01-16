package com.example.pf.controller;

import com.example.pf.entities.ForgotPassword;
import com.example.pf.entities.User;
import com.example.pf.repo.ForgotPasswordRepository;
import com.example.pf.repo.UserRepository;
import com.example.pf.service.ForgotPasswordService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserRepository userService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    ForgotPasswordRepository forgotPasswordRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("/password-request")
    public String passwordRequest() {

        return "password-request";
    }

    @PostMapping("/password-request")
    public String savePasswordRequest(@RequestParam("email") String email, Model model) {
        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "Cet email est introuvable !");
            return "password-request";
        }

        ForgotPassword forgotPasswordToken = new ForgotPassword();
        forgotPasswordToken.setExpireTime(forgotPasswordService.expireTimeRange());
        forgotPasswordToken.setToken(forgotPasswordService.generateToken());
        forgotPasswordToken.setUser(user);
        forgotPasswordToken.setUsed(false);

        forgotPasswordRepository.save(forgotPasswordToken);

        String emailLink = "http://monorail.proxy.rlwy.net:29780/reset-password?token=" + forgotPasswordToken.getToken();

        try {
            forgotPasswordService.sendEmail(user.getEmail(), "Password Reset Link", emailLink);
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Erreur d'envoie d'email");
            e.printStackTrace();
            return "password-request";
        }


        return "redirect:/password-request?success";
    }

    @GetMapping("/reset-password")
    public String resetPassword(@Param(value="token") String token, Model model, HttpSession session) {

        session.setAttribute("token", token);
        ForgotPassword forgotPasswordToken = forgotPasswordRepository.findByToken(token);
        return forgotPasswordService.checkValidity(forgotPasswordToken, model);

    }

    @PostMapping("/reset-password")
    public String saveResetPassword(HttpServletRequest request, HttpSession session, Model model) {
        String password = request.getParameter("password");
        String token = (String)session.getAttribute("token");

        ForgotPassword forgotPasswordToken = forgotPasswordRepository.findByToken(token);
        User user = forgotPasswordToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        forgotPasswordToken.setUsed(true);
        userService.save(user);
        forgotPasswordRepository.save(forgotPasswordToken);

        model.addAttribute("message", "Vous avez change votre mot de passe avec succes");

        return "reset-password";
    }

}
