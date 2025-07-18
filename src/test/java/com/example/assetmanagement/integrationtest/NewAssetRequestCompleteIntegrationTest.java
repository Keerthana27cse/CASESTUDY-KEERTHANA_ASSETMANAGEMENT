package com.example.assetmanagement.integrationtest;

import com.example.assetmanagement.entity.*;
import com.example.assetmanagement.enums.*;
import com.example.assetmanagement.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback
public class NewAssetRequestCompleteIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private NewAssetRequestRepo requestRepo;
    @Autowired private EmployeeRepo employeeRepo;
    @Autowired private AssetRepo assetRepo;
    @Autowired private AssetCategoryRepo categoryRepo;

    private Long requestId;

    @BeforeEach
    public void setup() {

Employee employee = new Employee();
employee.setUsername("jdoe123");
employee.setName("John Doe");
employee.setEmail("jdoe@example.com");
employee.setContactNumber("9876543210");
employee.setAddress("123 Test Street, Test City");
employee.setPassword("securePass123");
employee.setGender(Gender.MALE);
employee.setRole(UserRole.EMPLOYEE); // or UserRole.ADMIN if needed
employeeRepo.save(employee);


        AssetCategory cat = new AssetCategory();
        cat.setCategoryName("Laptop");
        categoryRepo.save(cat);

        Asset asset = new Asset();
        asset.setAssetNo("A-001");
        asset.setAssetName("Dell XPS");
        asset.setAssetModel("XPS13");
        asset.setDescription("High-end laptop");
        asset.setImageUrl("http://example.com/image.png");
        asset.setManufacturingDate(LocalDate.of(2023, 1, 1));
        asset.setExpiryDate(LocalDate.of(2027, 1, 1));
        asset.setAssetValue(100000.0);
        asset.setAssetStatus(AssetStatus.AVAILABLE);
        asset.setCategory(cat);
        assetRepo.save(asset);

        NewAssetRequest req = new NewAssetRequest();
        req.setEmployee(employee);
        req.setAsset(asset);
        req.setRequestedCategory(cat);
        req.setDescription("Need laptop");
        req.setRequestReason("Software Development");
        req.setStatus(RequestStatus.RETURNED);
        req.setRequestDate(LocalDateTime.now());
        req.setFullAddress("Chennai");
        req.setZipCode("600001");
        req.setPhone("9876543210");
        requestRepo.save(req);

        requestId = req.getId();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void completeRequest_shouldSucceed_whenStatusIsReturned() throws Exception {
        mockMvc.perform(put("/api/asset-requests/complete/" + requestId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Request marked as COMPLETED."));

        NewAssetRequest updated = requestRepo.findById(requestId).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(RequestStatus.COMPLETED);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void completeRequest_shouldFail_whenRequestNotReturnedOrResolved() throws Exception {
        NewAssetRequest req = requestRepo.findById(requestId).orElseThrow();
        req.setStatus(RequestStatus.PENDING);
        requestRepo.save(req);

        mockMvc.perform(put("/api/asset-requests/complete/" + requestId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Only RETURNED or RESOLVED requests can be completed."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void completeRequest_shouldFail_whenRequestNotFound() throws Exception {
        mockMvc.perform(put("/api/asset-requests/complete/99999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Request not found."));
    }
}
