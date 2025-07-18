package com.example.assetmanagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.assetmanagement.dto.EmployeeDTO;
import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.entity.User;
import com.example.assetmanagement.enums.UserRole;
import com.example.assetmanagement.exceptions.EmailAlreadyExistsException;
import com.example.assetmanagement.exceptions.UserNotFoundException;
import com.example.assetmanagement.exceptions.UsernameAlreadyTakenException;
import com.example.assetmanagement.mapper.EmployeeMapper;
import com.example.assetmanagement.repository.EmployeeRepo;
import com.example.assetmanagement.repository.UserRepo;
@Service
public class UserService {

    @Autowired private UserRepo userRepo;
    @Autowired private EmployeeRepo empRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    public String register(EmployeeDTO dto) {
        if (empRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
        	throw new UsernameAlreadyTakenException("Username already taken");

        }

        Employee emp = EmployeeMapper.toEntity(dto);

        emp.setPassword(passwordEncoder.encode(dto.getPassword()));
        emp.setRole(UserRole.EMPLOYEE);
        emp.setEmpstatus(dto.getStatus());
        emp.setCreatedAt(LocalDateTime.now());

        empRepo.save(emp);

        User user = new User();
        user.setEmail(emp.getEmail());
        user.setUsername(emp.getUsername());
        user.setPassword(emp.getPassword()); // already encoded
        user.setUserrole(UserRole.EMPLOYEE);

        userRepo.save(user);

        return "Registration successful for " + emp.getName();
    }



    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User getUser(Long id) {
        return userRepo.findById(id).orElseThrow(() ->
         new UserNotFoundException("User with ID " + id + " not found"));
    }
    
    
    public String forgotPassword(String email, String newPassword) {
        Optional<User> userOpt = userRepo.findByEmail(email);
        
        if (userOpt.isEmpty()) {
        	throw new UserNotFoundException("Email not registered");
        }

        User user = userOpt.get();
        
       

        String encodedPwd = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPwd);
        userRepo.save(user);

        Optional<Employee> empOpt = empRepo.findByEmail(email);
        empOpt.ifPresent(emp -> {
            emp.setPassword(encodedPwd);
            empRepo.save(emp);
        });

        return "Password updated successfully";
    }

}
