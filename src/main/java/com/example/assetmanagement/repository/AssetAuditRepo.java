package com.example.assetmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.entity.AssetAudit;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.enums.RequestStatus;

public interface AssetAuditRepo extends JpaRepository<AssetAudit, Long> {


	    // Prevent duplicate audit records
	    boolean existsByEmployeeAndAssetAndStatus(Employee employee, Asset asset, RequestStatus status);	
	    List<AssetAudit> findByEmployeeId(Long employeeId);
		boolean existsByEmployeeIdAndAssetIdAndAuditDate(Long id, Long id2, LocalDateTime now);

}
