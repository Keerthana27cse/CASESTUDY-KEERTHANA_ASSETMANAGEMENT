package com.example.assetmanagement.dto;

import com.example.assetmanagement.enums.AssetStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AssetDTO {
    private Long id;
    private String assetNo;
    private String assetName;
    private String assetModel;
    private String description;
    private String imageUrl;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private Double assetValue;
    private AssetStatus assetStatus;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AssetDTO() {
    }

    public AssetDTO(Long id, String assetNo, String assetName, String assetModel,
                    String description, String imageUrl, LocalDate manufacturingDate,
                    LocalDate expiryDate, Double assetValue, AssetStatus assetStatus,
                    Long categoryId, String categoryName, LocalDateTime createdAt,
                    LocalDateTime updatedAt) {
        this.id = id;
        this.assetNo = assetNo;
        this.assetName = assetName;
        this.assetModel = assetModel;
        this.description = description;
        this.imageUrl = imageUrl;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.assetValue = assetValue;
        this.assetStatus = assetStatus;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAssetNo() { return assetNo; }
    public void setAssetNo(String assetNo) { this.assetNo = assetNo; }

    public String getAssetName() { return assetName; }
    public void setAssetName(String assetName) { this.assetName = assetName; }

    public String getAssetModel() { return assetModel; }
    public void setAssetModel(String assetModel) { this.assetModel = assetModel; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDate getManufacturingDate() { return manufacturingDate; }
    public void setManufacturingDate(LocalDate manufacturingDate) { this.manufacturingDate = manufacturingDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public Double getAssetValue() { return assetValue; }
    public void setAssetValue(Double assetValue) { this.assetValue = assetValue; }

    public AssetStatus getAssetStatus() { return assetStatus; }
    public void setAssetStatus(AssetStatus assetStatus) { this.assetStatus = assetStatus; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "AssetDTO{" +
                "id=" + id +
                ", assetNo='" + assetNo + '\'' +
                ", assetName='" + assetName + '\'' +
                ", assetModel='" + assetModel + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", manufacturingDate=" + manufacturingDate +
                ", expiryDate=" + expiryDate +
                ", assetValue=" + assetValue +
                ", assetStatus=" + assetStatus +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
