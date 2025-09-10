package com.example.demo.weather.service;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

// nhiệm vụ của service là lấy dữ liệu từ client và lưu vào cache
@Service
public class WeatherService {

    @Autowired
    private ExternalServiceClient client;

    @Cacheable("weather")
    public int getInfo(int zip) {
        // return this.client.getWeatherInfo(zip);
        return 0;
    }

    @Scheduled(fixedRate = 5_000) // chạy 10s một lần
    public void update() {
        System.out.println("updating weather");
        IntStream.rangeClosed(1, 5)
                .forEach(this.client::getWeatherInfo);
    }

}