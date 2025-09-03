package com.example.MovieJPA.controller;

import com.example.MovieJPA.model.Movie;
import com.example.MovieJPA.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    MovieService service;

    @PostMapping
    public ResponseEntity<Movie> createMovie (@RequestBody Movie movie){
        return ResponseEntity.ok(service.save(movie));
    }

    @GetMapping
    public ResponseEntity<Iterable<Movie>> getAllMovies(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Movie>> getMovieById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovieById(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/director/{director}/{releasedYear}")
    public ResponseEntity<List<Movie>> getByDirectorAndYear(@PathVariable String director, @PathVariable int releasedYear){
        return ResponseEntity.ok(service.findByDirectorAndReleaseYear(director,releasedYear));
    }

    @GetMapping("/watched")
    public ResponseEntity<List<Movie>> getMoviesByWatched(){
        return ResponseEntity.ok(service.findByWatchedTrue());
    }

    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<Movie>> getMoviesByRating(@PathVariable Double rating){
        return ResponseEntity.ok(service.findByRatingGreaterThan(rating));
    }

    @GetMapping("/ratingSQL/{rating}")
    public ResponseEntity<List<Movie>> getMoviesByRatingSQL(
            @PathVariable Double rating
    ){
        return ResponseEntity.ok(service.findMoviesByRatingWithSQL(rating));
    }

    @GetMapping("/ratingNativeSQL/{rating}")
    public ResponseEntity<List<Movie>> getMoviesByRatingNativeSQL(
            @PathVariable Double rating
    ){
        return ResponseEntity.ok(service.findMoviesByRatingWithNativeSQL(rating));
    }
}
