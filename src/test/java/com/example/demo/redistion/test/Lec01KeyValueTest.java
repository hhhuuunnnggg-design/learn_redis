// Đây là 1 test case JUnit 5: bạn không run main App, mà run thẳng file test này
package com.example.demo.redistion.test;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec01KeyValueTest extends BaseTests {

    @Test // Đây là 1 test method, JUnit sẽ tự chạy khi bạn "Run Test"
    public void keyValueAccessTest() {
        // RBucketReactive tương ứng với 1 key trong Redis
        RBucketReactive<String> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE); // StringCodec.INSTANCE:
                                                                                                     // giải mã hóa
                                                                                                     // value

        // set key "user:1:name" = "sam" vào Redis (kiểu reactive, trả về Mono<Void>)
        Mono<Void> setkey = bucket.set("sam");

        // get key "user:1:name" từ Redis (trả về Mono<String>)
        Mono<String> getkey = bucket.get()
                .doOnNext(System.out::println); // in ra giá trị lấy được

        // StepVerifier là lib test reactive stream
        // set.then(get).then() => chuỗi thao tác set -> get -> hoàn tất
        // verifyComplete() => đảm bảo chuỗi reactive chạy xong không lỗi
        StepVerifier.create(setkey.then(getkey).then()).verifyComplete();
    }

    @Test
    public void keyValueExpireTest() {
        RBucketReactive<String> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> setkey = bucket.set("sam", 10, TimeUnit.SECONDS);
        Mono<String> getkey = bucket.get()
                .doOnNext(System.out::println);
        StepVerifier.create(setkey.then(getkey).then()).verifyComplete();
    }

    @Test
    public void keyValueExtendExpireTest() {
        RBucketReactive<String> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> setkey = bucket.set("sam", 10, TimeUnit.SECONDS);
        Mono<String> getkey = bucket.get()
                .doOnNext(System.out::println);
        StepVerifier.create(setkey.then(getkey).then()).verifyComplete();
        // extend expire
        sleep(5000);
        Mono<Boolean> extend = bucket.expire(10, TimeUnit.SECONDS);

        StepVerifier.create(extend).expectNext(true).verifyComplete();
        // access expire TimeUnit
        Mono<Void> ttl = bucket.remainTimeToLive().doOnNext(System.out::println).then();
        StepVerifier.create(ttl).verifyComplete();
    }

}
