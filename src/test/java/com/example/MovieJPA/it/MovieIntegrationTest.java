package com.example.MovieJPA.it;

import com.example.MovieJPA.model.Movie;
import com.example.MovieJPA.model.dto.MovieDto;
import com.example.MovieJPA.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MovieIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    private MovieDto testMovieDto;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();

        testMovieDto = new MovieDto();
        testMovieDto.setTitle("Inception");
        testMovieDto.setReleaseYear(2010);
        testMovieDto.setWatched(false);
        testMovieDto.setRating(9.0);
        testMovieDto.setGenreName("Sci-Fi");
        testMovieDto.setDirectorName("Christopher");
    }

    @Test
    void createMovie_ShouldReturnCreatedMovie() throws Exception {
        ResultActions result = mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMovieDto)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Inception")))
                .andExpect(jsonPath("$.directorName", is("Christopher")))
                .andExpect(jsonPath("$.genreName", is("Sci-Fi")))
                .andExpect(jsonPath("$.rating", is(9.0)))
                .andExpect(jsonPath("$.releaseYear", is(2010)));
    }

    @Test
    void getAllMovies_ShouldReturnList() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setReleaseYear(2010);
        movie.setWatched(true);
        movie.setRating(9.0);
        movieRepository.saveAndFlush(movie);

        ResultActions result = mockMvc.perform(get("/api/movies")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)))
                .andExpect(jsonPath("$.length()").value(greaterThan(0)))
                .andExpect(jsonPath("$[0].title").value("Inception"))
                .andExpect(jsonPath("$[0].releaseYear").value(2010))
                .andExpect(jsonPath("$[0].rating").value(9.0));
    }

    @Test
    void getMovieById_ShouldReturnMovie_WhenExists() throws Exception {
        String response = mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMovieDto)))
                .andReturn().getResponse().getContentAsString();
        MovieDto savedMovie = objectMapper.readValue(response, MovieDto.class);

        ResultActions result = mockMvc.perform(get("/api/movies/" + savedMovie.getId()));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Inception")))
                .andExpect(jsonPath("$.releaseYear", is(2010)));
    }

    @Test
    void deleteMovieById_ShouldReturnNoContent() throws Exception{
        String response = mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMovieDto)))
                .andReturn().getResponse().getContentAsString();
        MovieDto savedMovie = objectMapper.readValue(response, MovieDto.class);

        ResultActions result = mockMvc.perform(delete("/api/movies/" + savedMovie.getId()));

        result.andExpect(status().isNoContent());
    }

    @Test
    void addMovieToWatchList_ShouldUpdateAndSentEmail() throws Exception{
       String response = mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMovieDto)))
                .andReturn().getResponse().getContentAsString();
        MovieDto savedMovie = objectMapper.readValue(response, MovieDto.class);

        ResultActions result = mockMvc.perform(put("/api/movies/watchList/" + savedMovie.getId())
                .param("email", "gavrilita.rolland@gmail.com"));

        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.watchList", is(true)))
                .andExpect(jsonPath("$.directorName", is("Christopher")))
                .andExpect(jsonPath("$.title", is("Inception")));
    }
}
