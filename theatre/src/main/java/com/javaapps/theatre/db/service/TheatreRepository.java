package com.javaapps.theatre.db.service;


import com.javaapps.theatre.db.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TheatreRepository extends JpaRepository<Theatre, Integer> {

    Theatre findByName(String name);

    Theatre findByNameAndArea(String name, String area);

    List<Theatre> findByAreaContaining(String area);

    List<Theatre> findByCityContaining(String city);

    List<Theatre> findByStateContaining(String state);

    List<Theatre> findByCountryContaining(String country);

    List<Theatre> findByPinCodeContaining(String pinCode);

}
