package com.example.assetmanagement.integrationtest;

import com.example.assetmanagement.dto.ServiceRequestDTO;
import com.example.assetmanagement.entity.*;
import com.example.assetmanagement.enums.*;
import com.example.assetmanagement.repository.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ServiceRequestRestIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private AssetRepo assetRepo;
    @Autowired private EmployeeRepo employeeRepo;
    @Autowired private AssetCategoryRepo assetCategoryRepo;
    @Autowired private AssetAllocationRepo allocationRepo;
    @Autowired private ServiceRequestRepo serviceRequestRepo;

    private Long assetId;
    private Long requestId;

    @BeforeEach
    void setup() {

        AssetCategory category = new AssetCategory();
        category.setCategoryName("Electronics");
        assetCategoryRepo.save(category);

        Employee emp = new Employee();
        emp.setUsername("johndoe");
        emp.setName("John Doe");
        emp.setEmail("john@example.com");
        emp.setContactNumber("9876543210");
        emp.setAddress("123 Main Street");
        emp.setPassword("secure123");
        emp.setGender(Gender.MALE);
        emp.setRole(UserRole.EMPLOYEE);
        emp.setEmpstatus(EmployeeStatus.ACTIVE);
        employeeRepo.save(emp);

        Asset asset = new Asset();
        asset.setAssetNo("AST-" + UUID.randomUUID().toString().substring(0, 6));
        asset.setAssetName("Laptop");
        asset.setAssetModel("HP Envy");
        asset.setDescription("Test laptop asset");
        asset.setImageUrl("http://example.com/laptop.png");
        asset.setManufacturingDate(LocalDate.now().minusYears(2));
        asset.setExpiryDate(LocalDate.now().plusYears(2));
        asset.setAssetValue(65000.0);
        asset.setAssetStatus(AssetStatus.ALLOCATED);
        asset.setCategory(category);
        assetRepo.save(asset);

        AssetAllocation allocation = new AssetAllocation();
        allocation.setAsset(asset);
        allocation.setEmployee(emp);
        allocation.setAllocationDate(LocalDateTime.now().minusDays(1));
        allocation.setAllocationStatus(AllocationStatus.ALLOCATED);
        allocationRepo.save(allocation);

        this.assetId = asset.getId();

        ServiceRequest request = new ServiceRequest();
        request.setAsset(asset);
        request.setEmployee(emp);
        request.setDescription("Battery issue");
       request.setIssueType(IssueType.HARDWARE);         request.setStatus(RequestStatus.PENDING);
        request.setRequestDate(LocalDateTime.now());
        serviceRequestRepo.save(request);

        this.requestId = request.getId();
    }

    @Test
    @WithMockUser(username = "john@example.com", roles = "EMPLOYEE")
    void submitRequest_AsEmployee_Success() throws Exception {
        ServiceRequestDTO dto = new ServiceRequestDTO();
        dto.setAssetId(assetId);
        dto.setDescription("Screen flickering");
        dto.setIssueType(IssueType.HARDWARE); 
        mockMvc.perform(post("/api/service-requests/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(jsonPath("$.status").value("success"))
.andExpect(jsonPath("$.message").value("Service request submitted successfully."));

    }

    @Test
    @WithMockUser(username = "john@example.com", roles = "EMPLOYEE")
    void getOwnRequests_AsEmployee_Success() throws Exception {
        mockMvc.perform(get("/api/service-requests/my-requests"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].description").value("Battery issue"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllRequests_AsAdmin_Success() throws Exception {
        mockMvc.perform(get("/api/service-requests/admin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void approveRequest_AsAdmin_Success() throws Exception {
        mockMvc.perform(put("/api/service-requests/{id}/approve", requestId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void rejectRequest_AsAdmin_Success() throws Exception {
        mockMvc.perform(put("/api/service-requests/{id}/reject", requestId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateStatus_AsAdmin_Success() throws Exception {
        mockMvc.perform(put("/api/service-requests/{id}/status", requestId)
                .param("status", "APPROVED"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void submitRequest_Unauthorized() throws Exception {
        ServiceRequestDTO dto = new ServiceRequestDTO();
        dto.setAssetId(assetId);
        dto.setDescription("WiFi not working");
        dto.setIssueType(IssueType.HARDWARE); 
        mockMvc.perform(post("/api/service-requests/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }
}
