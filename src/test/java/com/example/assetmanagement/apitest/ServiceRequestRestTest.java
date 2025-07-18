package com.example.assetmanagement.apitest;

import com.example.assetmanagement.dto.ServiceRequestDTO;
import com.example.assetmanagement.entity.ServiceRequest;
import com.example.assetmanagement.enums.RequestStatus;
import com.example.assetmanagement.restcontroller.ServiceRequestRest;
import com.example.assetmanagement.service.ServiceRequestService;
import com.example.assetmanagement.mapper.ServiceRequestMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceRequestRestTest {

    @InjectMocks
    private ServiceRequestRest controller;

    @Mock
    private ServiceRequestService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitServiceRequest() {
        ServiceRequestDTO dto = new ServiceRequestDTO();
        when(service.submitServiceRequest(dto)).thenReturn("Request submitted");

        ResponseEntity<?> response = controller.submitRequest(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Request submitted", ((java.util.Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void testGetOwnServiceRequests() {
        ServiceRequest request = new ServiceRequest();
        request.setId(1L);

        ServiceRequestDTO dto = new ServiceRequestDTO();
        dto.setId(1L);

        when(service.getRequestsByLoggedInEmployee()).thenReturn(List.of(request));

        try (MockedStatic<ServiceRequestMapper> mockedMapper = mockStatic(ServiceRequestMapper.class)) {
            mockedMapper.when(() -> ServiceRequestMapper.toDTO(request)).thenReturn(dto);

            ResponseEntity<List<ServiceRequestDTO>> response = controller.getOwnRequests();

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(1, response.getBody().size());
            assertEquals(1L, response.getBody().get(0).getId());
        }
    }

    @Test
    void testGetAllServiceRequests() {
        ServiceRequest request = new ServiceRequest();
        request.setId(2L);

        ServiceRequestDTO dto = new ServiceRequestDTO();
        dto.setId(2L);

        when(service.getAllServiceRequests()).thenReturn(List.of(request));

        try (MockedStatic<ServiceRequestMapper> mockedMapper = mockStatic(ServiceRequestMapper.class)) {
            mockedMapper.when(() -> ServiceRequestMapper.toDTO(request)).thenReturn(dto);

            ResponseEntity<List<ServiceRequestDTO>> response = controller.getAllRequests();

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(1, response.getBody().size());
            assertEquals(2L, response.getBody().get(0).getId());
        }
    }

    @Test
    void testApproveServiceRequest() {
        ServiceRequest entity = new ServiceRequest();
        entity.setId(3L);

        ServiceRequestDTO dto = new ServiceRequestDTO();
        dto.setId(3L);

        when(service.approveServiceRequest(3L)).thenReturn(entity);

        try (MockedStatic<ServiceRequestMapper> mockedMapper = mockStatic(ServiceRequestMapper.class)) {
            mockedMapper.when(() -> ServiceRequestMapper.toDTO(entity)).thenReturn(dto);

            ResponseEntity<ServiceRequestDTO> response = controller.approve(3L);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(3L, response.getBody().getId());
        }
    }

    @Test
    void testRejectServiceRequest() {
        ServiceRequest entity = new ServiceRequest();
        entity.setId(4L);

        ServiceRequestDTO dto = new ServiceRequestDTO();
        dto.setId(4L);

        when(service.rejectServiceRequest(4L)).thenReturn(entity);

        try (MockedStatic<ServiceRequestMapper> mockedMapper = mockStatic(ServiceRequestMapper.class)) {
            mockedMapper.when(() -> ServiceRequestMapper.toDTO(entity)).thenReturn(dto);

            ResponseEntity<ServiceRequestDTO> response = controller.reject(4L);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(4L, response.getBody().getId());
        }
    }

    @Test
    void testUpdateStatus() {
        ServiceRequest entity = new ServiceRequest();
        entity.setId(5L);
        entity.setStatus(RequestStatus.APPROVED);

        ServiceRequestDTO dto = new ServiceRequestDTO();
        dto.setId(5L);
        dto.setStatus(RequestStatus.APPROVED);

        when(service.updateRequestStatus(5L, RequestStatus.APPROVED)).thenReturn(entity);

        try (MockedStatic<ServiceRequestMapper> mockedMapper = mockStatic(ServiceRequestMapper.class)) {
            mockedMapper.when(() -> ServiceRequestMapper.toDTO(entity)).thenReturn(dto);

            ResponseEntity<ServiceRequestDTO> response = controller.updateStatus(5L, RequestStatus.APPROVED);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(RequestStatus.APPROVED, response.getBody().getStatus());
        }
    }
}
