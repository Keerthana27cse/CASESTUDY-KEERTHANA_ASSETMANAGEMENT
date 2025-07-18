package com.example.assetmanagement.mapper;

import com.example.assetmanagement.dto.AssetCategoryDTO;
import com.example.assetmanagement.entity.AssetCategory;

public class AssetCategoryMapper {

    public static AssetCategoryDTO toDTO(AssetCategory category) {
        if (category == null) return null;

        AssetCategoryDTO dto = new AssetCategoryDTO();
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
        return dto;
    }

    public static AssetCategory toEntity(AssetCategoryDTO dto) {
        if (dto == null) return null;

        AssetCategory category = new AssetCategory();
        category.setId(dto.getId());
        category.setCategoryName(dto.getCategoryName());
        return category;
    }
}
