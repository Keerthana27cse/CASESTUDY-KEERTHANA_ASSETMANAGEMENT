package com.example.assetmanagement.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.assetmanagement.enums.RequestStatus;

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
public class AssetAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Employee linked to the asset being audited
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // The asset under audit
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(length = 1000)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(name = "audit_date", nullable = false)
    private LocalDateTime auditDate = LocalDateTime.now();

	public AssetAudit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AssetAudit(Long id, Employee employee, Asset asset, String remarks, RequestStatus status,
			LocalDateTime auditDate) {
		super();
		this.id = id;
		this.employee = employee;
		this.asset = asset;
		this.remarks = remarks;
		this.status = status;
		this.auditDate = auditDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public LocalDateTime getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(LocalDateTime auditDate) {
		this.auditDate = auditDate;
	}

	@Override
	public String toString() {
		return "AssetAudit [id=" + id + ", employee=" + employee + ", asset=" + asset + ", remarks=" + remarks
				+ ", status=" + status + ", auditDate=" + auditDate + "]";
	}
}
