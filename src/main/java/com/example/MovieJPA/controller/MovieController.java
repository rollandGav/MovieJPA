package com.example.MovieJPA.controller;

import com.example.MovieJPA.model.Movie;
import com.example.MovieJPA.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
