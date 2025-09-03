package com.example.MovieJPA.repository;

import com.example.MovieJPA.model.MovieDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDetailsRepository extends JpaRepository<MovieDetails, Long> {
}
