package com.javaapps.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetails {

    private String name;

    private Integer screenTime;

    private String certificate;

    private Set<String> cast;

    private Set<String> language;

}