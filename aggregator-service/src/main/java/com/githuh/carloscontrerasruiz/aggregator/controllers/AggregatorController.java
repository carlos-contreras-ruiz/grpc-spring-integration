package com.githuh.carloscontrerasruiz.aggregator.controllers;

import com.githuh.carloscontrerasruiz.aggregator.dto.RecommendedMovie;
import com.githuh.carloscontrerasruiz.aggregator.dto.UserGenre;
import com.githuh.carloscontrerasruiz.aggregator.service.UserMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AggregatorController {

    @Autowired
    private UserMovieService userMovieService;


    @GetMapping("/user/{loginId}")
    public List<RecommendedMovie> getMovies(@PathVariable String loginId) {
        System.out.println("Llega aqui");
        return this.userMovieService.getUserMovieSuggestion(loginId);
    }

    @PutMapping("/user")
    public void setUserGenre(@RequestBody UserGenre userGenre) {
        this.userMovieService.setUserGenre(userGenre);
    }
}
