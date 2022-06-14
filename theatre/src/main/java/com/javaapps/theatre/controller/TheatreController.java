package com.javaapps.theatre.controller;


import com.javaapps.theatre.db.entity.Theatre;
import com.javaapps.theatre.db.service.TheatreRepository;
import com.javaapps.theatre.entity.MovieAllotment;
import com.javaapps.theatre.entity.Schedule;
import com.javaapps.theatre.exception.BadDataException;
import com.javaapps.theatre.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/theatres")
public class TheatreController {

    @Autowired
    private TheatreService theatreService;

    @PostMapping(path =  "/")
    public String addTheatre(@RequestBody Theatre theatre) {
        return theatreService.addTheatre(theatre);
    }

    @GetMapping(path = "/{id}")
    public Theatre getTheatre(@PathVariable(name = "id") String id) {
        return theatreService.getTheatre(id);
    }

    @GetMapping()
    public Theatre findTheatre(@RequestParam String name, @RequestParam String area) {
        return theatreService.findTheatre(name, area);
    }

    @PostMapping(path =  "/{name}/movieSchedule/{date}")
    public String addMovieForTheDay(@PathVariable(name = "name") String theatreName, @RequestBody MovieAllotment movieAllotment, @PathVariable(name = "date") String date) {
        try {
            return theatreService.addMovieForTheDay(theatreName, movieAllotment, date);
        } catch (BadDataException e) {
            return null;
        }
    }

    @PutMapping(path =  "/{name}/movieSchedule/{date}")
    public String updateMovieForTheDay(@PathVariable(name = "name") String theatreName, @RequestBody MovieAllotment movieAllotment, @PathVariable(name = "date") String date) {
        try {
            return theatreService.updateMovieForTheDay(theatreName, movieAllotment, date);
        } catch (BadDataException e) {
            return null;
        }
    }

    @GetMapping(path = "/movieSchedule/{date}")
    public Schedule findTheatres(@RequestParam String movieName, @RequestParam String city, @PathVariable(name = "date") String date)  {
        try {
            return theatreService.findTheatres(movieName, city, date);
        } catch (BadDataException e) {
            return null;
        }
    }

    @DeleteMapping(path = "/{name}/movieSchedule/{date}")
    public String deleteMovieForTheDay(@PathVariable(name = "name") String theatreName, @RequestParam String area, @RequestParam String movieName, @PathVariable(name = "date") String date) {
        try {
            return theatreService.deleteMovieForTheDay(theatreName, area, movieName, date);
        } catch (BadDataException e) {
            return null;
        }
    }

}
