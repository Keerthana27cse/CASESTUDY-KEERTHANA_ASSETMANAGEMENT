package com.example.assetmanagement.apitest;

import com.example.assetmanagement.dto.AssetAllocationDTO;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.enums.AllocationStatus;
import com.example.assetmanagement.mapper.AssetAllocationMapper;
import com.example.assetmanagement.restcontroller.AssetAllocationRest;
import com.example.assetmanagement.service.AssetAllocationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetAllocationRestTest {

    @InjectMocks
    private AssetAllocationRest allocationRest;

    @Mock
    private AssetAllocationService allocationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAllocations() {
        AssetAllocationDTO dto = new AssetAllocationDTO();
        // set properties if needed

        when(allocationService.getAllAllocations()).thenReturn(Collections.emptyList());

        try (MockedStatic<AssetAllocationMapper> mapperMock = mockStatic(AssetAllocationMapper.class)) {
            mapperMock.when(() -> AssetAllocationMapper.toDTO(any())).thenReturn(dto);

            ResponseEntity<List<AssetAllocationDTO>> response = allocationRest.getAllAllocations();

            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
        }
    }

    @Test
    void testGetByStatus() {
        AllocationStatus status = AllocationStatus.ALLOCATED;

        when(allocationService.getAllocationsByStatus(status)).thenReturn(Collections.emptyList());

        try (MockedStatic<AssetAllocationMapper> mapperMock = mockStatic(AssetAllocationMapper.class)) {
            mapperMock.when(() -> AssetAllocationMapper.toDTO(any())).thenReturn(new AssetAllocationDTO());

            ResponseEntity<List<AssetAllocationDTO>> response = allocationRest.getByStatus(status);

            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
        }
    }

    @Test
    void testGetCountPerEmployee() {
        Employee emp = new Employee();
        emp.setName("John Doe");
        emp.setEmail("john@example.com");
        Map<Employee, Long> map = Map.of(emp, 3L);
        when(allocationService.getAllocationCountPerEmployee()).thenReturn(map);
        ResponseEntity<Map<String, Long>> response = allocationRest.getCountPerEmployee();
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Long> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("John Doe (john@example.com)"));
        assertEquals(3L, body.get("John Doe (john@example.com)"));
    }
}
