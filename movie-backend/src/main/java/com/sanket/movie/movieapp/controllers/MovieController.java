package com.sanket.movie.movieapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanket.movie.movieapp.dto.MovieDto;
import com.sanket.movie.movieapp.dto.MoviePageResponse;
import com.sanket.movie.movieapp.exceptions.EmptyFileException;
import com.sanket.movie.movieapp.service.MovieService;
import com.sanket.movie.movieapp.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/movie")
@CrossOrigin(origins = "*")
public class MovieController {
    private final MovieService movieService;

    private final ObjectMapper objectMapper;

    public MovieController(MovieService movieService, ObjectMapper objectMapper) {
        this.movieService = movieService;
        this.objectMapper = objectMapper;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("")
    public ResponseEntity<MovieDto> addMovie(@RequestPart MultipartFile file, @RequestPart String movieData) throws IOException {
        if(file.isEmpty()) throw new EmptyFileException("File is empty! Send another file.");
        MovieDto movieDto = objectMapper.readValue(movieData,MovieDto.class);
        MovieDto response = movieService.addMovie(movieDto,file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{movieId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Integer movieId,
                                             @RequestPart(required = false) MultipartFile file,
                                             @RequestPart String movieData) throws IOException {
        if(file==null || file.isEmpty()) file=null;
        MovieDto movieDto = objectMapper.readValue(movieData,MovieDto.class);
        MovieDto response = movieService.updateMovie(movieId,movieDto,file);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{movieId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteMovie(@PathVariable Integer movieId) throws IOException {
        String deletedMovieMsg = movieService.deleteMovie(movieId);
        return ResponseEntity.ok(deletedMovieMsg);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> viewMovieDetails(@PathVariable Integer movieId)  {
        MovieDto movieDto = movieService.getMovie(movieId);
        return ResponseEntity.ok(movieDto);
    }

    @GetMapping("")
    public ResponseEntity<List<MovieDto>> getAllMovies()  {
        List<MovieDto> movieList = movieService.getAllMovies();
        return ResponseEntity.ok(movieList);
    }

    @GetMapping("/pagination")
    public ResponseEntity<MoviePageResponse> getAllMovies(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    )  {
        return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber, pageSize));
    }

    @GetMapping("/sorting")
    public ResponseEntity<MoviePageResponse> getAllMovies(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
             @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String dir

    )  {
        return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNumber, pageSize,sortBy,dir));
    }
}
