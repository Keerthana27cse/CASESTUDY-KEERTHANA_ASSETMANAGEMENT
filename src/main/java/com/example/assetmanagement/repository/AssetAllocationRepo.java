package com.example.assetmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.entity.AssetAllocation;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.enums.AllocationStatus;

public interface AssetAllocationRepo extends JpaRepository<AssetAllocation, Long> {


	List<AssetAllocation> findAllByOrderByAllocationDateDesc();

	List<AssetAllocation> findByEmployee(Employee employee);
	List<AssetAllocation> findByAllocationStatus(AllocationStatus status);

	AssetAllocation findByAssetAndEmployeeAndAllocationStatus(Asset asset, Employee employee,
			AllocationStatus allocated);
	List<AssetAllocation> findByEmployeeId(Long employeeId);

	boolean existsByAssetAndAllocationStatus(Asset asset, AllocationStatus status);

	List<AssetAllocation> findByAssetAndAllocationStatus(Asset asset, AllocationStatus allocationStatus);



	AssetAllocation findByAssetAndEmployee(Asset asset, Employee employee);	
}
