package com.example.MovieJPA.repository;

import com.example.MovieJPA.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByWatched(boolean watched);

    List<Movie> findByRatingGreaterThan(Double minRating);
}
