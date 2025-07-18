package com.example.assetmanagement.mapper;

import com.example.assetmanagement.dto.AssetAllocationDTO;
import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.entity.AssetAllocation;
import com.example.assetmanagement.entity.Employee;

public class AssetAllocationMapper {

    public static AssetAllocationDTO toDTO(AssetAllocation allocation) {
        if (allocation == null) return null;

        AssetAllocationDTO dto = new AssetAllocationDTO();
        Asset asset = allocation.getAsset();
        Employee emp = allocation.getEmployee();

        dto.setId(allocation.getId());
        dto.setAssetId(asset != null ? asset.getId() : null);
        dto.setEmployeeId(emp != null ? emp.getId() : null);
        dto.setEmployeeName(emp != null ? emp.getName() : "-");
        dto.setAssetName(asset != null ? asset.getAssetName() : "-");
        dto.setCategoryName(asset != null && asset.getCategory() != null ? asset.getCategory().getCategoryName() : "-");
        dto.setRequestDate(allocation.getRequestDate());
        dto.setAllocationDate(allocation.getAllocationDate());
        dto.setReturnDate(allocation.getReturnDate());
        dto.setAllocationStatus(allocation.getAllocationStatus());

        return dto;
    }

    public static AssetAllocation toEntity(AssetAllocationDTO dto, Employee employee, Asset asset) {
        if (dto == null) return null;

        AssetAllocation allocation = new AssetAllocation();
        allocation.setId(dto.getId());
        allocation.setEmployee(employee);
        allocation.setAsset(asset);
        allocation.setRequestDate(dto.getRequestDate());
        allocation.setAllocationDate(dto.getAllocationDate());
        allocation.setReturnDate(dto.getReturnDate());
        allocation.setAllocationStatus(dto.getAllocationStatus());
        return allocation;
    }
}
