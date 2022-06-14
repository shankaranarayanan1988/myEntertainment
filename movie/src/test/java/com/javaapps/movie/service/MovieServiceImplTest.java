package com.javaapps.movie.service;

import com.javaapps.movie.db.entity.Movie;
import com.javaapps.movie.db.service.MovieRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    @Test
    void testAddMovie() {

        Movie movie = new Movie(null, "Avengers", 100, "UA");
        String name = movieService.addmovie(movie);
        Assertions.assertThat(name).isNotNull().isNotEmpty().isEqualTo("Avengers");

    }

    @Test
    void testGetUnknownMovie() {

        Mockito.when(movieRepository.findById(1234)).thenReturn(null);
        Movie movie = movieService.getmovie("1234");
        Assertions.assertThat(movie).isNull();

    }

    @Test
    void testGetAvailableMovie() {

        Movie data = new Movie(1234, "Avengers", 100, "UA");
        Mockito.when(movieRepository.findById(1234)).thenReturn(Optional.of(data));
        Movie movie = movieService.getmovie("1234");
        Assertions.assertThat(movie).isNotNull().isEqualTo(data);

    }

    @Test
    void testFindUnknownMovieByName() {

        Mockito.when(movieRepository.findByName("Avengers")).thenReturn(null);
        Movie movie = movieService.findmovie("Avengers");
        Assertions.assertThat(movie).isNull();

    }

    @Test
    void testFindKnownMovieByName() {

        Movie data = new Movie(1234, "Avengers", 100, "UA");
        Mockito.when(movieRepository.findByName("Avengers")).thenReturn(data);
        Movie movie = movieService.findmovie("Avengers");
        Assertions.assertThat(movie).isNotNull().isEqualTo(data);

    }

}
