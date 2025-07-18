package com.example.assetmanagement.mapper;

import com.example.assetmanagement.dto.EmployeeDTO;
import com.example.assetmanagement.entity.Employee;

public class EmployeeMapper {

    public static EmployeeDTO toDTO(Employee emp) {
        if (emp == null) return null;

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(emp.getId());  // use Long directly
        dto.setUsername(emp.getUsername());
        dto.setName(emp.getName());
        dto.setEmail(emp.getEmail());
        dto.setContactNumber(emp.getContactNumber());
        dto.setAddress(emp.getAddress());
        dto.setGender(emp.getGender());
        dto.setStatus(emp.getEmpstatus());
        dto.setCreatedAt(emp.getCreatedAt());
        return dto;
    }

    public static Employee toEntity(EmployeeDTO dto) {
        if (dto == null) return null;

        Employee emp = new Employee();
        emp.setId(dto.getId());  // use Long directly
        emp.setUsername(dto.getUsername());
        emp.setName(dto.getName());
        emp.setEmail(dto.getEmail());
        emp.setContactNumber(dto.getContactNumber());
        emp.setAddress(dto.getAddress());
        emp.setGender(dto.getGender());
        emp.setEmpstatus(dto.getStatus());
        emp.setCreatedAt(dto.getCreatedAt());
        return emp;
    }
}
