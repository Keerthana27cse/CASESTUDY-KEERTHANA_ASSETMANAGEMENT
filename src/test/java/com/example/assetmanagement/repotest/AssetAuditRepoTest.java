package com.example.assetmanagement.repotest;

import com.example.assetmanagement.entity.*;
import com.example.assetmanagement.enums.*;
import com.example.assetmanagement.repository.AssetAuditRepo;
import com.example.assetmanagement.repository.AssetCategoryRepo;
import com.example.assetmanagement.repository.AssetRepo;
import com.example.assetmanagement.repository.EmployeeRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AssetAuditRepoTest {

    @Autowired
    private AssetAuditRepo assetAuditRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private AssetCategoryRepo assetCategoryRepo;

    private Employee employee;
    private Asset asset;
    private AssetAudit audit;
    private LocalDateTime auditDate;

    @BeforeEach
    void setUp() {
        // Create employee
        employee = new Employee();
        employee.setUsername("keerthana123");
        employee.setName("Keerthana R");
        employee.setEmail("keerthana@example.com");
        employee.setContactNumber("9876543210");
        employee.setAddress("Chennai");
        employee.setPassword("pass1234");
        employee.setGender(Gender.FEMALE);
        employee.setRole(UserRole.EMPLOYEE);
        employee.setEmpstatus(EmployeeStatus.ACTIVE);
        employee = employeeRepo.save(employee);

        // Create category
        AssetCategory category = new AssetCategory("IT Equipment");
        category = assetCategoryRepo.save(category);

        // Create asset
        asset = new Asset();
        asset.setAssetNo("AST-2001");
        asset.setAssetName("Mouse");
        asset.setAssetModel("Logitech M331");
        asset.setDescription("Silent wireless mouse");
        asset.setManufacturingDate(LocalDate.of(2022, 6, 1));
        asset.setExpiryDate(LocalDate.of(2025, 6, 1));
        asset.setAssetValue(1500.0);
        asset.setAssetStatus(AssetStatus.AVAILABLE);
        asset.setCategory(category);
        asset = assetRepo.save(asset);

        // Create asset audit
		auditDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        audit = new AssetAudit();
        audit.setEmployee(employee);
        audit.setAsset(asset);
        audit.setAuditDate(auditDate);
        audit.setStatus(RequestStatus.PENDING);
        audit.setRemarks("Physical condition check");
        assetAuditRepo.save(audit);
    }

    @Test
    void testExistsByEmployeeAndAssetAndStatus() {
        boolean exists = assetAuditRepo.existsByEmployeeAndAssetAndStatus(
                employee, asset, RequestStatus.PENDING
        );
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByEmployeeId() {
        List<AssetAudit> audits = assetAuditRepo.findByEmployeeId(employee.getId());
        assertThat(audits).isNotEmpty();
        assertThat(audits.get(0).getRemarks()).contains("condition");
    }

    @Test
    void testExistsByEmployeeIdAndAssetIdAndAuditDate() {
        boolean exists = assetAuditRepo.existsByEmployeeIdAndAssetIdAndAuditDate(
                employee.getId(), asset.getId(), auditDate
        );
        assertThat(exists).isTrue();
    }
}
