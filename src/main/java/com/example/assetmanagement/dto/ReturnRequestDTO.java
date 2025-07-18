package com.example.assetmanagement.dto;

import com.example.assetmanagement.enums.RequestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ReturnRequestDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private Long assetId;
    private String assetName;
    private String reason;
    private RequestStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnDate;

    // No-arg constructor
    public ReturnRequestDTO() {
    }

    // All-arg constructor
    public ReturnRequestDTO(Long id, Long employeeId, String employeeName, Long assetId,
                            String assetName, String reason, RequestStatus status,
                            LocalDateTime requestDate, LocalDateTime returnDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.assetId = assetId;
        this.assetName = assetName;
        this.reason = reason;
        this.status = status;
        this.requestDate = requestDate;
        this.returnDate = returnDate;
    }

    // Getters and Setters
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    // toString
    @Override
    public String toString() {
        return "ReturnRequestDTO{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", assetId=" + assetId +
                ", assetName='" + assetName + '\'' +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", requestDate=" + requestDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
