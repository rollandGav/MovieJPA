package com.example.MovieJPA.controller;

import com.example.MovieJPA.model.Movie;
import com.example.MovieJPA.model.dto.MovieDto;
import com.example.MovieJPA.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto dto) {
        Movie saved = service.createFromDto(dto);
        return ResponseEntity.ok(service.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(
                service.findAll().stream().map(service::toDto).collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(service::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/watched")
    public ResponseEntity<List<MovieDto>> getWatched() {
        return ResponseEntity.ok(
                service.findByWatchedTrue().stream().map(service::toDto).collect(Collectors.toList())
        );
    }

    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<MovieDto>> byRating(@PathVariable Double rating) {
        return ResponseEntity.ok(
                service.findByRatingGreaterThan(rating).stream().map(service::toDto).collect(Collectors.toList())
        );
    }
}

