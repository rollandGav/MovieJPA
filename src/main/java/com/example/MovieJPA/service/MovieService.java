package com.example.MovieJPA.service;

import com.example.MovieJPA.model.*;
import com.example.MovieJPA.model.dto.MovieDto;
import com.example.MovieJPA.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepo;
    @Autowired
    MovieDetailsRepository movieDetailsRepo;
    @Autowired
    DirectorRepository directorRepo;
    @Autowired
    ActorRepository actorRepo;
    @Autowired
    GenreRepository genreRepo;

    public Movie save(Movie movie) {
        return movieRepo.save(movie);
    }

    public Optional<Movie> findById(Long id) {
        return movieRepo.findById(id);
    }

    public List<Movie> findAll() {
        return movieRepo.findAll();
    }

    public void delete(Long id) {
        movieRepo.deleteById(id);
    }

    public List<Movie> findByWatchedTrue() {
        return movieRepo.findByWatched(true);
    }

    public List<Movie> findByRatingGreaterThan(Double rating) {
        return movieRepo.findByRatingGreaterThan(rating);
    }


    public Movie createFromDto(MovieDto dto){
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setReleaseYear(dto.getReleaseYear());
        movie.setWatched(dto.isWatched());
        movie.setRating(dto.getRating());

        if(dto.getGenreName() != null && !dto.getGenreName().isBlank()){
            Genre genre = genreRepo.findByNameIgnoreCase(dto.getGenreName())
                    .orElseGet(() -> {
                        Genre g = new Genre();
                        g.setName(dto.getGenreName());
                        return genreRepo.save(g);
                    });
            movie.setGenre(genre);
        }

        if(dto.getDirectorName() != null && !dto.getDirectorName().isBlank()){
            Director director = directorRepo.findByNameIgnoreCase(dto.getDirectorName())
                    .orElseGet(() ->{
                        Director d = new Director();
                        d.setName(dto.getDirectorName());
                        return directorRepo.save(d);
                    });

            movie.setDirector(director);
        }

        if(dto.getActorNames() != null && !dto.getActorNames().isEmpty()){
            Set<Actor> actors = dto.getActorNames().stream()
                    .filter(n -> n!=null && !n.isBlank())
                    .map(name -> actorRepo.findByNameIgnoreCase(name)
                            .orElseGet(() -> {
                                Actor a = new Actor();
                                a.setName(name);
                                return actorRepo.save(a);
                            })
                    ).collect(Collectors.toSet());
            movie.setActors(actors);
        }

        if (dto.getDuration() != null || dto.getLanguage() !=null || dto.getSynopsis() != null){
            MovieDetails details = new MovieDetails();
            details.setDuration(dto.getDuration());
            details.setLanguage(dto.getLanguage());
            details.setSynopsis(dto.getSynopsis());
            movie.setDetails(details);
        }

        return movieRepo.save(movie);
    }

    public MovieDto toDto(Movie movie){
        MovieDto dto = new MovieDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setWatched(movie.isWatched());
        dto.setRating(movie.getRating());

        if(movie.getGenre() != null) dto.setGenreName(movie.getGenre().getName());
        if (movie.getDirector()!=null) dto.setDirectorName(movie.getDirector().getName());
        if(movie.getActors() !=null) dto.setActorNames(movie.getActors().stream().map(Actor::getName).collect(Collectors.toSet()));
        if(movie.getDetails() !=null){
            dto.setDuration(movie.getDetails().getDuration());
            dto.setLanguage(movie.getDetails().getLanguage());
            dto.setSynopsis(movie.getDetails().getSynopsis());
        }
        return dto;
    }
}