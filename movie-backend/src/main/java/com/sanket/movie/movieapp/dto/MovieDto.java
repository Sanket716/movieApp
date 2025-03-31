package com.sanket.movie.movieapp.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {

    private Integer movieId;


    @NotBlank(message = "Movie title can not be empty!")
    private String title;


    @NotBlank(message = "Please provide a valid director!")
    private String director;


    @NotBlank(message = "Please provide a valid studio!")
    private String studio;


    private Set<String> movieCast;



    private Integer releaseYear;


    @NotBlank(message = "Please provide a valid poster!")
    private String poster;

    @NotBlank(message = "Please provide a valid poster url!")
    private String posterUrl;
}
