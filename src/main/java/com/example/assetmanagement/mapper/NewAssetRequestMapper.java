package com.example.assetmanagement.mapper;

import com.example.assetmanagement.dto.NewAssetRequestDTO;
import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.entity.AssetCategory;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.entity.NewAssetRequest;

public class NewAssetRequestMapper {

    /**
     * Converts a NewAssetRequest entity to a NewAssetRequestDTO.
     */
    public static NewAssetRequestDTO toDTO(NewAssetRequest request) {
        if (request == null) return null;

        NewAssetRequestDTO dto = new NewAssetRequestDTO();
        dto.setId(request.getId());
        dto.setEmployeeId(request.getEmployee().getId());
        dto.setEmployeeName(request.getEmployee().getName());
        dto.setCategoryId(request.getRequestedCategory().getId());
        dto.setCategoryName(request.getRequestedCategory().getCategoryName());
        dto.setAssetId(request.getAsset() != null ? request.getAsset().getId() : null);
        dto.setAssetName(request.getAsset() != null ? request.getAsset().getAssetName() : null);
        dto.setDescription(request.getDescription());
        dto.setRequestReason(request.getRequestReason());
        dto.setStatus(request.getStatus());
        dto.setRequestDate(request.getRequestDate());
        dto.setFullAddress(request.getFullAddress());
        dto.setZipCode(request.getZipCode());
        dto.setPhone(request.getPhone());
        return dto;
    }

    /**
     * Converts a NewAssetRequestDTO to a NewAssetRequest entity.
     */
    public static NewAssetRequest toEntity(NewAssetRequestDTO dto, Employee employee, AssetCategory category, Asset asset) {
        if (dto == null) return null;

        NewAssetRequest request = new NewAssetRequest();
        request.setId(dto.getId());
        request.setEmployee(employee);
        request.setRequestedCategory(category);
        request.setAsset(asset); // Optional if null
        request.setDescription(dto.getDescription());
        request.setRequestReason(dto.getRequestReason());
        request.setStatus(dto.getStatus());
        request.setRequestDate(dto.getRequestDate());
        request.setFullAddress(dto.getFullAddress());
        request.setZipCode(dto.getZipCode());
        request.setPhone(dto.getPhone());
        return request;
    }
}
