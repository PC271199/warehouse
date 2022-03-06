package com.example.Warehouse.services;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.Warehouse.pojo.Mail;

 

 
@Service
public class MailServiceImpl implements MailService {
 
    @Autowired
    JavaMailSender mailSender;
    public void sendEmail(Mail mail,String fileToAttach) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
 
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            
            mimeMessageHelper.setSubject("Confirmation Mail");
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "phuoccong99@gmail.com"));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText("");
            FileSystemResource file = new FileSystemResource(new File(fileToAttach));
            mimeMessageHelper.addAttachment("qrcode.png", file);
            mailSender.send(mimeMessageHelper.getMimeMessage());
 
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public void sendEmailPassword(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
 
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "phuoccong99@gmail.com"));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent());
            mailSender.send(mimeMessageHelper.getMimeMessage());
 
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
