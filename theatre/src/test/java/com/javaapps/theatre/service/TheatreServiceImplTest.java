package com.javaapps.theatre.service;


import com.javaapps.theatre.db.entity.MovieSchedule;
import com.javaapps.theatre.db.entity.Theatre;
import com.javaapps.theatre.db.service.MovieScheduleRepository;
import com.javaapps.theatre.db.service.TheatreRepository;
import com.javaapps.theatre.entity.Movie;
import com.javaapps.theatre.entity.MovieAllotment;
import com.javaapps.theatre.entity.Timing;
import com.javaapps.theatre.exception.BadDataException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TheatreServiceImplTest {

    @Mock
    private TheatreRepository theatreRepository;

    @Mock
    private MovieScheduleRepository movieScheduleRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TheatreServiceImpl theatreService;

    private static Theatre data;
    private static MovieAllotment movieAllotment;
    private static Movie movie;
    private static MovieSchedule movieSchedule;
    private static Date date;

    @BeforeAll
    static void init() {
        data = new Theatre(1234, "PVR", "Yelahanka",
                "Bangalore", "Karnataka", "India",
                "560064", null, null);

        movieAllotment = new MovieAllotment();
        movieAllotment.setMovieName("Indianajones");
        movieAllotment.setArea("Yelahanka");
        movieAllotment.setTimings(Arrays.asList(Timing.AFTERNOON, Timing.EVENING));

        movie = new Movie(1234, "Indianajones", 100, "UA");

        date = new SimpleDateFormat("yyyy-MM-dd").getCalendar().getTime();
        movieSchedule = new MovieSchedule(1234, 1234, 1234, date, Arrays.asList(Timing.AFTERNOON).toString() );
    }

    @Test
    void testAddTheatre() {

        Theatre theatre = new Theatre(null, "PVR", "Yelahanka", "Bangalore", "Karnataka", "India", "560064", null, null);
        String name = theatreService.addTheatre(data);
        Assertions.assertThat(name).isNotNull().isNotEmpty().isEqualTo("PVR");

    }

    @Test
    void testGetUnknownTheatre() {

        Mockito.when(theatreRepository.findById(1234)).thenReturn(null);
        Theatre theatre = theatreService.getTheatre("1234");
        Assertions.assertThat(theatre).isNull();

    }

    @Test
    void testGetAvailableTheatre() {

        Mockito.when(theatreRepository.findById(1234)).thenReturn(Optional.of(data));
        Theatre theatre = theatreService.getTheatre("1234");
        Assertions.assertThat(theatre).isNotNull().isEqualTo(data);

    }

    @Test
    void testFindUnknownTheatreByName() {

        Mockito.when(theatreRepository.findByNameAndArea("PVR", "Yelahanka")).thenReturn(null);
        Theatre theatre = theatreService.findTheatre("PVR", "Yelahanka");
        Assertions.assertThat(theatre).isNull();

    }

    @Test
    void testFindKnownTheatreByName() {

        Mockito.when(theatreRepository.findByNameAndArea("PVR", "Yelahanka")).thenReturn(data);
        Theatre theatre = theatreService.findTheatre("PVR", "Yelahanka");
        Assertions.assertThat(theatre).isNotNull().isEqualTo(data);

    }

    @Test
    void testAddInvalidDateForTheDay()  {
        org.junit.jupiter.api.Assertions.assertThrows(BadDataException.class, () -> {
            theatreService.addMovieForTheDay(null, null, "PVR");
        });
    }

    @Test
    void testAddUnknownMovieForTheDay() throws BadDataException {

        Mockito.when(theatreRepository.findByNameAndArea("PVR", "Yelahanka")).thenReturn(data);
        Mockito.when(restTemplate.getForEntity("http://MOVIESERVICE/movies?name="+"Indianajones", Movie.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        String result = theatreService.addMovieForTheDay("PVR", movieAllotment, "2022-07-20");
        Assertions.assertThat(result).isNull();

    }

    @Test
    void testAddKnownMovieForTheDay() throws BadDataException {

        Mockito.when(theatreRepository.findByNameAndArea("PVR", "Yelahanka")).thenReturn(data);
        Mockito.when(restTemplate.getForEntity("http://MOVIESERVICE/movies?name="+"Indianajones", Movie.class))
                .thenReturn(new ResponseEntity<>(movie, HttpStatus.OK));

        String result = theatreService.addMovieForTheDay("PVR", movieAllotment, "2022-07-20");
        Assertions.assertThat(result).isNotNull().isEqualTo(movie.getName());

    }

    @Test
    void testUpdateUnknownMovieForTheDay() throws BadDataException {

        Mockito.when(theatreRepository.findByNameAndArea("PVR", "Yelahanka")).thenReturn(data);
        Mockito.when(restTemplate.getForEntity("http://MOVIESERVICE/movies?name="+"Indianajones", Movie.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        String result = theatreService.updateMovieForTheDay("PVR", movieAllotment, "2022-07-20");
        Assertions.assertThat(result).isNull();

    }

    @Test
    void testUpdateKnownMovieForTheDay() throws BadDataException {

        Mockito.when(theatreRepository.findByNameAndArea("PVR", "Yelahanka")).thenReturn(data);
        Mockito.when(restTemplate.getForEntity("http://MOVIESERVICE/movies?name="+"Indianajones", Movie.class))
                .thenReturn(new ResponseEntity<>(movie, HttpStatus.OK));
        Mockito.when(movieScheduleRepository.findByTheatreIdAndMovieIdAndDate(ArgumentMatchers.any(Integer.class),
                        ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Date.class)))
                .thenReturn(movieSchedule);

        String searchDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String result = theatreService.updateMovieForTheDay("PVR", movieAllotment, searchDate);
        Assertions.assertThat(result).isNotNull().isEqualTo(movie.getName());

    }

}
