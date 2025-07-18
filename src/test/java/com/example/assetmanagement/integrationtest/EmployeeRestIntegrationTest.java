package com.example.assetmanagement.integrationtest;

import com.example.assetmanagement.dto.EmployeeDTO;
import com.example.assetmanagement.enums.Gender;
import com.example.assetmanagement.enums.EmployeeStatus;
import com.example.assetmanagement.repository.EmployeeRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class EmployeeRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Long testEmployeeId;

    @BeforeEach
    public void setup() {
        employeeRepo.deleteAll();

        var emp = new com.example.assetmanagement.entity.Employee();
        emp.setUsername("testuser");
        emp.setName("Test User");
        emp.setEmail("testuser@example.com");
        emp.setContactNumber("1234567890");
        emp.setAddress("123 Test St");
        emp.setPassword("password123");
        emp.setGender(Gender.MALE);
        emp.setEmpstatus(EmployeeStatus.ACTIVE);

        emp = employeeRepo.save(emp);
        testEmployeeId = emp.getId();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void givenAdmin_whenGetAllEmployees_thenReturnList() throws Exception {
        mockMvc.perform(get("/api/employees"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"EMPLOYEE"})
    public void givenEmployee_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        mockMvc.perform(get("/api/employees/{id}", testEmployeeId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"EMPLOYEE"})
    public void givenEmployee_whenUpdateEmployee_thenSuccess() throws Exception {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(testEmployeeId);
        dto.setUsername("updateduser");
        dto.setName("Updated User");
        dto.setEmail("updateduser@example.com");
        dto.setContactNumber("0987654321");
        dto.setAddress("456 Updated St");
        dto.setGender(Gender.MALE);
        dto.setStatus(EmployeeStatus.ACTIVE);

        mockMvc.perform(put("/api/employees/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(content().string("Employee updated successfully!"));

        var updated = employeeRepo.findById(testEmployeeId).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getUsername()).isEqualTo("updateduser");
        assertThat(updated.getContactNumber()).isEqualTo("0987654321");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void givenAdmin_whenDeleteEmployee_thenSuccess() throws Exception {
        mockMvc.perform(delete("/api/employees/{id}", testEmployeeId))
            .andExpect(status().isOk())
            .andExpect(content().string("Employee and corresponding user deleted successfully"));

        boolean exists = employeeRepo.existsById(testEmployeeId);
        assertThat(exists).isFalse();
    }

    @Test
    public void whenNoAuth_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/employees"))
        .andExpect(status().isUnauthorized());

    }
}
