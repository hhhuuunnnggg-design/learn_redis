package com.example.demo.weather.service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

// nhiệm vụ của client là lấy dữ liệu từ external service(có thể hiểu là thời tiết) và lưu vào cache
@Service
public class ExternalServiceClient {
    // *khi đã có dữ liệu trong cache, nếu có update thì @CachePut sẽ cập nhật dữ
    // liệu trong cache*
    @CachePut(value = "weather", key = "#zip")
    public int getWeatherInfo(int zip) {
        return ThreadLocalRandom.current().nextInt(60, 100);
    }

}