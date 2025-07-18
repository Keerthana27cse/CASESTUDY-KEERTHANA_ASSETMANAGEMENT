package com.example.assetmanagement.mapper;

import com.example.assetmanagement.dto.ReturnRequestDTO;
import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.entity.ReturnRequest;

public class ReturnRequestMapper {

    public static ReturnRequestDTO toDTO(ReturnRequest request) {
        if (request == null) return null;

        ReturnRequestDTO dto = new ReturnRequestDTO();
        dto.setId(request.getId());
        dto.setEmployeeId(request.getEmployee().getId());
        dto.setEmployeeName(request.getEmployee().getName());
        dto.setAssetId(request.getAsset() != null ? request.getAsset().getId() : null);
        dto.setAssetName(request.getAsset() != null ? request.getAsset().getAssetName() : null);
        dto.setReason(request.getReason());
        dto.setStatus(request.getStatus());
        dto.setRequestDate(request.getRequestDate());
        dto.setReturnDate(request.getReturnDate());
        return dto;
    }

    public static ReturnRequest toEntity(ReturnRequestDTO dto, Asset asset, Employee employee) {
        if (dto == null) return null;

        ReturnRequest request = new ReturnRequest();
        request.setId(dto.getId());
        request.setEmployee(employee);
        request.setAsset(asset);
        request.setReason(dto.getReason());
        request.setStatus(dto.getStatus());
        request.setRequestDate(dto.getRequestDate());
        request.setReturnDate(dto.getReturnDate());
        return request;
    }
}
