package com.example.assetmanagement.apitest;

import com.example.assetmanagement.dto.NewAssetRequestDTO;
import com.example.assetmanagement.entity.NewAssetRequest;
import com.example.assetmanagement.mapper.NewAssetRequestMapper;
import com.example.assetmanagement.restcontroller.NewAssetRequestRest;
import com.example.assetmanagement.service.NewAssetRequestService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewAssetRequestRestTest {

    @InjectMocks
    private NewAssetRequestRest controller;

    @Mock
    private NewAssetRequestService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitNewAssetRequest_Success() {
        NewAssetRequestDTO dto = new NewAssetRequestDTO();
        NewAssetRequest entity = new NewAssetRequest();
        entity.setId(1L);
        dto.setId(1L);

        when(service.createNewAssetRequest(dto)).thenReturn(entity);

        try (MockedStatic<NewAssetRequestMapper> mocked = mockStatic(NewAssetRequestMapper.class)) {
            mocked.when(() -> NewAssetRequestMapper.toDTO(entity)).thenReturn(dto);

            ResponseEntity<?> response = controller.submitNewAssetRequest(dto);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(1L, ((NewAssetRequestDTO) response.getBody()).getId());
        }
    }

    // ✅ get employee's own requests
    @Test
    void testGetMyAssetRequests_Success() {
        NewAssetRequest entity = new NewAssetRequest();
        entity.setId(1L);

        NewAssetRequestDTO dto = new NewAssetRequestDTO();
        dto.setId(1L);

        when(service.getRequestsByLoggedInEmployee()).thenReturn(List.of(entity));

        try (MockedStatic<NewAssetRequestMapper> mocked = mockStatic(NewAssetRequestMapper.class)) {
            mocked.when(() -> NewAssetRequestMapper.toDTO(entity)).thenReturn(dto);

            ResponseEntity<List<NewAssetRequestDTO>> response = controller.getMyAssetRequests();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().size());
        }
    }

    // ✅ get all requests (ADMIN)
    @Test
    void testGetAllAssetRequests() {
        NewAssetRequest entity = new NewAssetRequest();
        entity.setId(1L);

        NewAssetRequestDTO dto = new NewAssetRequestDTO();
        dto.setId(1L);

        when(service.getAllRequests()).thenReturn(List.of(entity));

        try (MockedStatic<NewAssetRequestMapper> mocked = mockStatic(NewAssetRequestMapper.class)) {
            mocked.when(() -> NewAssetRequestMapper.toDTO(entity)).thenReturn(dto);

            ResponseEntity<List<NewAssetRequestDTO>> response = controller.getAllAssetRequests();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().size());
        }
    }

    // ✅ approve request
    @Test
    void testApproveAssetRequest_Success() {
        when(service.approveRequest(1L)).thenReturn("Asset approved successfully");

        ResponseEntity<String> response = controller.approveAssetRequest(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("successfully"));
    }

    // ✅ reject request
    @Test
    void testRejectAssetRequest_Success() {
        when(service.rejectRequest(1L)).thenReturn("Rejected successfully");

        ResponseEntity<String> response = controller.rejectAssetRequest(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Rejected successfully", response.getBody());
    }

    // ✅ ship request
    @Test
    void testShipAssetRequest_Success() {
        when(service.markRequestAsShipped(1L)).thenReturn("Shipped successfully");

        ResponseEntity<String> response = controller.shipAssetRequest(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Shipped successfully", response.getBody());
    }

    // ✅ mark as in use
    @Test
    void testMarkAssetInUse_Success() {
        when(service.markAssetAsInUse(1L)).thenReturn("Asset marked as IN_USE");

        ResponseEntity<String> response = controller.markAssetInUse(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("IN_USE"));
    }

    // ✅ submit return request
    @Test
    void testSubmitReturnRequest_Success() {
        when(service.submitReturnRequest(1L, false)).thenReturn("Return submitted");

        ResponseEntity<String> response = controller.submitReturnRequest(1L, false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("submitted"));
    }

    // ✅ approve return request
    @Test
    void testApproveReturnRequest_Success() {
        when(service.markAsReturned(1L)).thenReturn("Status changed to RETURNED");

        ResponseEntity<String> response = controller.approveReturnRequest(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("RETURNED"));
    }

    // ✅ complete request
    @Test
    void testCompleteRequest_Success() {
        when(service.completeRequest(1L)).thenReturn("Request COMPLETED");

        ResponseEntity<String> response = controller.completeRequest(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("COMPLETED"));
    }
}
