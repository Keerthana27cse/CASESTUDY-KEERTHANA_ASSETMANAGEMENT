package com.example.assetmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.assetmanagement.dto.NewAssetRequestDTO;
import com.example.assetmanagement.entity.*;
import com.example.assetmanagement.enums.AllocationStatus;
import com.example.assetmanagement.enums.RequestStatus;
import com.example.assetmanagement.repository.*;

@Service
public class NewAssetRequestService {

    @Autowired private NewAssetRequestRepo requestRepo;
    @Autowired private AssetRepo assetRepo;
    @Autowired private AssetCategoryRepo categoryRepo;
    @Autowired private AssetAllocationRepo assetAllocationRepo;
    @Autowired private EmployeeRepo employeeRepo;

    //----------------------- EMPLOYEE METHODS ----------------------------

    public NewAssetRequest createNewAssetRequest(NewAssetRequestDTO dto) {
        String email = getLoggedInUserEmail();
        Employee employee = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Employee not found with email: " + email));

        if (dto.getCategoryId() == null) {
            throw new IllegalArgumentException("Category ID must not be null");
        }

        AssetCategory category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + dto.getCategoryId()));

        Asset asset = null;
        if (dto.getAssetId() != null) {
            asset = assetRepo.findById(dto.getAssetId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid asset ID: " + dto.getAssetId()));
        }

        NewAssetRequest request = new NewAssetRequest();
        request.setEmployee(employee);
        request.setRequestedCategory(category);
        request.setAsset(asset); // asset may be null
        request.setDescription(dto.getDescription());
        request.setRequestReason(dto.getRequestReason());
        request.setStatus(RequestStatus.PENDING);
        request.setRequestDate(LocalDateTime.now());
        request.setFullAddress(dto.getFullAddress());
        request.setZipCode(dto.getZipCode());
        request.setPhone(dto.getPhone());

        return requestRepo.save(request);
    }

    public List<NewAssetRequest> getRequestsByLoggedInEmployee() {
        String email = getLoggedInUserEmail();
        Employee emp = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        return requestRepo.findByEmployeeId(emp.getId());
    }

    public String markAssetAsInUse(Long requestId) {
        NewAssetRequest request = requestRepo.findById(requestId).orElse(null);
        if (request == null) return "Request not found.";
        if (request.getStatus() != RequestStatus.SHIPPED)
            return "Only shipped assets can be marked as IN_USE.";

        request.setStatus(RequestStatus.IN_USE);
        requestRepo.save(request);

        Asset asset = request.getAsset();
        Employee employee = request.getEmployee();
        AssetAllocation allocation = assetAllocationRepo.findByAssetAndEmployee(asset, employee);

        if (allocation != null) {
            allocation.setAllocationStatus(AllocationStatus.IN_USE);
            assetAllocationRepo.save(allocation);
        }

        return "Asset marked as IN_USE.";
    }

    public String submitReturnRequest(Long requestId, boolean isDamaged) {
        NewAssetRequest request = requestRepo.findById(requestId).orElse(null);
        if (request == null) return "Request not found.";
        if (request.getStatus() != RequestStatus.IN_USE)
            return "Only assets in use can be returned.";

        request.setStatus(isDamaged ? RequestStatus.DAMAGED : RequestStatus.RETURN_REQUEST);
        requestRepo.save(request);
        return "Return request submitted.";
    }

    public String markAsReturned(Long requestId) {
        NewAssetRequest request = requestRepo.findById(requestId).orElse(null);
        if (request == null) return "Request not found.";
        if (request.getStatus() != RequestStatus.RETURN_REQUEST &&
            request.getStatus() != RequestStatus.DAMAGED) {
            return "Request must be in RETURN_REQUEST or DAMAGED state.";
        }

        request.setStatus(RequestStatus.RETURNED);
        requestRepo.save(request);

        AssetAllocation allocation = assetAllocationRepo.findByAssetAndEmployee(
                request.getAsset(), request.getEmployee());

        if (allocation != null) {
            allocation.setAllocationStatus(AllocationStatus.RETURNED);
            allocation.setReturnDate(LocalDateTime.now());
            assetAllocationRepo.save(allocation);
        }

        return "Asset marked as RETURNED.";
    }

    public String resolveDamage(Long requestId) {
        NewAssetRequest request = requestRepo.findById(requestId).orElse(null);
        if (request == null) return "Request not found.";
        if (request.getStatus() != RequestStatus.DAMAGED)
            return "Asset is not marked as DAMAGED.";

        request.setStatus(RequestStatus.RESOLVED);
        requestRepo.save(request);
        return "Damage resolved.";
    }


    //----------------------- ADMIN METHODS ----------------------------

    public String approveRequest(Long id) {
        NewAssetRequest req = requestRepo.findById(id).orElse(null);
        if (req == null || req.getStatus() != RequestStatus.PENDING) {
            return "Request not found or already processed.";
        }

        Asset asset = req.getAsset();
        boolean isAllocated = assetAllocationRepo.existsByAssetAndAllocationStatus(
                asset, AllocationStatus.ALLOCATED);

        if (isAllocated) {
            return "Asset is already allocated and not yet returned.";
        }

        req.setStatus(RequestStatus.APPROVED);
        requestRepo.save(req);

        AssetAllocation allocation = new AssetAllocation();
        allocation.setAsset(asset);
        allocation.setEmployee(req.getEmployee());
        allocation.setAllocationDate(LocalDateTime.now());
        allocation.setAllocationStatus(AllocationStatus.ALLOCATED);
        assetAllocationRepo.save(allocation);

        return "Request approved successfully and asset allocated.";
    }

    public String rejectRequest(Long id) {
        NewAssetRequest req = requestRepo.findById(id).orElse(null);
        if (req != null && req.getStatus() == RequestStatus.PENDING) {
            req.setStatus(RequestStatus.REJECTED);
            requestRepo.save(req);
            return "Request rejected successfully.";
        }
        return "Request not found or already processed.";
    }

    public String markRequestAsShipped(Long requestId) {
        NewAssetRequest req = requestRepo.findById(requestId).orElse(null);
        if (req == null) return "Request not found.";
        if (req.getStatus() != RequestStatus.APPROVED)
            return "Only APPROVED requests can be marked as SHIPPED.";

        req.setStatus(RequestStatus.SHIPPED);
        requestRepo.save(req);
        return "Request marked as SHIPPED.";
    }

    public List<NewAssetRequest> getAllRequests() {
        return requestRepo.findAll();
    }
    
    public String completeRequest(Long requestId) {
        NewAssetRequest request = requestRepo.findById(requestId).orElse(null);
        if (request == null) {
            return "Request not found.";
        }

        if (request.getStatus() != RequestStatus.RETURNED &&
            request.getStatus() != RequestStatus.RESOLVED) {
            return "Only RETURNED or RESOLVED requests can be completed.";
        }

        request.setStatus(RequestStatus.COMPLETED);
        requestRepo.save(request);

        return "Request marked as COMPLETED.";
    }

    //----------------------- UTILITY ----------------------------

    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        }
        return null;
    }
}
