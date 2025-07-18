package com.example.assetmanagement.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.assetmanagement.entity.AssetAllocation;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.enums.AllocationStatus;
import com.example.assetmanagement.repository.AssetAllocationRepo;





@Service
public class AssetAllocationService {
	

@Autowired
private AssetAllocationRepo allocationRepo;



public List<AssetAllocation> getAllAllocations() {
    return allocationRepo.findAll();
}

public List<AssetAllocation> getAllocationsByStatus(AllocationStatus status) {
    return allocationRepo.findByAllocationStatus(status);
}

public Map<Employee, Long> getAllocationCountPerEmployee() {
    return allocationRepo.findByAllocationStatus(AllocationStatus.ALLOCATED)
        .stream()
        .collect(Collectors.groupingBy(AssetAllocation::getEmployee, Collectors.counting()));
}
}