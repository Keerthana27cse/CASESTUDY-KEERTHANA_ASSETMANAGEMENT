package com.example.assetmanagement.repotest;

import com.example.assetmanagement.entity.*;
import com.example.assetmanagement.enums.*;
import com.example.assetmanagement.repository.AssetCategoryRepo;
import com.example.assetmanagement.repository.AssetRepo;
import com.example.assetmanagement.repository.EmployeeRepo;
import com.example.assetmanagement.repository.NewAssetRequestRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class NewAssetRequestRepoTest {

    @Autowired
    private NewAssetRequestRepo newAssetRequestRepo;

    @Autowired
    private EmployeeRepo employeeRepository;

    @Autowired
    private AssetRepo assetRepository;

    @Autowired
    private AssetCategoryRepo categoryRepository;

    private Employee employee;
    private Asset asset;

    private AssetCategory category;

    @BeforeEach
    void setUp() {
        // Save Employee
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
        employee = employeeRepository.save(employee);

        // Save Category
        category = new AssetCategory("Electronics");
        category = categoryRepository.save(category);

        // Save Asset
        asset = new Asset();
        asset.setAssetNo("AST101");
        asset.setAssetName("Laptop");
        asset.setAssetModel("Dell Inspiron");
        asset.setDescription("14-inch i5 laptop");
        asset.setImageUrl("http://example.com/image.jpg");
        asset.setManufacturingDate(LocalDate.of(2023, 1, 1));
        asset.setExpiryDate(LocalDate.of(2026, 1, 1));
        asset.setAssetValue(50000.0);
        asset.setAssetStatus(AssetStatus.AVAILABLE);
        asset.setCategory(category);
        asset = assetRepository.save(asset);
    }

    @Test
    void testFindByEmployeeId() {
        NewAssetRequest request = new NewAssetRequest();
        request.setEmployee(employee);
        request.setRequestedCategory(category);
        request.setRequestReason("Need a laptop for remote work");
        request.setFullAddress("123 Anna Nagar");
        request.setZipCode("600001");
        request.setPhone("9876543210");
        request.setStatus(RequestStatus.PENDING);
        request.setRequestDate(LocalDateTime.now());
        newAssetRequestRepo.save(request);

        List<NewAssetRequest> result = newAssetRequestRepo.findByEmployeeId(employee.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRequestReason()).contains("remote work");
        assertThat(result.get(0).getRequestedCategory().getCategoryName()).isEqualTo("Electronics");
    }

    @Test
    void testCountAllocatedAssets() {
        NewAssetRequest request = new NewAssetRequest();
        request.setEmployee(employee);
        request.setRequestedCategory(category);
        request.setRequestReason("Approved request");
        request.setFullAddress("456 Velachery");
        request.setZipCode("600042");
        request.setPhone("9123456789");
        request.setStatus(RequestStatus.APPROVED);
        request.setAsset(asset); // allocated asset
        request.setRequestDate(LocalDateTime.now());
        newAssetRequestRepo.save(request);

        long count = newAssetRequestRepo.countAllocatedAssets();
        assertThat(count).isEqualTo(1);
    }

    @Test
    void testFindAllocatedAssets() {
        NewAssetRequest request = new NewAssetRequest();
        request.setEmployee(employee);
        request.setRequestedCategory(category);
        request.setRequestReason("For office work");
        request.setFullAddress("7th Cross Road");
        request.setZipCode("600018");
        request.setPhone("9000000000");
        request.setStatus(RequestStatus.APPROVED);
        request.setAsset(asset); // assigned
        request.setRequestDate(LocalDateTime.now());
        newAssetRequestRepo.save(request);

        List<NewAssetRequest> allocated = newAssetRequestRepo.findAllocatedAssets();

        assertThat(allocated).hasSize(1);
        assertThat(allocated.get(0).getStatus()).isEqualTo(RequestStatus.APPROVED);
        assertThat(allocated.get(0).getAsset().getAssetName()).isEqualTo("Laptop");
    }
}