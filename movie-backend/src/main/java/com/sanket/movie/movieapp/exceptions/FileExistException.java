package com.sanket.movie.movieapp.exceptions;

public class FileExistException extends RuntimeException{

    public FileExistException(String message) {
        super(message);
    }
}
