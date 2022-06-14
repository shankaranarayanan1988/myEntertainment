package com.javaapps.movie.service;

import com.javaapps.movie.db.entity.Movie;
import com.javaapps.movie.entity.MovieDetails;

import java.util.List;

public interface MovieService {
    public String addmovie(Movie movie);

    public Movie getmovie(String id);

    public Movie findmovie(String name);
}
