package com.sanket.movie.movieapp.controllers;

import com.sanket.movie.movieapp.auth.entities.User;
import com.sanket.movie.movieapp.auth.repositories.UserRepository;
import com.sanket.movie.movieapp.dto.ResetPassword;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/resetPassword")
@CrossOrigin(origins = "*")
public class ResetPasswordController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public ResetPasswordController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable String email, @RequestBody ResetPassword resetPassword)
    {
        if(resetPassword.newPassword()==null || resetPassword.newPassword().isEmpty())
        {
            throw new RuntimeException("New password cannot be null or empty!");
        }
        User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Please provide a valid email."));

        String oldPass = user.getPassword();

        if(!passwordEncoder.matches(resetPassword.oldPassword(), oldPass)){
            throw new RuntimeException("Invalid old password!");
        }

        if(!Objects.equals(resetPassword.newPassword(),resetPassword.repeatPassword()))
        {
            return new ResponseEntity<>("Entered passwords doesn't matched!", HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword = passwordEncoder.encode(resetPassword.newPassword());
        userRepository.updatePassword(email,encodedPassword);
        return ResponseEntity.ok("Password updated successfully!");

    }


}
