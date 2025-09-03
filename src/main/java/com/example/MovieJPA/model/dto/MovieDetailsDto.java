package com.example.MovieJPA.model.dto;

import lombok.Data;

@Data
public class MovieDetailsDto {
    private Long id;
    private Integer duration;
    private String language;
    private String synopsis;
}
