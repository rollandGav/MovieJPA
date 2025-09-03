package com.example.MovieJPA.service;

import com.example.MovieJPA.model.*;
import com.example.MovieJPA.model.dto.MovieDto;
import com.example.MovieJPA.repository.ActorRepository;
import com.example.MovieJPA.repository.DirectorRepository;
import com.example.MovieJPA.repository.GenreRepository;
import com.example.MovieJPA.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepo;
    private final GenreRepository genreRepo;
    private final ActorRepository actorRepo;
    private final DirectorRepository directorRepo;


    public MovieService(MovieRepository movieRepo,
                        GenreRepository genreRepo,
                        ActorRepository actorRepo,
                        DirectorRepository directorRepo) {
        this.movieRepo = movieRepo;
        this.genreRepo = genreRepo;
        this.actorRepo = actorRepo;
        this.directorRepo = directorRepo;
    }

    public Movie save(Movie movie) {
        return movieRepo.save(movie);
    }

    public List<Movie> findAll() {
        return movieRepo.findAll();
    }

    public Optional<Movie> findById(Long id) {
        return movieRepo.findById(id);
    }

    public void delete(Long id) {
        movieRepo.deleteById(id);
    }

    public List<Movie> findByWatchedTrue() {
        return movieRepo.findByWatchedTrue();
    }

    public List<Movie> findByRatingGreaterThan(Double rating) {
        return movieRepo.findByRatingGreaterThan(rating);
    }

    public MovieDto toDto(Movie movie) {
        MovieDto dto = new MovieDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setWatched(movie.isWatched());
        dto.setRating(movie.getRating());

        if (movie.getGenre() != null) dto.setGenreName(movie.getGenre().getName());
        if (movie.getDirector() != null) dto.setDirectorName(movie.getDirector().getName());
        if (movie.getActors() != null) dto.setActorNames(
                movie.getActors().stream().map(Actor::getName).collect(Collectors.toSet())
        );
        if (movie.getDetails() != null) {
            dto.setDurationMinutes(movie.getDetails().getDurationMinutes());
            dto.setLanguage(movie.getDetails().getLanguage());
            dto.setSynopsis(movie.getDetails().getSynopsis());
        }
        return dto;
    }

    public Movie createFromDto(MovieDto dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setReleaseYear(dto.getReleaseYear());
        movie.setWatched(dto.isWatched());
        movie.setRating(dto.getRating());

        // Genre
        if (dto.getGenreName() != null && !dto.getGenreName().isBlank()) {
            Genre genre = genreRepo.findByNameIgnoreCase(dto.getGenreName())
                    .orElseGet(() -> {
                        Genre g = new Genre();
                        g.setName(dto.getGenreName());
                        return genreRepo.save(g);
                    });
            movie.setGenre(genre);
        }

        // Director
        if (dto.getDirectorName() != null && !dto.getDirectorName().isBlank()) {
            Director director = directorRepo.findByNameIgnoreCase(dto.getDirectorName())
                    .orElseGet(() -> {
                        Director d = new Director();
                        d.setName(dto.getDirectorName());
                        return directorRepo.save(d);
                    });
            movie.setDirector(director);
        }

        // Actors
        if (dto.getActorNames() != null && !dto.getActorNames().isEmpty()) {
            Set<Actor> actors = dto.getActorNames().stream()
                    .filter(n -> n != null && !n.isBlank())
                    .map(name -> actorRepo.findByNameIgnoreCase(name)
                            .orElseGet(() -> {
                                Actor a = new Actor();
                                a.setName(name);
                                return actorRepo.save(a);
                            })
                    ).collect(Collectors.toSet());
            movie.setActors(actors);
        }

        // Details (optional)
        if (dto.getDurationMinutes() != null || dto.getLanguage() != null || dto.getSynopsis() != null) {
            MovieDetails details = new MovieDetails();
            details.setDurationMinutes(dto.getDurationMinutes());
            details.setLanguage(dto.getLanguage());
            details.setSynopsis(dto.getSynopsis());
            movie.setDetails(details); // cascade will save details
        }

        return movieRepo.save(movie);
    }

}