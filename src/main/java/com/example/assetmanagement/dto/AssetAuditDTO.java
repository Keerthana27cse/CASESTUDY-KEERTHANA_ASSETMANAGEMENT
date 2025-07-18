package com.example.assetmanagement.dto;

import com.example.assetmanagement.enums.RequestStatus;

import java.time.LocalDateTime;

public class AssetAuditDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;    // add this
    private Long assetId;
    private String assetName;       // add this
    private String remarks;
    private RequestStatus status;
    private LocalDateTime auditDate;
    private String categoryName;  // in DTO
    public AssetAuditDTO() {
    }
    public AssetAuditDTO(Long id, Long employeeId, String employeeName, Long assetId, String assetName, String remarks,
            RequestStatus status, LocalDateTime auditDate, String categoryName) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.assetId = assetId;
        this.assetName = assetName;
        this.remarks = remarks;
        this.status = status;
        this.auditDate = auditDate;
        this.categoryName = categoryName;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public Long getAssetId() {
        return assetId;
    }
    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }
    public String getAssetName() {
        return assetName;
    }
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public RequestStatus getStatus() {
        return status;
    }
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
    public LocalDateTime getAuditDate() {
        return auditDate;
    }
    public void setAuditDate(LocalDateTime auditDate) {
        this.auditDate = auditDate;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
   
}
