package com.javaapps.theatre.service;

import com.javaapps.theatre.db.entity.Theatre;
import com.javaapps.theatre.entity.MovieAllotment;
import com.javaapps.theatre.entity.Schedule;
import com.javaapps.theatre.exception.BadDataException;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface TheatreService {
    public String addTheatre(Theatre theatre);

    public Theatre getTheatre(String id);

    public Theatre findTheatre(String name, String area);

    Schedule findTheatres(String movieName, String city, String date) throws BadDataException;

    String addMovieForTheDay(String theatreName, MovieAllotment movieAllotment, String date) throws BadDataException;

    String updateMovieForTheDay(String theatreName, MovieAllotment movieAllotment, String date) throws BadDataException;

    String deleteMovieForTheDay(String theatreName, String area, String movieName, String date) throws BadDataException;
}
