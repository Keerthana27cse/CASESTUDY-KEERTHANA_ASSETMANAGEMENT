package com.example.assetmanagement.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.assetmanagement.service.EmailService;

@RestController
@RequestMapping("/api")
public class EmailController {
  
  @Autowired EmailService emailService;
  
@PreAuthorize("hasRole('EMPLOYEE')")
  @PostMapping("/send-pdf-email")
  public ResponseEntity<String> sendPdfEmail(
      @RequestParam("file") MultipartFile file,
      @RequestParam("email") String email) {
    try {
      emailService.sendEmailWithAttachment(email, file);
      return ResponseEntity.ok("Email sent!");
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body("Error sending email.");
    }
  }
}
