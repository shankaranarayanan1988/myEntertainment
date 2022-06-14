package com.javaapps.movie.db.service;


import com.javaapps.movie.db.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Movie findByName(String name);

}
