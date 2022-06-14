package com.javaapps.theatre.service;

import com.javaapps.theatre.db.entity.MovieSchedule;
import com.javaapps.theatre.db.entity.Theatre;
import com.javaapps.theatre.db.service.MovieScheduleRepository;
import com.javaapps.theatre.db.service.TheatreRepository;
import com.javaapps.theatre.entity.Movie;
import com.javaapps.theatre.entity.MovieAllotment;
import com.javaapps.theatre.entity.Schedule;
import com.javaapps.theatre.entity.TheatreSchedule;
import com.javaapps.theatre.exception.BadDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TheatreServiceImpl implements TheatreService {

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private MovieScheduleRepository movieScheduleRepository;

    @Autowired
    private RestTemplate restTemplate;

    public String addTheatre(Theatre theatre) {
        log.info("Adding a theatre "+theatre);
        theatreRepository.save(theatre);
        return theatre.getName();
    }

    public Theatre getTheatre(String id) {
        log.info("Retrieving a theatre by id "+id);
        return Optional.ofNullable(theatreRepository.findById(Integer.valueOf(id))).map(data -> {
            log.info("Theatre retrieved "+data.get());
            return data.get();
        }).orElse(null);
    }

    public Theatre findTheatre(String name, String area) {
        log.info("Searching for a theatre by name "+name+" and area "+area);
        return theatreRepository.findByNameAndArea(name, area);
    }

    @Override
    public String addMovieForTheDay(String theatreName, MovieAllotment movieAllotment, String date) throws BadDataException {
        {

            Date allotmentDate= getDate(date);
            Theatre theatre = findTheatre(theatreName, movieAllotment.getArea());
            Movie movie = getMovie(movieAllotment.getMovieName());

            if(movie != null) {
                MovieSchedule movieSchedule = new MovieSchedule();
                movieSchedule.setMovieId(movie.getId());
                movieSchedule.setTheatreId(theatre.getId());
                movieSchedule.setDate(allotmentDate);
                movieSchedule.setTimings(movieAllotment.getTimings().toString());

                log.info("Saving movie schedule for "+movieAllotment);
                movieScheduleRepository.save(movieSchedule);

                return movieAllotment.getMovieName();
            } else {
                log.error("No movie found for "+movieAllotment.getMovieName());
                return null;
            }

        }
    }

    @Override
    public String updateMovieForTheDay(String theatreName, MovieAllotment movieAllotment, String date) throws BadDataException {

        Date allotmentDate= getDate(date);
        Theatre theatre = findTheatre(theatreName, movieAllotment.getArea());
        Movie movie = getMovie(movieAllotment.getMovieName());

        if(movie != null) {

            MovieSchedule movieSchedule = movieScheduleRepository.findByTheatreIdAndMovieIdAndDate(theatre.getId(), movie.getId(), allotmentDate);
            movieSchedule.setTimings(movieAllotment.getTimings().toString());

            log.info("Updating movie schedule for "+movieAllotment);
            movieScheduleRepository.save(movieSchedule);
            return movieAllotment.getMovieName();
        } else {
            log.error("No movie found for "+movieAllotment.getMovieName());
            return null;
        }

    }

    @Override
    public String deleteMovieForTheDay(String theatreName, String area, String movieName, String date) throws BadDataException {

        Date allotmentDate= getDate(date);
        Theatre theatre = theatreRepository.findByNameAndArea(theatreName, area);
        Movie movie = getMovie(movieName);

        if(movie != null) {

            log.info("Finding the movie schedule for "+movieName+" and "+date);
            MovieSchedule movieSchedule = movieScheduleRepository.findByTheatreIdAndMovieIdAndDate(theatre.getId(), movie.getId(), allotmentDate);

            log.info("Deleting movie schedule for "+movieSchedule);
            movieScheduleRepository.delete(movieSchedule);
            return movieName;

        } else {
            log.error("No movie found for "+movieName);
            return null;
        }

    }

    @Override
    public Schedule findTheatres(String movieName, String city, String date) throws BadDataException {

        Date searchableDate= getDate(date);
        Movie movie = getMovie(movieName);
        List<Theatre> theatres = theatreRepository.findByCityContaining(city);
        List<Integer> theatreIds = theatres.stream().map(theatre -> theatre.getId()).collect(Collectors.toList());

        List<MovieSchedule> movieSchedules = movieScheduleRepository.
                findByTheatreIdInAndMovieIdAndDate(
                        theatreIds, movie.getId(), searchableDate);

        Schedule schedule = new Schedule();
        schedule.setMovieName(movieName);
        List<TheatreSchedule> theatreSchedules = new ArrayList<>();

        movieSchedules.forEach(movieSchedule -> {
            TheatreSchedule theatreSchedule = new TheatreSchedule();
            Theatre theatre = getTheatre(movieSchedule.getTheatreId());
            theatreSchedule.setTheatreName(theatre.getName());
            theatreSchedule.setArea(theatre.getArea());
            theatreSchedule.setTimings(movieSchedule.getTimings());
            theatreSchedules.add(theatreSchedule);

        });
        schedule.setTheatreSchedules(theatreSchedules);

        return schedule;
    }



    private Movie getMovie(String name) {

        log.info("Retrieving movie information for "+name);
        ResponseEntity<Movie> movies = restTemplate.getForEntity("http://MOVIESERVICE/movies?name="+name, Movie.class);
        log.info("Received movie information "+movies.getBody());
        return movies.getBody();

    }

    private Theatre getTheatre(Integer id) {
        return theatreRepository.findById(id).get();
    }

    private Date getDate(String date) throws BadDataException {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            log.error("Received bad date format "+ e.getMessage());
            throw new BadDataException("The date entered is not in a valid format."+date);
        }
    }
}
