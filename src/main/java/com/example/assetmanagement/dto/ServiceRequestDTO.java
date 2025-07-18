package com.example.assetmanagement.dto;

import com.example.assetmanagement.enums.IssueType;
import com.example.assetmanagement.enums.RequestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ServiceRequestDTO {
    private Long id;
    private Long assetId;
    private String assetName;
    private String employeeName;
    private String categoryName;
    private Long employeeId;
    private IssueType issueType;
    private String description;
    private RequestStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestDate;

    // No-arg constructor
    public ServiceRequestDTO() {
    }

    // All-arg constructor
    public ServiceRequestDTO(Long id, Long assetId, String assetName, String employeeName,
                             String categoryName, Long employeeId, IssueType issueType,
                             String description, RequestStatus status, LocalDateTime requestDate) {
        this.id = id;
        this.assetId = assetId;
        this.assetName = assetName;
        this.employeeName = employeeName;
        this.categoryName = categoryName;
        this.employeeId = employeeId;
        this.issueType = issueType;
        this.description = description;
        this.status = status;
        this.requestDate = requestDate;
    }

    // Getters and Setters

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

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public String toString() {
        return "ServiceRequestDTO{" +
                "id=" + id +
                ", assetId=" + assetId +
                ", assetName='" + assetName + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", employeeId=" + employeeId +
                ", issueType=" + issueType +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", requestDate=" + requestDate +
                '}';
    }
}
