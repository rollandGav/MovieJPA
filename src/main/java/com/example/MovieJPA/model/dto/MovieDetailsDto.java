package com.example.MovieJPA.model.dto;

import lombok.Data;

@Data
public class MovieDetailsDto {
    private Long id;
    private Integer durationMinutes;
    private String language;
    private String synopsis;
}