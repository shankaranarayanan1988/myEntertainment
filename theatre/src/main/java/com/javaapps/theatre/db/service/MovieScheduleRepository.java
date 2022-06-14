package com.javaapps.theatre.db.service;


import com.javaapps.theatre.db.entity.MovieSchedule;
import com.javaapps.theatre.db.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MovieScheduleRepository extends JpaRepository<MovieSchedule, Integer> {

    MovieSchedule findByTheatreIdAndMovieIdAndDate(Integer theatreId, Integer movieId, Date date);
    List<MovieSchedule> findByTheatreIdInAndMovieIdAndDate(List<Integer> theatreIds, Integer movieId, Date date);

}
