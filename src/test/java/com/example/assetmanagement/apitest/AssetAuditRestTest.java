package com.example.assetmanagement.apitest;


import com.example.assetmanagement.dto.AssetAuditDTO;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.enums.RequestStatus;
import com.example.assetmanagement.mapper.AssetAuditMapper;
import com.example.assetmanagement.restcontroller.AssetAuditRest;
import com.example.assetmanagement.service.AssetAuditService;
import com.example.assetmanagement.service.EmployeeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetAuditRestTest {

    @InjectMocks
    private AssetAuditRest assetAuditRest;

    @Mock
    private AssetAuditService assetAuditService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendAuditRequestsToAll() {
        doNothing().when(assetAuditService).sendAuditRequestToAllEmployees();
        ResponseEntity<String> response = assetAuditRest.sendAuditRequestsToAll();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Audit requests sent to all allocated employees.", response.getBody());
        verify(assetAuditService).sendAuditRequestToAllEmployees();
    }

    @Test
    void testGetAllAuditRequests() {
        when(assetAuditService.getAllAuditRequests()).thenReturn(Collections.emptyList());
        try (MockedStatic<AssetAuditMapper> mapperMock = mockStatic(AssetAuditMapper.class)) {
            mapperMock.when(() -> AssetAuditMapper.toDTO(any())).thenReturn(new AssetAuditDTO());
            ResponseEntity<List<AssetAuditDTO>> response = assetAuditRest.getAllAuditRequests();
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
        }
    }

    @Test
    void testUpdateAuditStatus() {
        Long auditId = 1L;
        RequestStatus status = RequestStatus.APPROVED;
        when(assetAuditService.updateAuditStatus(auditId, status)).thenReturn("Status updated");
        ResponseEntity<String> response = assetAuditRest.updateAuditStatus(auditId, status);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Status updated", response.getBody());
    }

    @Test
    void testGetMyAuditRequests_Unauthorized() {
        // No authentication principal
        SecurityContextHolder.clearContext();
        ResponseEntity<List<AssetAuditDTO>> response = assetAuditRest.getMyAuditRequests();
        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void testGetMyAuditRequests_EmployeeNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        SecurityContextHolder.setContext(securityContext);
        when(employeeService.getByEmail("user@example.com")).thenReturn(null);
        ResponseEntity<List<AssetAuditDTO>> response = assetAuditRest.getMyAuditRequests();
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetMyAuditRequests_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        SecurityContextHolder.setContext(securityContext);
        Employee emp = new Employee();
        when(employeeService.getByEmail("user@example.com")).thenReturn(emp);
        when(assetAuditService.getAuditRequestsByEmployee(emp)).thenReturn(Collections.emptyList());

        try (MockedStatic<AssetAuditMapper> mapperMock = mockStatic(AssetAuditMapper.class)) {
            mapperMock.when(() -> AssetAuditMapper.toDTO(any())).thenReturn(new AssetAuditDTO());
            ResponseEntity<List<AssetAuditDTO>> response = assetAuditRest.getMyAuditRequests();
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
        }
    }

    @Test
    void testSubmitAuditRemarks() {
        Long auditId = 1L;
        String remarks = "Test remarks";
        when(assetAuditService.submitAuditRemarks(auditId, remarks)).thenReturn("Remarks submitted");
        ResponseEntity<String> response = assetAuditRest.submitAuditRemarks(auditId, remarks);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Remarks submitted", response.getBody());
    }
}
