package com.smartdoor.security.service;

import com.smartdoor.security.dto.request.LoginRequest;
import com.smartdoor.security.dto.request.RefreshTokenRequest;
import com.smartdoor.security.dto.request.RegisterRequest;
import com.smartdoor.security.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(RefreshTokenRequest request);
    void logout(String username);
}
