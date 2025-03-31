package com.sanket.movie.movieapp.dto;

public record ResetPassword(String oldPassword, String newPassword, String repeatPassword) {
}
