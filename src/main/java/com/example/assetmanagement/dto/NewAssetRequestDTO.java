package com.example.assetmanagement.dto;

import java.time.LocalDateTime;

import com.example.assetmanagement.enums.RequestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

public class NewAssetRequestDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private Long categoryId;
    private String categoryName;
    private Long assetId;
    private String assetName;
    private String description;
    private String requestReason;
    private RequestStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestDate;

    private String fullAddress;
    private String zipCode;
    private String phone;

    // No-arg constructor
    public NewAssetRequestDTO() {
    }

    // All-arg constructor
    public NewAssetRequestDTO(Long id, Long employeeId, String employeeName, Long categoryId,
                              String categoryName, Long assetId, String assetName, String description,
                              String requestReason, RequestStatus status, LocalDateTime requestDate,
                              String fullAddress, String zipCode, String phone) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.assetId = assetId;
        this.assetName = assetName;
        this.description = description;
        this.requestReason = requestReason;
        this.status = status;
        this.requestDate = requestDate;
        this.fullAddress = fullAddress;
        this.zipCode = zipCode;
        this.phone = phone;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public String getAssetName() { return assetName; }
    public void setAssetName(String assetName) { this.assetName = assetName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRequestReason() { return requestReason; }
    public void setRequestReason(String requestReason) { this.requestReason = requestReason; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }

    public String getFullAddress() { return fullAddress; }
    public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // toString
    @Override
    public String toString() {
        return "NewAssetRequestDTO{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", assetId=" + assetId +
                ", assetName='" + assetName + '\'' +
                ", description='" + description + '\'' +
                ", requestReason='" + requestReason + '\'' +
                ", status=" + status +
                ", requestDate=" + requestDate +
                ", fullAddress='" + fullAddress + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
