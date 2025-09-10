package com.example.demo.city.service;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.city.client.CityClient;
import com.example.demo.city.dto.City;

import reactor.core.publisher.Mono;

@Service
public class CityService {

    @Autowired
    private CityClient cityClient;

    private RMapReactive<String, City> cityMap;

    public CityService(RedissonReactiveClient client) {
        this.cityMap = client.getMap("city", new TypedJsonJacksonCodec(String.class, City.class));
    }

    public Mono<City> getCity(final String zipCode) {
        // return this.cityMap.get(zipCode)
        // .onErrorResume(ex -> this.cityClient.getCity(zipCode));

        // đây là code thủ công, chứa nhiều logic, nếu không muốn code thủ công thì thêm
        // @Cacheable vào method getCity
        return this.cityMap.get(zipCode).switchIfEmpty(
                this.cityClient.getCity(zipCode) // tìm dữ liệu trong Redis map (nếu có).
                        .flatMap(C -> this.cityMap.fastPut(zipCode, C).thenReturn(C))); // tự push dữ liệu vào redis map
    }

    // @Scheduled(fixedRate = 10_000)
    public void updateCity() {
        this.cityClient.getAll()
                .collectList()
                .map(list -> list.stream().collect(Collectors.toMap(City::getZip, Function.identity())))
                .flatMap(this.cityMap::putAll)
                .subscribe();
    }

}