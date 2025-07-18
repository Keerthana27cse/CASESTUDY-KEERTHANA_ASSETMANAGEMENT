package com.example.assetmanagement.enums;

public enum RequestStatus {
    PENDING,        // Initially placed by employee
    APPROVED,       // Admin approves
    REJECTED,       // Admin rejects
    SHIPPED,        // Admin marks as shipped
    IN_USE,         // Employee is using it
    RETURN_REQUEST, // Employee requests return
    RETURNED,       // Admin accepts return
    DAMAGED,        // Employee reports damaged
    RESOLVED,       // Admin resolves damaged asset
    COMPLETED       // Final closure
, MARKED_RETURNED
}
