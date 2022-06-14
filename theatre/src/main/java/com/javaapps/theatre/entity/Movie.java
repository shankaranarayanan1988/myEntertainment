package com.javaapps.theatre.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    private Integer id;
    private String name;
    private Integer screenTime;
    private String certificate;

}
