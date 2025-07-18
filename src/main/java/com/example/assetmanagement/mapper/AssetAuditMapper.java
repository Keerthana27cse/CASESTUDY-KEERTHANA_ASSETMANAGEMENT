package com.example.assetmanagement.mapper;

import com.example.assetmanagement.dto.AssetAuditDTO;
import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.entity.AssetAudit;
import com.example.assetmanagement.entity.Employee;

public class AssetAuditMapper {

    public static AssetAuditDTO toDTO(AssetAudit audit) {
        if (audit == null) return null;

        AssetAuditDTO dto = new AssetAuditDTO();
        dto.setId(audit.getId());
        dto.setEmployeeId(audit.getEmployee().getId());
        dto.setEmployeeName(audit.getEmployee().getName());
        dto.setAssetId(audit.getAsset().getId());
        dto.setAssetName(audit.getAsset().getAssetName());
        dto.setRemarks(audit.getRemarks());
        dto.setStatus(audit.getStatus());
        dto.setAuditDate(audit.getAuditDate());
        dto.setCategoryName(audit.getAsset().getCategory().getCategoryName());

        return dto;
    }

    public static AssetAudit toEntity(AssetAuditDTO dto, Employee employee, Asset asset) {
        if (dto == null) return null;

        AssetAudit audit = new AssetAudit();
        audit.setId(dto.getId());
        audit.setEmployee(employee);
        audit.setAsset(asset);
        audit.setRemarks(dto.getRemarks());
        audit.setStatus(dto.getStatus());
        audit.setAuditDate(dto.getAuditDate());
        return audit;
    }
}
