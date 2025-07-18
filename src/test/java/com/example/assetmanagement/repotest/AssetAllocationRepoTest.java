package com.example.assetmanagement.repotest;

import com.example.assetmanagement.entity.*;
import com.example.assetmanagement.enums.*;
import com.example.assetmanagement.repository.AssetAllocationRepo;
import com.example.assetmanagement.repository.AssetCategoryRepo;
import com.example.assetmanagement.repository.AssetRepo;
import com.example.assetmanagement.repository.EmployeeRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AssetAllocationRepoTest {

    @Autowired
    private AssetAllocationRepo assetAllocationRepo;

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private AssetCategoryRepo categoryRepo;

    private Employee employee;
    private Asset asset;
    private AssetAllocation allocation;

    @BeforeEach
    void setUp() {
        AssetCategory category = new AssetCategory("Electronics");
        category = categoryRepo.save(category);

        employee = new Employee();
        employee.setUsername("keerthana123");
        employee.setName("Keerthana");
        employee.setEmail("keerthana@example.com");
        employee.setContactNumber("9876543210");
        employee.setAddress("Chennai");
        employee.setPassword("pass1234");
        employee.setGender(Gender.FEMALE);
        employee.setRole(UserRole.EMPLOYEE);
        employee.setEmpstatus(EmployeeStatus.ACTIVE);
        employee = employeeRepo.save(employee);

        asset = new Asset();
        asset.setAssetNo("AST002");
        asset.setAssetName("Monitor");
        asset.setAssetModel("Dell 24");
        asset.setDescription("Full HD monitor");
        asset.setManufacturingDate(LocalDate.of(2022, 1, 1));
        asset.setExpiryDate(LocalDate.of(2026, 1, 1));
        asset.setAssetStatus(AssetStatus.AVAILABLE);
        asset.setAssetValue(12000.0);
        asset.setCategory(category);
        asset = assetRepo.save(asset);

        allocation = new AssetAllocation();
        allocation.setAsset(asset);
        allocation.setEmployee(employee);
        allocation.setAllocationStatus(AllocationStatus.ALLOCATED);
        allocation.setAllocationDate(LocalDateTime.now().minusDays(1));
        allocation.setReturnDate(null);
        assetAllocationRepo.save(allocation);
    }

    @Test
    void testFindAllByOrderByAllocationDateDesc() {
        List<AssetAllocation> result = assetAllocationRepo.findAllByOrderByAllocationDateDesc();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getAllocationDate()).isBefore(LocalDateTime.now().plusMinutes(1));
    }

    @Test
    void testFindByEmployee() {
        List<AssetAllocation> result = assetAllocationRepo.findByEmployee(employee);
        assertThat(result).hasSize(1);
    }

    @Test
    void testFindByAllocationStatus() {
        List<AssetAllocation> result = assetAllocationRepo.findByAllocationStatus(AllocationStatus.ALLOCATED);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getAsset().getAssetName()).isEqualTo("Monitor");
    }

    @Test
    void testFindByAssetAndEmployeeAndAllocationStatus() {
        AssetAllocation result = assetAllocationRepo.findByAssetAndEmployeeAndAllocationStatus(
                asset, employee, AllocationStatus.ALLOCATED
        );
        assertThat(result).isNotNull();
    }

    @Test
    void testFindByEmployeeId() {
        List<AssetAllocation> result = assetAllocationRepo.findByEmployeeId(employee.getId());
        assertThat(result).hasSize(1);
    }

    @Test
    void testExistsByAssetAndAllocationStatus() {
        boolean exists = assetAllocationRepo.existsByAssetAndAllocationStatus(asset, AllocationStatus.ALLOCATED);
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByAssetAndAllocationStatus() {
        List<AssetAllocation> result = assetAllocationRepo.findByAssetAndAllocationStatus(asset, AllocationStatus.ALLOCATED);
        assertThat(result).hasSize(1);
    }

    @Test
    void testFindByAssetAndEmployee() {
        AssetAllocation result = assetAllocationRepo.findByAssetAndEmployee(asset, employee);
        assertThat(result).isNotNull();
        assertThat(result.getAllocationStatus()).isEqualTo(AllocationStatus.ALLOCATED);
    }
}
