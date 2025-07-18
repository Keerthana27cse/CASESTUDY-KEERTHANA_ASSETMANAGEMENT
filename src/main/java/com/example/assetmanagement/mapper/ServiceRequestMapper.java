package com.example.assetmanagement.mapper;

import com.example.assetmanagement.dto.ServiceRequestDTO;
import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.entity.ServiceRequest;

public class ServiceRequestMapper {

    public static ServiceRequestDTO toDTO(ServiceRequest request) {
        if (request == null) return null;

        ServiceRequestDTO dto = new ServiceRequestDTO();
        dto.setId(request.getId());
        dto.setAssetId(request.getAsset().getId());
        dto.setAssetName(request.getAsset().getAssetName());
        dto.setCategoryName(request.getAsset().getCategory().getCategoryName());
        dto.setEmployeeId(request.getEmployee().getId());
        dto.setEmployeeName(request.getEmployee().getName());
        dto.setIssueType(request.getIssueType());
        dto.setDescription(request.getDescription());
        dto.setStatus(request.getStatus());
        dto.setRequestDate(request.getRequestDate());
        return dto;
    }

    public static ServiceRequest toEntity(ServiceRequestDTO dto, Asset asset, Employee employee) {
        if (dto == null) return null;

        ServiceRequest request = new ServiceRequest();
        request.setId(dto.getId());
        request.setAsset(asset);
        request.setEmployee(employee);
        request.setIssueType(dto.getIssueType());
        request.setDescription(dto.getDescription());
        request.setStatus(dto.getStatus());
        request.setRequestDate(dto.getRequestDate());
        return request;
    }
}
