package com.example.assetmanagement.apitest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import com.example.assetmanagement.dto.AuthResponse;
import com.example.assetmanagement.dto.EmployeeDTO;
import com.example.assetmanagement.dto.LoginRequest;
import com.example.assetmanagement.restcontroller.UserRest;
import com.example.assetmanagement.security.JwtUtil;
import com.example.assetmanagement.service.CustomUserDetailsService;
import com.example.assetmanagement.service.UserService;

class UserRestTest {

    @InjectMocks
    private UserRest userRest;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private CustomUserDetailsService userDetailsService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserService userService;
    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mock(Authentication.class));

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getAuthorities()).thenReturn((Collection) List.of((GrantedAuthority) () -> "EMPLOYEE"));
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("jwt-token");

        ResponseEntity<?> response = userRest.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponse authResponse = (AuthResponse) response.getBody();
        assertNotNull(authResponse);
        assertEquals("jwt-token", authResponse.getToken());
        assertEquals("test@example.com", authResponse.getUsername());
        assertEquals("EMPLOYEE", authResponse.getRole());
    }

    @Test
    void testLogin_InactiveAccount() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("inactive@example.com");
        loginRequest.setPassword("password");

        doThrow(new RuntimeException("User is inactive"))
            .when(authenticationManager).authenticate(any());

        ResponseEntity<?> response = userRest.login(loginRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Account inactive. Please contact your manager.", response.getBody());
    }

    @Test
    void testLogin_Unauthorized() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@example.com");
        loginRequest.setPassword("wrongpass");

        doThrow(new RuntimeException("Bad credentials"))
            .when(authenticationManager).authenticate(any());

        ResponseEntity<?> response = userRest.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Bad credentials", response.getBody());
    }
    @Test
    void testRegister_Success() {
        EmployeeDTO dto = new EmployeeDTO();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.register(dto)).thenReturn("Registration successful");
        ResponseEntity<?> response = userRest.registerEmployee(dto, bindingResult);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration successful", response.getBody());
    }

    @Test
    void testRegister_ValidationError() {
        EmployeeDTO dto = new EmployeeDTO();
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = userRest.registerEmployee(dto, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testForgotPassword_Success() {
        LoginRequest dto = new LoginRequest();
        dto.setEmail("test@example.com");
        dto.setPassword("newPassword");

        when(userService.forgotPassword("test@example.com", "newPassword"))
            .thenReturn("Password reset successful");

        ResponseEntity<?> response = userRest.forgotPassword(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset successful", response.getBody());
    }
    @Test
    void testForgotPassword_Failure() {
        LoginRequest dto = new LoginRequest();
        dto.setEmail("wrong@example.com");
        dto.setPassword("newPassword");
        when(userService.forgotPassword("wrong@example.com", "newPassword"))
            .thenThrow(new IllegalArgumentException("Invalid email"));

        ResponseEntity<?> response = userRest.forgotPassword(dto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid email", response.getBody());
    }
}
