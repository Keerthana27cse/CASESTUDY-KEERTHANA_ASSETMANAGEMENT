package com.example.assetmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.entity.AssetAllocation;
import com.example.assetmanagement.entity.AssetAudit;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.enums.AllocationStatus;
import com.example.assetmanagement.enums.RequestStatus;
import com.example.assetmanagement.repository.AssetAllocationRepo;
import com.example.assetmanagement.repository.AssetAuditRepo;
import com.example.assetmanagement.repository.EmployeeRepo;

@Service
public class AssetAuditService {

    @Autowired
    private AssetAuditRepo auditRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private AssetAllocationRepo allocationRepo;

    public void sendAuditRequestToAllEmployees() {
        List<Employee> employees = employeeRepo.findAll();

        for (Employee emp : employees) {
            List<AssetAllocation> allocations = allocationRepo.findByEmployeeId(emp.getId());

            for (AssetAllocation allocation : allocations) {
                if (allocation.getAllocationStatus() == AllocationStatus.ALLOCATED) {
                    Asset asset = allocation.getAsset();

                    boolean auditExists = auditRepo.existsByEmployeeIdAndAssetIdAndAuditDate(
                            emp.getId(), asset.getId(), LocalDateTime.now());

                    if (!auditExists) {
                        AssetAudit audit = new AssetAudit();
                        audit.setEmployee(emp);
                        audit.setAsset(asset);
                        audit.setStatus(RequestStatus.PENDING);
                        audit.setAuditDate(LocalDateTime.now());
                        auditRepo.save(audit);
                    }
                }
            }
        }
    }

    
    public List<AssetAudit> getAllAuditRequests() {
        return auditRepo.findAll();
    }

   
    public String updateAuditStatus(Long auditId, RequestStatus status) {
        AssetAudit audit = auditRepo.findById(auditId).orElse(null);
        if (audit != null) {
            audit.setStatus(status);
            auditRepo.save(audit);
            return "Audit status updated.";
        }
        return "Audit request not found.";
    }

   
    public List<AssetAudit> getAuditRequestsByEmployee(Employee employee) {
        return auditRepo.findByEmployeeId(employee.getId());
    }

   
    public String submitAuditRemarks(Long auditId, String remarks) {
        AssetAudit audit = auditRepo.findById(auditId).orElse(null);
        if (audit != null && audit.getStatus() == RequestStatus.PENDING) {
            audit.setRemarks(remarks);
            audit.setStatus(RequestStatus.COMPLETED);
            auditRepo.save(audit);
            return "Audit remarks submitted successfully.";
        }
        return "Audit request not found or already processed.";
    }
}
