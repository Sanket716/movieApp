package com.sanket.movie.movieapp.controllers;

import com.sanket.movie.movieapp.auth.entities.ForgotPassword;
import com.sanket.movie.movieapp.auth.entities.User;
import com.sanket.movie.movieapp.auth.repositories.ForgotPasswordRepository;
import com.sanket.movie.movieapp.auth.repositories.UserRepository;
import com.sanket.movie.movieapp.dto.ChangePassword;
import com.sanket.movie.movieapp.dto.MailBody;
import com.sanket.movie.movieapp.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/forgotPassword")
@CrossOrigin(origins = "*")
public class ForgotPasswordController {

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final ForgotPasswordRepository forgotPasswordRepository;

    private final PasswordEncoder passwordEncoder;


    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/verifyemail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Please provide a valid email."));
        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder().to(email).text("Otp for Forgot password: "+otp).subject("OTP of Forgot Password").build();
        Optional<ForgotPassword> forgotPassword = forgotPasswordRepository.findByUser(user);
        forgotPassword.ifPresent(forgotPasswordRepository::delete);
        ForgotPassword fp = ForgotPassword.builder().otp(otp).expirationTime(new Date(System.currentTimeMillis()+20*100000)).user(user).build();
        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return ResponseEntity.ok("Email sent for verification!");

    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Please provide a valid email."));

        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndUser(otp,user).orElseThrow(()->new RuntimeException("Invalid OTP for email : " + email));

        if(forgotPassword.getExpirationTime().before(Date.from(Instant.now())))
        {
            forgotPasswordRepository.delete(forgotPassword);
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>("OTP verified!",HttpStatus.OK);
    }

    @PostMapping("/changePassword/{email}/otp/{otp}")
    public ResponseEntity<String> changePasswordHandler(@PathVariable String email, @PathVariable Integer otp, @RequestBody ChangePassword changePassword)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Please provide a valid email."));

        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndUser(otp,user).orElseThrow(()->new RuntimeException("Invalid OTP for email : " + email));

        if(forgotPassword.getExpirationTime().before(Date.from(Instant.now())))
        {
            forgotPasswordRepository.delete(forgotPassword);
            return new ResponseEntity<>("OTP has expired! Try it again.", HttpStatus.EXPECTATION_FAILED);
        }

        if(!Objects.equals(changePassword.password(), changePassword.repeatPassword()))
        {
            return new ResponseEntity<>("Entered passwords doesn't matched!",HttpStatus.EXPECTATION_FAILED);
        }
        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email,encodedPassword);
        return ResponseEntity.ok("Password updated successfully!");

    }

    private Integer otpGenerator()
    {
        Random random = new Random();
        return random.nextInt(100_000,999_999);
    }
}
