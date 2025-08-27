package com.example.MovieJPA.service;

import com.example.MovieJPA.model.Movie;
import com.example.MovieJPA.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    MovieRepository repository;

    public Movie save(Movie movie){
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
}
