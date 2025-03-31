package com.sanket.movie.movieapp.service;

import com.sanket.movie.movieapp.dto.MovieDto;
import com.sanket.movie.movieapp.dto.MoviePageResponse;
import com.sanket.movie.movieapp.entities.Movie;
import com.sanket.movie.movieapp.exceptions.FileExistException;
import com.sanket.movie.movieapp.exceptions.MovieNotFoundException;
import com.sanket.movie.movieapp.repositories.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    private final FileService fileService;

    private final ModelMapper modelMapper;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;


    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService, ModelMapper modelMapper) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {

        //upload the file
        if(Files.exists(Paths.get(path+File.separator+file.getOriginalFilename())))
        {
            throw new FileExistException("File Already Exist! Please add the file with another name.");
        }

        String uploadedFileName = fileService.uploadFile(path, file);

        // set the value of the field poster as a file name
        movieDto.setPoster(uploadedFileName);

        //Map DTO to entity
        Movie movie = modelMapper.map(movieDto,Movie.class);
        movie.setMovieId(null);

        // save the movie object
        Movie savedMovie = movieRepository.save(movie);

        // generate the poster URL
        String posterUrl = baseUrl + "/file/" + uploadedFileName;

        // map movie object to dto object and return
        MovieDto response = modelMapper.map(savedMovie,MovieDto.class);
        response.setPosterUrl(posterUrl);

        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        //check the data in DB and fetch the data based on the movie id
        Movie movie = movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie not found with id : " + movieId));

        //generate the URL
        String posterUrl = baseUrl + "/file/" + movie.getPoster();

        //map the movie object and return it
        MovieDto response = modelMapper.map(movie, MovieDto.class);
        response.setPosterUrl(posterUrl);
        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        //fetch all the data from the DB
        List<Movie> movies = movieRepository.findAll();

        List<MovieDto> movieDtos = new ArrayList<>();

        //iterate to the list and generate the post url for each movie object
        for(Movie movie: movies)
        {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();

            MovieDto movieDto = modelMapper.map(movie, MovieDto.class);
            movieDto.setPosterUrl(posterUrl);

            movieDtos.add(movieDto);
        }

        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        //check the data in DB and fetch the data based on the movie id
        Movie movie = movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie not found with id " + movieId));

        String fileName = movie.getPoster();

        if(file!=null)
        {
            Files.deleteIfExists(Paths.get(path+File.separator+fileName));
            fileName = fileService.uploadFile(path, file);
        }

        movieDto.setPoster(fileName);

        Movie updatedMovieBasedOnDTO = modelMapper.map(movieDto,Movie.class);
        updatedMovieBasedOnDTO.setMovieId(movie.getMovieId());

        Movie updatedMovie = movieRepository.save(updatedMovieBasedOnDTO);

        String posterUrl = baseUrl + "/file/" + fileName;

        MovieDto updatedMovieDTO = modelMapper.map(updatedMovie,MovieDto.class);
        updatedMovieDTO.setPosterUrl(posterUrl);

        return updatedMovieDTO;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        //check the data in DB and fetch the data based on the movie id
        Movie movie = movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie not found with id " + movieId));
        Integer id = movie.getMovieId();
        String fileName = movie.getPoster();
        Files.deleteIfExists(Paths.get(path+File.separator+fileName));
        movieRepository.delete(movie);

        return "Movie deleted successfully with id " + id;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();

        //iterate to the list and generate the post url for each movie object
        for(Movie movie: movies)
        {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();

            MovieDto movieDto = modelMapper.map(movie, MovieDto.class);
            movieDto.setPosterUrl(posterUrl);

            movieDtos.add(movieDto);
        }


        return new MoviePageResponse(movieDtos,pageNumber,pageSize,moviePages.getTotalPages(),moviePages.getTotalElements(),moviePages.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();

        //iterate to the list and generate the post url for each movie object
        for(Movie movie: movies)
        {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();

            MovieDto movieDto = modelMapper.map(movie, MovieDto.class);
            movieDto.setPosterUrl(posterUrl);

            movieDtos.add(movieDto);
        }


        return new MoviePageResponse(movieDtos,pageNumber,pageSize,moviePages.getTotalPages(),moviePages.getTotalElements(),moviePages.isLast());

    }
}
