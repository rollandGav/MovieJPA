package com.example.MovieJPA.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class MovieDto {
    private Long id;
    private String title;
    private int releaseYear;
    private boolean watched;
    private Double rating;

    private String genreName;
    private String directorName;
    private Set<String> actorNames;

    private Integer duration;
    private String language;
    private String synopsis;
}
