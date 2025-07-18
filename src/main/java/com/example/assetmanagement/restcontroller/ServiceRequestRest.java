package com.example.assetmanagement.restcontroller;

import com.example.assetmanagement.dto.ServiceRequestDTO;
import com.example.assetmanagement.entity.ServiceRequest;
import com.example.assetmanagement.enums.RequestStatus;
import com.example.assetmanagement.mapper.ServiceRequestMapper;
import com.example.assetmanagement.service.ServiceRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/service-requests")
public class ServiceRequestRest {

    @Autowired
    private ServiceRequestService serviceRequestService;

    // EMPLOYEE: Submit a new service request
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> submitRequest(@RequestBody ServiceRequestDTO dto) {
        String result = serviceRequestService.submitServiceRequest(dto);
        return ResponseEntity.ok(Map.of("status", "success", "message", result));
    }

     

    // EMPLOYEE: View own requests
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/my-requests")
    public ResponseEntity<List<ServiceRequestDTO>> getOwnRequests() {
        List<ServiceRequestDTO> list = serviceRequestService.getRequestsByLoggedInEmployee()
                .stream()
                .map(ServiceRequestMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // ADMIN: View all service requests
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<ServiceRequestDTO>> getAllRequests() {
        List<ServiceRequestDTO> list = serviceRequestService.getAllServiceRequests()
                .stream()
                .map(ServiceRequestMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // ADMIN: Approve a request
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/approve") 
    public ResponseEntity<ServiceRequestDTO> approve(@PathVariable Long id) {
        ServiceRequest updated = serviceRequestService.approveServiceRequest(id);
        return ResponseEntity.ok(ServiceRequestMapper.toDTO(updated));
    }

    // ADMIN: Reject a request
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<ServiceRequestDTO> reject(@PathVariable Long id) {
        ServiceRequest updated = serviceRequestService.rejectServiceRequest(id);
        return ResponseEntity.ok(ServiceRequestMapper.toDTO(updated));
    }

    // ADMIN: Update status manually
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<ServiceRequestDTO> updateStatus(@PathVariable Long id,
                                                          @RequestParam RequestStatus status) {
        ServiceRequest updated = serviceRequestService.updateRequestStatus(id, status);
        return ResponseEntity.ok(ServiceRequestMapper.toDTO(updated));
    }
}
