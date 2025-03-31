package com.sanket.movie.movieapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Movie title can not be empty!")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Please provide a valid director!")
    private String director;

    @Column(nullable = false)
    @NotBlank(message = "Please provide a valid studio!")
    private String studio;

    @ElementCollection
    @CollectionTable(name="movie_cast")
    private Set<String> movieCast;

    @Column(nullable = false)
    private Integer releaseYear;

    @Column(nullable = false)
    @NotBlank(message = "Please provide a valid poster!")
    private String poster;


}
