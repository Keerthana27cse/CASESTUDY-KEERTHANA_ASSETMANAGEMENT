package com.example.assetmanagement.restcontroller;

import com.example.assetmanagement.dto.NewAssetRequestDTO;
import com.example.assetmanagement.entity.NewAssetRequest;
import com.example.assetmanagement.mapper.NewAssetRequestMapper;
import com.example.assetmanagement.service.NewAssetRequestService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/asset-requests")
public class NewAssetRequestRest {


    @Autowired
    private NewAssetRequestService assetRequestService;

    // ------------------ EMPLOYEE ENDPOINTS ------------------

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/submit")
    public ResponseEntity<?> submitNewAssetRequest(@Valid @RequestBody NewAssetRequestDTO dto) {
        try {
            NewAssetRequest savedRequest = assetRequestService.createNewAssetRequest(dto);
            NewAssetRequestDTO responseDto = NewAssetRequestMapper.toDTO(savedRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Log for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<NewAssetRequestDTO>> getMyAssetRequests() {
        try {
            List<NewAssetRequest> requests = assetRequestService.getRequestsByLoggedInEmployee();
            List<NewAssetRequestDTO> responseList = requests.stream()
                    .map(NewAssetRequestMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseList);
        } catch (IllegalStateException | IllegalArgumentException ex) {
            // Unauthorized or Employee not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    // ------------------ ADMIN ENDPOINTS ------------------

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NewAssetRequestDTO>> getAllAssetRequests() {
        List<NewAssetRequest> allRequests = assetRequestService.getAllRequests();
        List<NewAssetRequestDTO> responseList = allRequests.stream()
                .map(NewAssetRequestMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> approveAssetRequest(@PathVariable Long id) {
        String result = assetRequestService.approveRequest(id);

        if (result.contains("successfully")) {
            return ResponseEntity.ok(result);
        } else if (result.contains("already allocated")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result); // 409 Conflict
        } else if (result.contains("not found") || result.contains("processed")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result); // 404 Not Found
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result); // fallback
        }
    }


    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> rejectAssetRequest(@PathVariable Long id) {
        String result = assetRequestService.rejectRequest(id);
        if (result.contains("successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
    
    @PutMapping("/ship/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> shipAssetRequest(@PathVariable Long id) {
        String result = assetRequestService.markRequestAsShipped(id);
        if (result.contains("successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
    @PutMapping("/{id}/in-use")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> markAssetInUse(@PathVariable Long id) {
        String result = assetRequestService.markAssetAsInUse(id);
        if (result.contains("IN_USE")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
    
    @PutMapping("/{id}/return")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> submitReturnRequest(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean damaged) {
        
        String result = assetRequestService.submitReturnRequest(id, damaged);

        if (result.contains("submitted")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PutMapping("/approve-return/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> approveReturnRequest(@PathVariable Long id) {
        String result = assetRequestService.markAsReturned(id);
        
        if (result.contains("RETURNED")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PutMapping("/complete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> completeRequest(@PathVariable Long id) {
        String result = assetRequestService.completeRequest(id);

        if (result.contains("COMPLETED")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }


    // ------------------ Utility Method ------------------

  
}
