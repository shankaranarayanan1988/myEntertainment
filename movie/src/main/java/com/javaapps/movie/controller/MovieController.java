package com.javaapps.movie.controller;


import com.javaapps.movie.db.entity.Movie;
import com.javaapps.movie.entity.MovieDetails;
import com.javaapps.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping(path =  "/")
    public String addmovie(@RequestBody Movie movie) {
        return movieService.addmovie(movie);
    }

    @GetMapping(path = "/{id}")
    public Movie getmovie(@PathVariable(name = "id") String id) {
        return movieService.getmovie(id);
    }

    @GetMapping()
    public Movie findmovie(@RequestParam String name) {
        return movieService.findmovie(name);
    }

}
