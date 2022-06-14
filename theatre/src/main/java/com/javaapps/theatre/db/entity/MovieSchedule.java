package com.javaapps.theatre.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movie_schedule")
public class MovieSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "theatreId")
    private Integer theatreId;

    @Column(name = "movieId")
    private Integer movieId;

    @Column(name = "date")
    private Date date;

    @Column(name = "timings")
    private String timings;

}
