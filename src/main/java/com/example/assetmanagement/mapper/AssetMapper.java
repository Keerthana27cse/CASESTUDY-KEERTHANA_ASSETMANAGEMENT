package com.example.assetmanagement.mapper;

import com.example.assetmanagement.dto.AssetDTO;
import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.entity.AssetCategory;

public class AssetMapper {

    public static AssetDTO toDTO(Asset asset) {
        if (asset == null) return null;

        AssetDTO dto = new AssetDTO();
        dto.setId(asset.getId());
        dto.setAssetNo(asset.getAssetNo());
        dto.setAssetName(asset.getAssetName());
        dto.setAssetModel(asset.getAssetModel());
        dto.setDescription(asset.getDescription());
        dto.setImageUrl(asset.getImageUrl());
        dto.setManufacturingDate(asset.getManufacturingDate());
        dto.setExpiryDate(asset.getExpiryDate());
        dto.setAssetValue(asset.getAssetValue());
        dto.setAssetStatus(asset.getAssetStatus());

        if (asset.getCategory() != null) {
            dto.setCategoryId(asset.getCategory().getId());
            dto.setCategoryName(asset.getCategory().getCategoryName());
        }

        dto.setCreatedAt(asset.getCreatedAt());
        dto.setUpdatedAt(asset.getUpdatedAt());

        return dto;
    }

    public static Asset toEntity(AssetDTO dto, AssetCategory category) {
        if (dto == null) return null;

        Asset asset = new Asset();
        asset.setId(dto.getId());
        asset.setAssetNo(dto.getAssetNo());
        asset.setAssetName(dto.getAssetName());
        asset.setAssetModel(dto.getAssetModel());
        asset.setDescription(dto.getDescription());
        asset.setImageUrl(dto.getImageUrl());
        asset.setManufacturingDate(dto.getManufacturingDate());
        asset.setExpiryDate(dto.getExpiryDate());
        asset.setAssetValue(dto.getAssetValue());
        asset.setAssetStatus(dto.getAssetStatus());
        asset.setCategory(category);

        if (dto.getCreatedAt() != null) {
            asset.setCreatedAt(dto.getCreatedAt());
        }

        if (dto.getUpdatedAt() != null) {
            asset.setUpdatedAt(dto.getUpdatedAt());
        }

        return asset;
    }
}
