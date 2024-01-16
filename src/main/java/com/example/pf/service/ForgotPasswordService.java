package com.example.pf.service;

import com.example.pf.entities.ForgotPassword;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    @Autowired
    JavaMailSender javaMailSender;

    private final int MINUTES = 10;

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public LocalDateTime expireTimeRange() {
        return LocalDateTime.now().plusMinutes(MINUTES);
    }

    public void sendEmail(String to, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String emailContent = "<p>Hello</p>"
                + "Click the link the below to reset password"
                + "<p><a href=\""+ emailLink + "\">Change My Password</a></p>"
                + "<br>"
                + "Ignore this Email if you did not made the request";
        helper.setText(emailContent, true);
        helper.setFrom("cpamomi1221@gmail.com", "Support Technique");
        helper.setSubject(subject);
        helper.setTo(to);
        javaMailSender.send(message);
    }

    public boolean isExpired (ForgotPassword forgotPasswordToken) {
        return LocalDateTime.now().isAfter(forgotPasswordToken.getExpireTime());
    }

    public String checkValidity (ForgotPassword forgotPasswordToken, Model model) {

        if (forgotPasswordToken == null) {
            model.addAttribute("error", "Token Invalide !");
            return "error-page";
        }

        else if (forgotPasswordToken.isUsed()) {
            model.addAttribute("error", "Ce Token est deja utilise !");
            return "error-page";
        }

        else if (isExpired(forgotPasswordToken)) {
            model.addAttribute("error", "Ce Token a expire !");
            return "error-page";
        }
        else {
            return "reset-password";
        }


    }



}