package com.example.assetmanagement.apitest;

import com.example.assetmanagement.dto.EmployeeDTO;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.mapper.EmployeeMapper;
import com.example.assetmanagement.restcontroller.EmployeeRest;
import com.example.assetmanagement.service.EmployeeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeRestTest {

    @InjectMocks
    private EmployeeRest controller;

    @Mock
    private EmployeeService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        Employee emp = new Employee();
        emp.setId(1L);

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(1L);  // Fixed: String instead of Long

        when(service.getAllEmployees()).thenReturn(List.of(emp));

        try (MockedStatic<EmployeeMapper> mocked = mockStatic(EmployeeMapper.class)) {
            mocked.when(() -> EmployeeMapper.toDTO(emp)).thenReturn(dto);

            ResponseEntity<List<EmployeeDTO>> response = controller.getAllEmployees();

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(1, response.getBody().size());
            assertEquals("1", response.getBody().get(0).getId());
        }
    }

    @Test
    void testGetEmployeeById() {
        Employee emp = new Employee();
        emp.setId(2L);

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(2L); // Fixed: String

        when(service.getEmployeeById(2L)).thenReturn(emp);

        try (MockedStatic<EmployeeMapper> mocked = mockStatic(EmployeeMapper.class)) {
            mocked.when(() -> EmployeeMapper.toDTO(emp)).thenReturn(dto);

            ResponseEntity<EmployeeDTO> response = controller.getEmployeeById(2L);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals("2", response.getBody().getId());
        }
    }

    @Test
    void testUpdateEmployee() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(3L); // Fixed: String

        Employee emp = new Employee();
        emp.setId(3L);

        when(service.updateEmployee(emp)).thenReturn("Updated");

        try (MockedStatic<EmployeeMapper> mocked = mockStatic(EmployeeMapper.class)) {
            mocked.when(() -> EmployeeMapper.toEntity(dto)).thenReturn(emp);

            ResponseEntity<String> response = controller.updateEmployee(dto);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals("Updated", response.getBody());
        }
    }

    @Test
    void testDeleteEmployee() {
        when(service.deleteEmployee(4L)).thenReturn("Deleted");

        ResponseEntity<String> response = controller.deleteEmployee(4L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Deleted", response.getBody());
    }
}
