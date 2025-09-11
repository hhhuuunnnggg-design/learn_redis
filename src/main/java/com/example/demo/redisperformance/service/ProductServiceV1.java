package com.example.demo.redisperformance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.redisperformance.entity.Product;
import com.example.demo.redisperformance.repository.ProductRepository;

import reactor.core.publisher.Mono;

@Service
public class ProductServiceV1 {

    @Autowired
    private ProductRepository repository;

    public Mono<Product> getProduct(int id) {
        return this.repository.findById(id);
    }

    public Mono<Product> updateProduct(int id, Mono<Product> productMono) {
        return this.repository.findById(id) // 1️⃣ Tìm product trong DB theo id (Mono<Product>)
                .flatMap(p ->
                // 2️⃣ Nếu tìm thấy → lấy product mới (từ client)
                // và gán id cũ vào để đảm bảo update đúng bản ghi
                productMono.doOnNext(pr -> pr.setId(id)))
                // 3️⃣ Lưu product mới vào DB (update nếu id tồn tại, insert nếu chưa có)
                .flatMap(this.repository::save);
        // 4️⃣ Trả về Mono<Product> sau khi save
    }

}
