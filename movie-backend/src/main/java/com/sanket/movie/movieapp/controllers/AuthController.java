package com.sanket.movie.movieapp.controllers;

import com.sanket.movie.movieapp.auth.entities.RefreshToken;
import com.sanket.movie.movieapp.auth.entities.User;
import com.sanket.movie.movieapp.auth.service.AuthService;
import com.sanket.movie.movieapp.auth.service.JwtService;
import com.sanket.movie.movieapp.auth.service.RefreshTokenService;
import com.sanket.movie.movieapp.auth.utils.AuthResponse;
import com.sanket.movie.movieapp.auth.utils.LoginRequest;
import com.sanket.movie.movieapp.auth.utils.RefreshTokenRequest;
import com.sanket.movie.movieapp.auth.utils.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build());
    }
}
