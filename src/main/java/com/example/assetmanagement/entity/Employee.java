package com.example.assetmanagement.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.assetmanagement.enums.EmployeeStatus;
import com.example.assetmanagement.enums.Gender;
import com.example.assetmanagement.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Entity
public class Employee {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
	@NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    @Column(unique = true, nullable = false)
    private String username;
	
	@NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Column(nullable = false)
	private String name; 
	
	@NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email; 
	
    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Contact number must be 10 digits")
    @Column(nullable = false)
    private String contactNumber; 
	
    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    @Column(nullable = false)
	private String address;
	
    @Size(min = 6, message = "Password must be at least 6 characters")
	private String password;
    
    @NotNull(message = "Gender must be selected")
	@Enumerated(EnumType.STRING) 
	private Gender gender;
	
    @NotNull(message = "User role must be assigned")
    @Enumerated(EnumType.STRING)
	private UserRole role = UserRole.EMPLOYEE;
    
    @Enumerated(EnumType.STRING)
	private EmployeeStatus empstatus=EmployeeStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
	
    public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Employee(Long id,
			@NotBlank(message = "Username is required") @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters") String username,
			@NotBlank(message = "Name is required") @Size(max = 100, message = "Name cannot exceed 100 characters") String name,
			@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
			@NotBlank(message = "Contact number is required") @Pattern(regexp = "^\\d{10}$", message = "Contact number must be 10 digits") String contactNumber,
			@NotBlank(message = "Address is required") @Size(max = 255, message = "Address cannot exceed 255 characters") String address,
			@Size(min = 6, message = "Password must be at least 6 characters") String password,
			@NotNull(message = "Gender must be selected") Gender gender,
			@NotNull(message = "User role must be assigned") UserRole role, EmployeeStatus empstatus,
			LocalDateTime createdAt) {
		super();
		this.id = id;
		this.username = username;
		this.name = name;
		this.email = email;
		this.contactNumber = contactNumber;
		this.address = address;
		this.password = password;
		this.gender = gender;
		this.role = role;
		this.empstatus = empstatus;
		this.createdAt = createdAt;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public EmployeeStatus getEmpstatus() {
		return empstatus;
	}

	public void setEmpstatus(EmployeeStatus empstatus) {
		this.empstatus = empstatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", username=" + username + ", name=" + name + ", email=" + email
				+ ", contactNumber=" + contactNumber + ", address=" + address + ", password=" + password + ", gender="
				+ gender + ", role=" + role + ", empstatus=" + empstatus + ", createdAt=" + createdAt + "]";
	}
}
	