package com.example.assetmanagement.dto;

import com.example.assetmanagement.enums.AllocationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class AssetAllocationDTO {
    private Long id;
    private Long assetId;
    private Long employeeId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Customize as needed
    private LocalDateTime requestDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Customize as needed
    private LocalDateTime allocationDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Customize as needed
    private LocalDateTime returnDate;
    
    private AllocationStatus allocationStatus;
    
    private String employeeName;   // add
    private String assetName;      // add
    private String categoryName;   // add from asset's category
    public AssetAllocationDTO() {
    }
    public AssetAllocationDTO(Long id, Long assetId, Long employeeId, LocalDateTime requestDate,
            LocalDateTime allocationDate, LocalDateTime returnDate, AllocationStatus allocationStatus,
            String employeeName, String assetName, String categoryName) {
        this.id = id;
        this.assetId = assetId;
        this.employeeId = employeeId;
        this.requestDate = requestDate;
        this.allocationDate = allocationDate;
        this.returnDate = returnDate;
        this.allocationStatus = allocationStatus;
        this.employeeName = employeeName;
        this.assetName = assetName;
        this.categoryName = categoryName;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getAssetId() {
        return assetId;
    }
    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }
    public Long getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    public LocalDateTime getRequestDate() {
        return requestDate;
    }
    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
    public LocalDateTime getAllocationDate() {
        return allocationDate;
    }
    public void setAllocationDate(LocalDateTime allocationDate) {
        this.allocationDate = allocationDate;
    }
    public LocalDateTime getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }
    public AllocationStatus getAllocationStatus() {
        return allocationStatus;
    }
    public void setAllocationStatus(AllocationStatus allocationStatus) {
        this.allocationStatus = allocationStatus;
    }
    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public String getAssetName() {
        return assetName;
    }
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    @Override
    public String toString() {
        return "AssetAllocationDTO [id=" + id + ", assetId=" + assetId + ", employeeId=" + employeeId + ", requestDate="
                + requestDate + ", allocationDate=" + allocationDate + ", returnDate=" + returnDate
                + ", allocationStatus=" + allocationStatus + ", employeeName=" + employeeName + ", assetName="
                + assetName + ", categoryName=" + categoryName + "]";
    }

}
