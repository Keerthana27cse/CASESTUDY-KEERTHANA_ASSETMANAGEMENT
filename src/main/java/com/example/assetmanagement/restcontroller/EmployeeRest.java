package com.example.assetmanagement.restcontroller;

import com.example.assetmanagement.dto.EmployeeDTO;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.mapper.EmployeeMapper;
import com.example.assetmanagement.service.EmployeeService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRest {

    @Autowired
    private EmployeeService employeeService;

    // Admin: View all employees
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> dtoList = employeeService.getAllEmployees()
                .stream()
                .map(EmployeeMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // Admin or Employee: View own profile or employee details
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        Employee emp = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(EmployeeMapper.toDTO(emp));
    }

    // Admin or Employee: Update employee details
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping("/update")
    public ResponseEntity<String> updateEmployee(@Valid @RequestBody EmployeeDTO dto) {
        String msg = employeeService.updateEmployee(EmployeeMapper.toEntity(dto));
        return ResponseEntity.ok(msg);
    }

    // Admin only: Delete employee
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        String msg = employeeService.deleteEmployee(id);
        return ResponseEntity.ok(msg);
    }
}
