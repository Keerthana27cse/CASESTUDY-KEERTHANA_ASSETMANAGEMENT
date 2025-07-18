package com.example.assetmanagement.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.assetmanagement.enums.AllocationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity 
public class AssetAllocation { 

@Id 
@GeneratedValue(strategy = GenerationType.IDENTITY) 
private Long id; 


@ManyToOne(optional = false, fetch = FetchType.EAGER)
@JoinColumn(name = "asset_id", nullable = false)
private Asset asset; //id  10 to emp a on jan,then to emp b on dec-> one laptop may be allocated multimes to differ emp or diff time

@ManyToOne(optional = false, fetch = FetchType.EAGER)
@JoinColumn(name = "employee_id", nullable = false)
private Employee employee; 

@CreationTimestamp
@Column(name = "request_date", nullable = false, updatable = false)
private LocalDateTime requestDate;
@Column(name = "allocation_date", nullable = false)
private LocalDateTime allocationDate;

@Column(name = "return_date")
private LocalDateTime returnDate;

@Enumerated(EnumType.STRING)
@Column(name = "allocation_status", nullable = false)
private AllocationStatus allocationStatus = AllocationStatus.REQUESTED;

public AssetAllocation() {
	super();
	// TODO Auto-generated constructor stub
}

public AssetAllocation(Long id, Asset asset, Employee employee, LocalDateTime requestDate, LocalDateTime allocationDate,
		LocalDateTime returnDate, AllocationStatus allocationStatus) {
	super();
	this.id = id;
	this.asset = asset;
	this.employee = employee;
	this.requestDate = requestDate;
	this.allocationDate = allocationDate;
	this.returnDate = returnDate;
	this.allocationStatus = allocationStatus;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public Asset getAsset() {
	return asset;
}

public void setAsset(Asset asset) {
	this.asset = asset;
}

public Employee getEmployee() {
	return employee;
}

public void setEmployee(Employee employee) {
	this.employee = employee;
}

public LocalDateTime getRequestDate() {
	return requestDate;
}

public void setRequestDate(LocalDateTime requestDate) {
	this.requestDate = requestDate;
}

public LocalDateTime getAllocationDate() {
	return allocationDate;
}

public void setAllocationDate(LocalDateTime allocationDate) {
	this.allocationDate = allocationDate;
}

public LocalDateTime getReturnDate() {
	return returnDate;
}

public void setReturnDate(LocalDateTime returnDate) {
	this.returnDate = returnDate;
}

public AllocationStatus getAllocationStatus() {
	return allocationStatus;
}

public void setAllocationStatus(AllocationStatus allocationStatus) {
	this.allocationStatus = allocationStatus;
}

@Override
public String toString() {
	return "AssetAllocation [id=" + id + ", asset=" + asset + ", employee=" + employee + ", requestDate=" + requestDate
			+ ", allocationDate=" + allocationDate + ", returnDate=" + returnDate + ", allocationStatus="
			+ allocationStatus + "]";
}
}
