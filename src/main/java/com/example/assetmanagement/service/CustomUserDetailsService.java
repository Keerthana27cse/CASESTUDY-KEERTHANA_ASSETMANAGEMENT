package com.example.assetmanagement.service;

import com.example.assetmanagement.entity.Employee;
import com.example.assetmanagement.entity.User;
import com.example.assetmanagement.enums.EmployeeStatus;
import com.example.assetmanagement.enums.UserRole;
import com.example.assetmanagement.repository.EmployeeRepo;
import com.example.assetmanagement.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Autowired private EmployeeRepo employeeRepo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        if (user.getUserrole() == UserRole.EMPLOYEE) {
            Employee emp = employeeRepo.findByEmail(user.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

            if (emp.getEmpstatus() == EmployeeStatus.INACTIVE) {
                throw new DisabledException("Account inactive. Please contact your manager.");
            }
        }
        
        
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getUserrole().name()))
        );
    }
}
