package com.example.MovieJPA.controller;

import com.example.MovieJPA.model.Movie;
import com.example.MovieJPA.model.dto.MovieDto;
import com.example.MovieJPA.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    MovieService service;

    @PostMapping
    public ResponseEntity<MovieDto> createMovie (@RequestBody MovieDto dto){
        Movie movie = service.createFromDto(dto);
        return ResponseEntity.ok(service.toDto(movie));
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies(){
        return ResponseEntity.ok(service.findAll().stream().map(service::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id){
        Optional<Movie> movieOptional = service.findById(id);
        return movieOptional.map(movie -> ResponseEntity.ok(service.toDto(movie)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovieById(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/watched")
    public ResponseEntity<List<MovieDto>> getMoviesByWatched(){
        List<Movie> movies = service.findByWatchedTrue();
        List<MovieDto> movieDtos = movies.stream().map(service::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(movieDtos);
    }

    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<MovieDto>> getMoviesByRating(@PathVariable Double rating){
        List<Movie> movies = service.findByRatingGreaterThan(rating);
        List<MovieDto> movieDtos = movies.stream().map(service::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(movieDtos);
    }

}
