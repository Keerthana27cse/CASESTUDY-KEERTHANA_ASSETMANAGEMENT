package com.example.assetmanagement.integrationtest;

import com.example.assetmanagement.dto.LoginRequest;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.entity.User;
import com.example.assetmanagement.enums.EmployeeStatus;
import com.example.assetmanagement.enums.Gender;
import com.example.assetmanagement.enums.UserRole;
import com.example.assetmanagement.repository.AssetAllocationRepo;
import com.example.assetmanagement.repository.AssetAuditRepo;
import com.example.assetmanagement.repository.EmployeeRepo;
import com.example.assetmanagement.repository.NewAssetRequestRepo;
import com.example.assetmanagement.repository.ServiceRequestRepo;
import com.example.assetmanagement.repository.UserRepo;
import com.example.assetmanagement.restcontroller.UserRest;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") 
public class UserRestIntegrationTest {

    @Autowired
private UserRest userRest;

@Test
void contextLoads() {
    assertNotNull(userRest);  
}

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepo userRepo;
    @Autowired private EmployeeRepo employeeRepo;
    @Autowired private AssetAuditRepo assetAuditRepo;
    @Autowired private AssetAllocationRepo assetAllocationRepo;
    @Autowired private NewAssetRequestRepo newAssetRequestRepo;
    @Autowired private ServiceRequestRepo serviceRequestRepo;
    @Autowired private PasswordEncoder passwordEncoder;
@BeforeEach
void setUp() {
    serviceRequestRepo.deleteAll();
    newAssetRequestRepo.deleteAll();
    assetAllocationRepo.deleteAll();
    assetAuditRepo.deleteAll();
    userRepo.deleteAll();
    employeeRepo.deleteAll();

    User user = new User();
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user.setPassword(passwordEncoder.encode("123"));  // encode "123" using BCrypt
    user.setUserrole(UserRole.EMPLOYEE);
    userRepo.save(user);

    Employee emp = new Employee();
    emp.setUsername("testuser"); 
    emp.setName("Test User");
    emp.setEmail("test@example.com");
    emp.setPassword("{noop}password"); 
    emp.setContactNumber("9876543210");
    emp.setAddress("123 Main St");
    emp.setGender(Gender.FEMALE);
    emp.setRole(UserRole.EMPLOYEE);
    emp.setEmpstatus(EmployeeStatus.ACTIVE);
    emp.setCreatedAt(LocalDateTime.now());

    employeeRepo.save(emp);
}

    @Test
    void testLogin_Success() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
request.setPassword("123");
mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    void testLogin_Failure() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("invalid@example.com");
        request.setPassword("wrong");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void testForgotPassword_Success() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("newPassword");

        mockMvc.perform(post("/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    void testForgotPassword_Failure() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test56@example.com");
        request.setPassword("newPassword");

        mockMvc.perform(post("/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}
