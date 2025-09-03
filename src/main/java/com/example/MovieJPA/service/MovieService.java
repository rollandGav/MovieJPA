package com.example.MovieJPA.service;

import com.example.MovieJPA.model.Movie;
import com.example.MovieJPA.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    MovieRepository repository;

    public Movie save(Movie movie) {
        if (movie.getMovieDetails() != null) {
            movie.getMovieDetails().setMovie(movie);
        }
        return repository.save(movie);
    }

    public Optional<Movie> findById(Long id){
        return repository.findById(id);
    }

    public Iterable<Movie> findAll(){
        return repository.findAll();
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public List<Movie> findByDirectorAndReleaseYear(String director, int releasedYear){
        return repository.findByDirectorAndReleaseYear(director,releasedYear);
    }

    public List<Movie> findByWatchedFalse(){
        return repository.findByWatched(false);
    }

    public List<Movie> findByWatchedTrue(){
        return repository.findByWatched(true);
    }

    public List<Movie> findByRatingGreaterThan(Double rating){
        return repository.findByRatingGreaterThan(rating);
    }

    public List<Movie> findMoviesByRatingWithSQL(Double rating){
        return repository.findMoviesByRatingWithSQL(rating);
    }

    public List<Movie> findMoviesByRatingWithNativeSQL(Double rating){
        return repository.findMoviesByRatingWithNativeSQL(rating);
    }
}
