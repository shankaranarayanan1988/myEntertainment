package com.javaapps.movie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.javaapps.movie.db.entity.Movie;
import com.javaapps.movie.service.MovieServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MovieController.class)
@Log4j2
public class MovieControllerTest {

    @MockBean
    private MovieServiceImpl movieService;

    @Autowired
    private MockMvc mockMvc;

    private static Movie data;

    @BeforeAll
    public  static  void init() {
        data = new Movie(null, "Avengers", 100, "UA");
    }

    @Test
    void testAddMovie() throws Exception{
        String uri = "/movies/";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(data );

        Mockito.when(movieService.addmovie(data)).thenReturn(data.getName());

        mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType("application/json").content(requestJson)).
                andDo(print()).
                andExpect(status().isOk());
    }

    @Test
    void testGetUnknownMovie() throws Exception{
        String uri = "/movies/1234";

        Mockito.when(movieService.getmovie("1234")).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get(uri)).
                andDo(print()).
                andExpect(status().isOk());
    }

    @Test
    void testGetKnownMovie() throws Exception{
        String uri = "/movies/1234";

        Mockito.when(movieService.getmovie("1234")).thenReturn(data);
        mockMvc.perform(MockMvcRequestBuilders.get(uri)).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.name").value(data.getName()));
    }

    @Test
    void testFindUnknownMovie() throws Exception{
        String uri = "/movies?name=Avengers";

        Mockito.when(movieService.findmovie("Avengers")).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get(uri)).
                andDo(print()).
                andExpect(status().isOk());
    }

    @Test
    void testFindKnownMovie() throws Exception{
        String uri = "/movies?name=Avengers";

        Mockito.when(movieService.findmovie("Avengers")).thenReturn(data);
        mockMvc.perform(MockMvcRequestBuilders.get(uri)).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.name").value(data.getName()));
    }
}
