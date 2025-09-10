package com.example.demo.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.weather.service.WeatherService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("weather")
public class WeatherController {

    @Autowired
    private WeatherService service;

    @GetMapping("{zip}")
    public Mono<Integer> getWeather(@PathVariable int zip) {
        return Mono.fromSupplier(() -> this.service.getInfo(zip));
    }

}