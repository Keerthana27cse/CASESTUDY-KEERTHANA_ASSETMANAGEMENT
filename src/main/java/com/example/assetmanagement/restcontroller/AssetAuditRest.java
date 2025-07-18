package com.example.assetmanagement.restcontroller;

import com.example.assetmanagement.dto.AssetAuditDTO;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.enums.RequestStatus;
import com.example.assetmanagement.mapper.AssetAuditMapper;
import com.example.assetmanagement.service.AssetAuditService;
import com.example.assetmanagement.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/asset-audits")
public class AssetAuditRest {

    @Autowired
    private AssetAuditService assetAuditService;

    @Autowired
    private EmployeeService employeeService;

    // ------------------- ADMIN -------------------

    @PostMapping("/trigger")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> sendAuditRequestsToAll() {
        assetAuditService.sendAuditRequestToAllEmployees();
        return ResponseEntity.ok("Audit requests sent to all allocated employees.");
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AssetAuditDTO>> getAllAuditRequests() {
        List<AssetAuditDTO> audits = assetAuditService.getAllAuditRequests()
                .stream()
                .map(AssetAuditMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(audits);
    }

    @PutMapping("/{auditId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateAuditStatus(@PathVariable Long auditId,
                                                    @RequestParam RequestStatus status) {
        String result = assetAuditService.updateAuditStatus(auditId, status);
        return ResponseEntity.ok(result);
    }

    // ------------------- EMPLOYEE -------------------

    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<AssetAuditDTO>> getMyAuditRequests() {
        String email = getLoggedInEmail();
        if (email == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Employee emp = employeeService.getByEmail(email);
        if (emp == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        List<AssetAuditDTO> audits = assetAuditService.getAuditRequestsByEmployee(emp)
                .stream()
                .map(AssetAuditMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(audits);
    }

    @PutMapping("/{auditId}/remarks")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> submitAuditRemarks(@PathVariable Long auditId,
                                                     @RequestParam String remarks) {
        String result = assetAuditService.submitAuditRemarks(auditId, remarks);
        return ResponseEntity.ok(result);
    }

    // ------------------- Utility -------------------

    private String getLoggedInEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) return null;
        return ((UserDetails) auth.getPrincipal()).getUsername();
    }
}
