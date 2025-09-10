package com.example.demo.fib.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FibService {

    // have a strategy for cache evict
    // @Cacheable = tự động cache kết quả method → tránh phải tính toán lại hoặc
    // truy vấn DB nhiều lần
    @Cacheable(value = "math:fib", key = "#index") // lưu kết quả vào cache
    public int getFib(int index) {
        System.out.println("calculating fib for " + index);
        return this.fib(index);
    }

    // PUT / POST / PATCH / DELETE
    // dùng để xóa cache
    @CacheEvict(value = "math:fib", key = "#index")
    public void clearCache(int index) {
        System.out.println("clearing hash key");
    }

    // @Scheduled(fixedRate = 10_000)
    // xóa sạch cache
    @CacheEvict(value = "math:fib", allEntries = true)
    public void clearCache() {
        System.out.println("clearing all fib keys");
    }

    // intentional 2^N
    private int fib(int index) {
        if (index < 2)
            return index;
        return fib(index - 1) + fib(index - 2);
    }

}