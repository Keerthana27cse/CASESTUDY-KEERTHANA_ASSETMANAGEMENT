package com.example.assetmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;

  public void sendEmailWithAttachment(String to, MultipartFile file) throws MessagingException, IOException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(to);
    helper.setSubject("Your Asset Request PDF");
    helper.setText("Hi, please find your Asset Request summary attached.");

    helper.addAttachment("Asset_Request.pdf", new ByteArrayResource(file.getBytes()));
    mailSender.send(message);
  }
}
