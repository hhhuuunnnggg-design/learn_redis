package com.example.demo.redistion.test;

import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec04BucketAsMapTest extends BaseTests {

    // set usser:1:name sam
    // set usser:2:name hung
    // set usser:1:name huy
    // xong rồi run
    @Test
    public void bucketAsMap() {
        Mono<Void> mono = this.client.getBuckets(StringCodec.INSTANCE).get("user:1:name", "user:2:name", "user:3:name")
                .doOnNext(System.out::println).then();
        StepVerifier.create(mono).verifyComplete();

    }

}
