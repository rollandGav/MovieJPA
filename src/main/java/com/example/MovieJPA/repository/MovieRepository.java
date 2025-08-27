package com.example.MovieJPA.repository;

import com.example.MovieJPA.model.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {
}
