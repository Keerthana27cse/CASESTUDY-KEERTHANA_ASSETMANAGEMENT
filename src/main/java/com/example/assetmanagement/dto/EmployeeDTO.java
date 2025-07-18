package com.example.assetmanagement.dto;


import java.time.LocalDateTime;

import com.example.assetmanagement.enums.EmployeeStatus;
import com.example.assetmanagement.enums.Gender;


public class EmployeeDTO {
	private  Long id;
    private String username;
    private String name;
    private String email;
    private String password;
    private String contactNumber;
    private String address;
    private LocalDateTime createdAt;
    private EmployeeStatus status = EmployeeStatus.ACTIVE;
    private Gender gender;
    public EmployeeDTO() {
    }
    public EmployeeDTO(Long id, String username, String name, String email, String password, String contactNumber,
            String address, LocalDateTime createdAt, EmployeeStatus status, Gender gender) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.contactNumber = contactNumber;
        this.address = address;
        this.createdAt = createdAt;
        this.status = status;
        this.gender = gender;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public EmployeeStatus getStatus() {
        return status;
    }
    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    @Override
    public String toString() {
        return "EmployeeDTO [id=" + id + ", username=" + username + ", name=" + name + ", email=" + email
                + ", password=" + password + ", contactNumber=" + contactNumber + ", address=" + address
                + ", createdAt=" + createdAt + ", status=" + status + ", gender=" + gender + "]";
    }
}