package com.example.demo.redistion.test;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.TypedJsonJacksonCodec;

import com.example.demo.redistion.test.dto.Student;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec02KeyValueObject extends BaseTests {

    @Test
    public void keyValueObjectTest() {
        Student student = new Student("Nguyen Van A", 20, "HCM", Arrays.asList(1, 2, 3));
        // JsonJacksonCodec.INSTANCE: dùng để giải mã hóa và mã hóa object Student
        RBucketReactive<Student> bucket = this.client.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        Mono<Void> set = bucket.set(student);
        Mono<Void> get = bucket.get().doOnNext(System.out::println).then();
        StepVerifier.create(set.then(get)).verifyComplete();
    }
}
