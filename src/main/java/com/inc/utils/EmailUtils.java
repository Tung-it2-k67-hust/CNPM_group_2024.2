package com.inc.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender emailSender;

    // Method to send simple message:create and send
    public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("dosontung2367@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if(list != null && !list.isEmpty()){
            message.setCc(getCcArray(list));
        }
        emailSender.send(message);
    }

    private String[] getCcArray(List<String> ccList) {//cc la ban copy gui cho nhung nguoi khac
        String[] cc = new String[ccList.size()];
        for(int i = 0; i < ccList.size(); i++){
            cc[i] = ccList.get(i);
        }
        return cc;
    }

    // Method to send HTML message
    public void forgotMail(String to, String subject, String password) throws MessagingException{
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("dosontung2367@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg = "<p><b>Your Login details for Coffee Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
        message.setContent(htmlMsg, "text/html");
        emailSender.send(message);
    }
}
