package com.example.demo.redistion.test;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLongReactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec03NumberTest extends BaseTests {

    @Test
    public void keyValueIncreaseTest() {
        // get user:1:visit
        RAtomicLongReactive atomicLong = this.client.getAtomicLong("user:1:visit");
        Mono<Void> mono = Flux.range(1, 30).delayElements(Duration.ofSeconds(1))
                .flatMap(i -> atomicLong.incrementAndGet()).then();
        StepVerifier.create(mono).verifyComplete();
    }
}
