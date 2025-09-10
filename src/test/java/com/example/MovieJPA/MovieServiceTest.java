package com.example.MovieJPA;

import com.example.MovieJPA.exception.EmailSedingException;
import com.example.MovieJPA.exception.ServiceException;
import com.example.MovieJPA.model.*;
import com.example.MovieJPA.model.dto.MovieDto;
import com.example.MovieJPA.repository.MovieRepository;
import com.example.MovieJPA.service.EmailService;
import com.example.MovieJPA.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepo;

    @InjectMocks
    private MovieService movieService;

    @Mock
    private EmailService emailService;

    private Movie testMovie;
    private MovieDto testDto;

    @BeforeEach
    void setUp() {
        testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("Inception");
        testMovie.setReleaseYear(2010);
        testMovie.setRating(8.8);
        testMovie.setWatched(true);
        testMovie.setWatchList(false);
        Genre genre = new Genre();
        genre.setName("Sci-Fi");
        testMovie.setGenre(genre);
        Director director = new Director();
        director.setName("Christopher");
        testMovie.setDirector(director);
        Actor actor = new Actor();
        actor.setName("Leonardo D");
        testMovie.setActors(Set.of(actor));
        MovieDetails movieDetails = new MovieDetails();
        movieDetails.setDuration(148);
        movieDetails.setLanguage("EN");
        movieDetails.setSynopsis("bla bla");
        testMovie.setDetails(movieDetails);

        testDto = new MovieDto();
        testDto.setId(1L);
        testDto.setTitle("Inception");
        testDto.setReleaseYear(2010);
        testDto.setWatched(true);
        testDto.setWatchList(false);
        testDto.setRating(8.8);
        testDto.setGenreName("Sci-Fi");
        testDto.setDirectorName("Christopher");
        testDto.setActorNames(Set.of("Leonardo D"));
        testDto.setDuration(148);
        testDto.setLanguage("EN");
        testDto.setSynopsis("bla bla");
    }

    @Test
    void save_ShouldReturnSavedMovie(){
        when(movieRepo.save(testMovie)).thenReturn(testMovie);
        Movie savedMovie = movieService.save(testMovie);
        assertNotNull(savedMovie);
        assertEquals("Inception", savedMovie.getTitle());
        verify(movieRepo).save(testMovie);
    }

    @Test
    void findById_ShouldReturnMovie_WhenExists(){
        when(movieRepo.findById(1L)).thenReturn(Optional.of(testMovie));
        Optional<Movie> foundMovie = movieService.findById(1L);
        assertTrue(foundMovie.isPresent());
        assertEquals("Inception", foundMovie.get().getTitle());
        assertEquals(8.8, foundMovie.get().getRating());
        verify(movieRepo).findById(1L);
    }

    @Test
    void findById_shouldReturnEmpty_WhenNotFound(){
        when(movieRepo.findById(2L)).thenReturn(Optional.empty());
        Optional<Movie> notFoundMovie = movieService.findById(2L);
        assertFalse(notFoundMovie.isPresent());
        verify(movieRepo).findById(2L);
    }


    @Test
    void delete_ShouldDeleteMovieById(){
        doNothing().when(movieRepo).deleteById(1L);
        movieService.delete(1L);
        verify(movieRepo).deleteById(1L);
    }

    @Test
    void findByWatchTrue_ShouldReturnMovies(){
        when(movieRepo.findByWatched(true)).thenReturn(List.of(testMovie));
        List<Movie> watchedMovies = movieService.findByWatchedTrue();
        assertFalse(watchedMovies.isEmpty());
        verify(movieRepo).findByWatched(true);
    }

    @Test
    void findByRatingGreaterThen_ShouldReturnMovies(){
        when(movieRepo.findByRatingGreaterThan(8.0)).thenReturn(List.of(testMovie));
        List<Movie> ratedMovies = movieService.findByRatingGreaterThan(8.0);
        assertEquals(1, ratedMovies.size());
        assertEquals(8.8, ratedMovies.get(0).getRating());
        verify(movieRepo).findByRatingGreaterThan(8.0);
    }

    @Test
    void addMovieToWatchList_ShouldUpdateMovieShouldSendMail() throws EmailSedingException {
        when(movieRepo.findById(1L)).thenReturn(Optional.of(testMovie));
        when(movieRepo.save(testMovie)).thenReturn(testMovie);
        doNothing().when(emailService).sentEmail(anyString(),anyString(),anyString());

        MovieDto updatedMovie = movieService.addMovieToWatchList(1L, "test@gmail.com");
//        assertTrue(updatedMovie.isWatchList());
        verify(movieRepo).findById(1L);
        verify(movieRepo).save(testMovie);
        verify(emailService).sentEmail(eq("test@gmail.com"),anyString(), contains("Inception"));
    }

    @Test
    void addMovieToWatchList_ShouldTrowException_WhenEmailFails() throws EmailSedingException {
        when(movieRepo.findById(1L)).thenReturn(Optional.of(testMovie));
        when(movieRepo.save(testMovie)).thenReturn(testMovie);
        doThrow(new EmailSedingException("Failed to send email")).when(emailService)
                .sentEmail(anyString(),anyString(),anyString());

        ServiceException exception = assertThrows(ServiceException.class, () ->
                movieService.addMovieToWatchList(1L,"test@gmail.com"));

        assertEquals("Movie updated but email sending failed", exception.getMessage());
        verify(emailService).sentEmail(anyString(),anyString(),anyString());
    }

}
