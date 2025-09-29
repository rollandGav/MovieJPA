package com.example.MovieJPA.service;

import com.example.MovieJPA.model.Movie;
import com.example.MovieJPA.model.dto.MovieDto;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    Movie save(Movie movie);

    Movie createFromDto(MovieDto dto);

    MovieDto toDto(Movie movie);

    List<Movie>  findAll();

    Optional<Movie> findById(Long id);

    void delete(Long id);

    List<Movie> findByWatchedTrue();

    List<Movie> findByRatingGreaterThan(Double rating);

    MovieDto addMovieToWatchList(Long id, String email);
}
