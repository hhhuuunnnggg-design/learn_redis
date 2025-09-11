package com.example.demo.redisperformance.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.redisperformance.entity.Product;

//ReactiveCrudRepository có các method trả về Mono<T> hoặc Flux<T>, để xử lý bất đồng bộ
@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
}
