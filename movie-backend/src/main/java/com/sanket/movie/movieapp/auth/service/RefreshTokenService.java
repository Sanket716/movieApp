package com.sanket.movie.movieapp.auth.service;

import com.sanket.movie.movieapp.auth.entities.RefreshToken;
import com.sanket.movie.movieapp.auth.entities.User;
import com.sanket.movie.movieapp.auth.repositories.RefreshTokenRepository;
import com.sanket.movie.movieapp.auth.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(String userName){

        User user = userRepository.findByEmail(userName).orElseThrow(()->new UsernameNotFoundException("User not found with email: "+ userName));

        RefreshToken refreshToken =user.getRefreshToken();

        if(refreshToken == null)
        {
            long refreshTokenValidity = 30 * 100000;
            refreshToken = RefreshToken.builder().refreshToken(UUID.randomUUID().toString()).expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user).build();
            refreshTokenRepository.save(refreshToken);

        }
        return refreshToken;

    }

    public RefreshToken verifyRefreshToken(String refreshToken)
    {
        RefreshToken refToken =
        refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new RuntimeException("Token not found!"));
        if(refToken.getExpirationTime().compareTo(Instant.now())<0)
        {
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh token expired..");
        }
        return refToken;
    }
}
