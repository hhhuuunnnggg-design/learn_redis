package com.example.demo.redistion.test;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class EvenListenerTest extends BaseTests {

    @Test
    public void expiredEventTest() {
        RBucketReactive<String> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> setkey = bucket.set("sam", 10, TimeUnit.SECONDS);
        Mono<String> getkey = bucket.get()
                .doOnNext(System.out::println);
        // add listener
        Mono<Void> event = bucket.addListener(new ExpiredObjectListener() {
            @Override
            public void onExpired(String s) {
                System.out.println("Event: " + s);
            }
        }).then();
        // Transform getkey to Mono<Void> using then()
        StepVerifier.create(setkey.concatWith(getkey.then()).concatWith(event)).verifyComplete();
        // extend expire
        sleep(11000);
    }
}