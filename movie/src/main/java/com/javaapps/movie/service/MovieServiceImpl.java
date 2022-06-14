package com.javaapps.movie.service;

import com.javaapps.movie.db.entity.Movie;
import com.javaapps.movie.db.service.MovieRepository;
import com.javaapps.movie.entity.MovieDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public String addmovie(Movie movie) {

        movieRepository.save(movie);
        return movie.getName();
    }
    public Movie getmovie(String id) {

        log.info("Retrieving a movie by id "+id);
        return Optional.ofNullable(movieRepository.findById(Integer.valueOf(id))).map(data -> {
            log.info("Retrieved movie information "+data.get());
            return data.get();
        }).orElse(null);
    }

    public Movie findmovie(String name) {
        log.info("Finding a movie by name "+name);
        return movieRepository.findByName(name);
    }
}
