package com.example.assetmanagement.repotest;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.entity.AssetCategory;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.entity.ServiceRequest;
import com.example.assetmanagement.enums.AssetStatus;
import com.example.assetmanagement.enums.EmployeeStatus;
import com.example.assetmanagement.enums.Gender;
import com.example.assetmanagement.enums.IssueType;
import com.example.assetmanagement.enums.RequestStatus;
import com.example.assetmanagement.enums.UserRole;
import com.example.assetmanagement.repository.AssetCategoryRepo;
import com.example.assetmanagement.repository.AssetRepo;
import com.example.assetmanagement.repository.EmployeeRepo;
import com.example.assetmanagement.repository.ServiceRequestRepo;

@DataJpaTest
public class ServiceRequestRepoTest {

    @Autowired
    private ServiceRequestRepo serviceRequestRepo;

    @Autowired
    private EmployeeRepo employeeRepository;

    @Autowired
    private AssetRepo assetRepository;

    @Autowired
    private AssetCategoryRepo categoryRepository;

    private Employee employee;
    private Asset asset;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setUsername("keerthana123");
        employee.setName("Keerthana R");
        employee.setEmail("keerthana@example.com");
        employee.setContactNumber("9876543210");
        employee.setAddress("Chennai");
        employee.setPassword("securepass");
        employee.setGender(Gender.FEMALE);
        employee.setRole(UserRole.EMPLOYEE);
        employee.setEmpstatus(EmployeeStatus.ACTIVE);
        employee = employeeRepository.save(employee);

        AssetCategory category = new AssetCategory();
        category.setCategoryName("Electronics");
        category = categoryRepository.save(category);

        asset = new Asset();
        asset.setAssetNo("ASSET101");
        asset.setAssetName("Monitor");
        asset.setAssetModel("Dell P2419H");
        asset.setDescription("24-inch full HD monitor");
        asset.setImageUrl("http://example.com/image.png");
        asset.setManufacturingDate(LocalDate.of(2023, 1, 1));
        asset.setExpiryDate(LocalDate.of(2026, 1, 1));
        asset.setAssetValue(12000.0);
        asset.setAssetStatus(AssetStatus.AVAILABLE);
        asset.setCategory(category);
        asset = assetRepository.save(asset);
    }

    @Test
    void testFindByEmployeeId_ShouldReturnServiceRequest() {
        ServiceRequest request = new ServiceRequest();
        request.setEmployee(employee);
        request.setAsset(asset);
        request.setIssueType(IssueType.HARDWARE);
        request.setDescription("Monitor screen flickers");
        request.setStatus(RequestStatus.PENDING);
        request.setRequestDate(LocalDateTime.now());

        serviceRequestRepo.save(request);

        List<ServiceRequest> result = serviceRequestRepo.findByEmployeeId(employee.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getDescription()).contains("flickers");
        assertThat(result.get(0).getEmployee().getUsername()).isEqualTo("keerthana123");
    }
}
