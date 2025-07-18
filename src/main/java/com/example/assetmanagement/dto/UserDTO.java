package com.example.assetmanagement.dto;

import com.example.assetmanagement.enums.UserRole;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private UserRole userrole;

    // No-arg constructor
    public UserDTO() {
    }

    // All-arg constructor
    public UserDTO(Long id, String username, String email, UserRole userrole) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userrole = userrole;
    }

    // Getters and Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getUserrole() {
        return userrole;
    }

    public void setUserrole(UserRole userrole) {
        this.userrole = userrole;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userrole=" + userrole +
                '}';
    }
}
