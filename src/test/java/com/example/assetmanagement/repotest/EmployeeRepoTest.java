package com.example.assetmanagement.repotest;

import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.enums.EmployeeStatus;
import com.example.assetmanagement.enums.Gender;
import com.example.assetmanagement.enums.UserRole;
import com.example.assetmanagement.repository.EmployeeRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepoTest {

    @Autowired
    private EmployeeRepo employeeRepo;

    private Employee employee;

    @BeforeEach
    void setUp() {
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

        employeeRepo.save(employee);
    }

    @Test
    void testExistsByUsername() {
        boolean exists = employeeRepo.existsByUsername("keerthana123");
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByEmail() {
        Optional<Employee> result = employeeRepo.findByEmail("keerthana@example.com");
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("keerthana123");
    }

    @Test
    void testFindByUsername() {
        Optional<Employee> result = employeeRepo.findByUsername("keerthana123");
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("keerthana@example.com");
    }
}
