package com.example.demo.redistion.test.Redisspring;

import org.junit.jupiter.api.RepeatedTest;
import org.redisson.api.RAtomicLongReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest // chạy test trong context Spring Boot (nạp bean đầy đủ)
class RedisSpringApplicationTests {

    @Autowired
    private ReactiveStringRedisTemplate template; // Bean của Spring Data Redis Reactive API

    @Autowired
    private RedissonReactiveClient client; // Bean của Redisson Reactive API

    @RepeatedTest(3) // chạy lại test này 3 lần

    //hàm này nó đang đo hiệu xuất với 500_000
    void springDataRedisTest() {
        // lấy ra ValueOperations để thao tác với key-value string
        ReactiveValueOperations<String, String> valueOperations = this.template.opsForValue();

        long before = System.currentTimeMillis(); // bắt đầu đếm thời gian

        // tạo Flux từ 1 → 500000
        Mono<Void> mono = Flux.range(1, 500_000)
                .flatMap(i -> valueOperations.increment("user:1:visit")) // mỗi phần tử gọi INCR user:1:visit
                .then(); // sau khi chạy hết thì return Mono<Void>

        // StepVerifier dùng để subscribe và verify kết quả reactive stream
        StepVerifier.create(mono)
                .verifyComplete(); // đảm bảo stream chạy xong mà không lỗi

        long after = System.currentTimeMillis(); // kết thúc đếm thời gian
        System.out.println((after - before) + " ms"); // in ra thời gian chạy
    }

    @RepeatedTest(3) // chạy lại test này 3 lần
    void redissonTest() {
        // lấy AtomicLong reactive từ Redisson, key là user:2:visit
        RAtomicLongReactive atomicLong = this.client.getAtomicLong("user:2:visit");

        long before = System.currentTimeMillis();

        // tương tự như trên, chạy 500000 lần increment
        Mono<Void> mono = Flux.range(1, 500_000)
                .flatMap(i -> atomicLong.incrementAndGet()) // gọi INCR thông qua Redisson
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        long after = System.currentTimeMillis();
        System.out.println((after - before) + " ms");
    }

}
