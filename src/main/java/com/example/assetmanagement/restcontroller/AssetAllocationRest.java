package com.example.assetmanagement.restcontroller;

import com.example.assetmanagement.dto.AssetAllocationDTO;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.mapper.AssetAllocationMapper;
import com.example.assetmanagement.service.AssetAllocationService;
import com.example.assetmanagement.enums.AllocationStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/allocations")
public class AssetAllocationRest {

    @Autowired
    private AssetAllocationService allocationService;

    // ------------------- ADMIN: Get All Allocations -------------------
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AssetAllocationDTO>> getAllAllocations() {
        List<AssetAllocationDTO> allocations = allocationService.getAllAllocations()
                .stream()
                .map(AssetAllocationMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(allocations);
    }

    // ------------------- ADMIN: Filter by Status -------------------
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AssetAllocationDTO>> getByStatus(@PathVariable AllocationStatus status) {
        List<AssetAllocationDTO> allocations = allocationService.getAllocationsByStatus(status)
                .stream()
                .map(AssetAllocationMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(allocations);
    }

    // ------------------- ADMIN: Allocation Count Per Employee -------------------
    @GetMapping("/counts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getCountPerEmployee() {
        Map<Employee, Long> rawCounts = allocationService.getAllocationCountPerEmployee();

        // Convert Employee object keys to readable strings (e.g., employee name or email)
        Map<String, Long> counts = rawCounts.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getName() + " (" + entry.getKey().getEmail() + ")",
                        Map.Entry::getValue
                ));

        return ResponseEntity.ok(counts);
    }
}
