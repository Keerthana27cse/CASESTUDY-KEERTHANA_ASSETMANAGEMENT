package com.example.assetmanagement.repotest;


import com.example.assetmanagement.entity.User;
import com.example.assetmanagement.enums.UserRole;
import com.example.assetmanagement.repository.UserRepo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    @DisplayName("Test findByUsername returns correct user")
    void testFindByUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        user.setUserrole(UserRole.EMPLOYEE);  // ✅ setting role
        userRepo.save(user);

        Optional<User> foundUser = userRepo.findByUsername("testuser");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("testuser@example.com");
    }

    @Test
    @DisplayName("Test findByEmail returns correct user")
    void testFindByEmail() {
        User user = new User();
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setPassword("securepassword");
        user.setUserrole(UserRole.ADMIN);  // ✅ setting role
        userRepo.save(user);

        Optional<User> foundUser = userRepo.findByEmail("johndoe@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("johndoe");
    }

    @Test
    @DisplayName("Test findByUsername returns empty when user not found")
    void testFindByUsernameNotFound() {
        Optional<User> result = userRepo.findByUsername("nonexistent");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Test findByEmail returns empty when user not found")
    void testFindByEmailNotFound() {
        Optional<User> result = userRepo.findByEmail("noone@example.com");
        assertThat(result).isEmpty();
    }
}
